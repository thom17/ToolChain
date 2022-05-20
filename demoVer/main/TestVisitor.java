package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import dataSet.Class;
import dataSet.Function;
import dataSet.Member;
import dataSet.statement.StateNode;
import testDataSet.ClassInfo;
import testDataSet.Datalist;
import testDataSet.FunctionInfo;
import testDataSet.MemberInfo;

public class TestVisitor extends VoidVisitorAdapter<StringBuilder>
{
	private Datalist datalist;	//데이터를 저장할 공간 (모든 정보를 포함할 데이터)
	private ClassInfo cls;	//현제 타겟 클래스
	
	public TestVisitor(Datalist list)	
	{
		this.datalist = list;
	}
	
	public void visit(ClassOrInterfaceDeclaration n, StringBuilder arg)
	{
		//n.toString() 함수를 통해 해당 노드의 내용을 string으로 받을 수 있음
		ResolvedReferenceTypeDeclaration resolve = n.resolve();		//편리한 정보의 추출을 위한 resolve를 구함.
		String srcName = resolve.getQualifiedName();	// packageName+"."+className과 동일
		
		cls = datalist.getClass(srcName);	//hashmap에 없다면 null이 반환됨.
		if(cls == null)	
		{
			String className = n.getName().toString();	//클래스의 이름은 해석이 필요가 없음
			String packageName = resolve.getPackageName();	//패키지의 정보는 해석이 필요하기에 resolve가 이용됨.
			
			cls = new ClassInfo(className, packageName);	//현재 정보로 클래스를 생성
			datalist.addClass(cls);			//전체 정보에 해당 클래스를 추가함.
		}
		super.visit(n, arg);	//해당 노드의 정보를 한번더 순환함.
	}
		
	public void visit(FieldDeclaration n, StringBuilder arg)
	{
		ResolvedFieldDeclaration resolve = n.resolve();	//필드선언 문은 resolve 없이는 정보 얻기가 불편함.
		String name = resolve.getName();
		String srcName = cls.getSrcName() + "." + name;	
		
		MemberInfo var = datalist.getMember(srcName);	//마찬가지로 언급된 적이 없는 정보는 생성되지 않았으므로 null을 반환
		if(var == null)
		{
			var = new MemberInfo(cls ,name);	//생성자로부터 cls의 haslist에 자동 추가됨.
			datalist.addMember(var);	//생성된 맴버 정보 객체를 datalist에 추가.
		}
		//super.visit(n, arg);	넣어도 상관은 없지만 필드 선언 내부의 정보에는 필요한 정보가 없음.
	}
	
	public void visit(MethodDeclaration n, StringBuilder arg)
	{
		ResolvedMethodDeclaration resolve = n.resolve();
		String srcSignature = resolve.getQualifiedSignature();	//src규칙과 동일 == 패키지.클래스.함수명(타입정보) 

		FunctionInfo fun = datalist.getFunction(srcSignature);
		if(fun == null)
		{
			//String name = n.getName().toString(); 이름만 가져오는것도 가능은 하다.
			String sig = resolve.getSignature();// src에서 패키지.클래스가 생략된 정보 함수명(타입정보)
			fun = new FunctionInfo(cls, sig);
			datalist.addFunction(fun);
		}
		arg.append(srcSignature);	//함수 내부를 순환하기전 해당 함수의 src를 스트링 빌더에 추가
		super.visit(n, arg);	//함수의 경우 내부 구조가 있으며 함수의 호출정도는 잡는것이 목표이므로 재방문.
		arg.delete(0, arg.length());	//방문이 끝난 후 arg를 비움
	}
	
	public void visit(MethodCallExpr n, StringBuilder arg) 
	{
		FunctionInfo hostFun = datalist.getFunction(arg.toString());	//null일 수가 없음. 메소드 주인.
		
		ResolvedMethodDeclaration resolve = n.resolve();	//해당 노드의 resolve
		
		//호출하는 함수의 소유자의 클래스 정보 접근
		ResolvedClassDeclaration clientClassResolve = resolve.declaringType().asClass();	
		String clientSrcClassName = clientClassResolve.getQualifiedName(); //호출자 Src 이름 
		ClassInfo clientClass = datalist.getClass(clientSrcClassName);	//datalist에서 해당 클래스 찾기
		
		String clientFunSrc = resolve.getQualifiedSignature();	//호출하는 함수의 src
		FunctionInfo clientFun = datalist.getFunction(clientFunSrc); //호출하는 함수 datalist에서 가져오기
		
		if(clientClass == null)	//호출하는 클래스가 아직 입력되지 않은 경우나  System.out과 같은 api인 경우는 항상
		{
			String packageName = clientClassResolve.getPackageName();
			String name = clientClassResolve.getName();
			
			clientClass = new ClassInfo(name, packageName);
			datalist.addClass(clientClass);
			
			//이 경우 항상 함수도 없음.
			String sig = resolve.getSignature();
			clientFun = new FunctionInfo(clientClass, sig);
			datalist.addFunction(clientFun);
			
		}else if (clientFun == null)
		{
			String sig = resolve.getSignature();
			clientFun = new FunctionInfo(clientClass, sig);
			datalist.addFunction(clientFun);
		}

		//host 함수의 callList 추가
		hostFun.getCallList().addFunction(clientFun);
		hostFun.getCallList().addClass(clientClass);
		
		//host 함수의 소유자인 class 역시 client 함수와 클래스를 call한다고 볼 수 있음
		cls.getCallList().addFunction(clientFun);
		cls.getCallList().addClass(clientClass);
	
		super.visit(n, arg);	//호출하는 파라미터안에서 호출이 가능하므로 재 순환 필요.
	}
}
