package visitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry.Type;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import complexity.ClassComplexity;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dataSet.statement.StateNode;
import dev.khe.DiagramRecover.TypeUtil;
import diagramMaker.usecase.UseCase;
import main.Parser;
import dataSet.Class;

public class DataMaker2 extends VoidVisitorAdapter<StringBuilder>
{
	private OMS datalist;
	Class hostClass;
	public static void main(String[] args) throws IOException, InterruptedException 
	{	
		//makeInitFile();
		Parser.initialize(new File("target_project.info"));
		System.out.println("Parser initialize Finish");
		
		ArrayList<CompilationUnit> list = Parser.makeASTList();
		
		OMS datalist = new OMS();
		DataMaker dataMaker = new DataMaker(datalist);
		ArrayList<String> classList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for(CompilationUnit ast : list)
		{
			new DataMaker2(ast, new OMS());
		}

	}
	public DataMaker2(CompilationUnit ast, OMS datalist)
	{
		this.datalist = datalist;
		String packageName = "default";
		if( ast.getPackageDeclaration().isPresent() )
			packageName = ast.getPackageDeclaration().get().getName().asString();
			
		System.out.println("==================");
		for( TypeDeclaration<?> typeDec : ast.getTypes() )
		{
			if ( typeDec.isTypeDeclaration() )
			{
				String name =  typeDec.getName().asString();
				Class cls = new Class(packageName, name);
				datalist.addData(cls);
				classDef(typeDec.asClassOrInterfaceDeclaration(), cls);
				
			}	
		}
	}
	private void classDef(ClassOrInterfaceDeclaration n, Class cls) 
	{
		ResolvedReferenceTypeDeclaration resolve = n.resolve();

		String srcName = resolve.getQualifiedName();

		// 슈퍼클래스 상속 관련 구문 처리 
		setExtendClass(cls, n);
		setImplementedClass(cls, n);
				
		cls.setInterface(resolve.isInterface());
		cls.setIsInterface(resolve.isInterface());
		setModif(cls, n.getModifiers() );
		
	}
	public DataMaker2(OMS datalist)	
	{
		this.datalist = datalist; 
	};
	
	private void setExtendClass(Class target, ClassOrInterfaceDeclaration n)
	{
		NodeList<ClassOrInterfaceType> extendList = n.getExtendedTypes();
		int size = extendList.size();
		if( 0 < size )
		{
			Class extendClassList[] = new Class[size];
			String srcName[] = new String[size];
			for(int i = 0; i < size; i++)
			{
				//ResolvedReferenceType resolve = extendList.get(i).resolve();
				//String extendSrc = resolve.getQualifiedName();
				ClassOrInterfaceType extendCls = extendList.get(i);
				ResolvedReferenceType resolve = extendCls.resolve();
				String extendSrc = resolve.getQualifiedName();
				
				extendClassList[i] = datalist.findClass(extendSrc);
				srcName[i] = extendSrc;
				if(extendClassList[i] == null)
				{
					extendClassList[i] = new Class(extendSrc);
					datalist.addData (extendClassList[i]);					
				}	
			}
			
			target.setExtendClass(extendClassList);	
			target.setExtendSrcName(srcName);
		}
		
	}
	private void setImplementedClass(Class target, ClassOrInterfaceDeclaration n)
	{
		NodeList<ClassOrInterfaceType> extendList = n.getImplementedTypes();
		int size = extendList.size();
		if( 0 < size )
		{
			Class extendClassList[] = new Class[size];
			String srcName[] = new String[size];
			for(int i = 0; i < size; i++)
			{
				ResolvedReferenceType resolve = extendList.get(i).resolve();
				String extendSrc = resolve.getQualifiedName();
				String extendName = extendList.get(i).getName().asString();
				
				extendClassList[i] = datalist.findClass(extendSrc);
				srcName[i] = extendSrc;
				if(extendClassList[i] == null)
				{
					extendClassList[i] = new Class(extendSrc);
					datalist.addData(extendClassList[i]);					
				}	
			}
			
			target.setImplementClass(extendClassList);	
			target.setImplementSrcName(srcName);
		}
		
	}
	public void visit(PackageDeclaration n , StringBuilder arg)
	{
		arg.append(n.toString());
	}
	

	public void setModif(Data_base target, NodeList<Modifier> list )
	{
		for(Modifier modfi : list)
		{
			String str = modfi.toString();
		//	System.out.println("mod : "+str);
			if(str.equals("private ") || str.equals("protected "))
				target.setModfi(str);
			else if(str.equals("static "))
				target.setStatic(true);
			else if(str.equals("abstract "))
				target.setAbstract(true);
		//	target.setAbstract(isAbstract);
		}
	}
	
	@Override	//필드 선언 body
	public void visit(FieldDeclaration n, StringBuilder arg)
	{
		Class cls = hostClass;
		String type = "null";
		for(VariableDeclarator dec : n.getVariables()) //int x, y, z 와 같은경우?
		{
			String name = dec.getName().asString();
			type = dec.getType().asString();
			Member member = new Member(type, name, cls);	
			
			setModif(member, n.getModifiers());
			cls.addHas(member);
			datalist.addData(member);	
		}
		

		//super.visit(n, arg);
		
		//////system.outprintln(n.resolve().getType().toString()); //error
	}
	
	@Override	//생성자 정의 body
	public void visit(ConstructorDeclaration n, StringBuilder arg) 
	{
		Class cls = hostClass;
		ResolvedConstructorDeclaration resolve = n.resolve();
		
		String srcSignature = resolve.getQualifiedSignature();
		
		String forDebug = resolve.getSignature();
		if(forDebug.equals("checkRecipe(coffeeShop.Resource)"))
			System.out.println("for debug");
		Function fun = datalist.findFunction(srcSignature);
		if(fun == null)
		{
			fun = new Function(resolve.getSignature(), cls);
		//	fun = new Function( type, name, cls );
			datalist.addData(fun);
		}
	
		String name = n.getName().asString();
		String type = hostClass.getName();
		//fun.setNode(n.getBody());
		fun.setInit(type, name);
		
		setParameter(n.getParameters() , fun);
		
		cls.addHas(fun);
		////system.outprintln("\n}");
		super.visit(n, arg);
		//////system.outprintln("after arg is "+arg);
	}

	private void setParameter(NodeList<Parameter> list, Function fun) 
	{
		String type[] = null;
		String name[] = null;
		if( ! list.isEmpty() )
		{
			int size = list.size();
			type = new String[size];
			name = new String[size];
			
			for(int i=0; i<size; i++ )
			{
				
				Parameter info = list.get(i);
				type[i] = info.getType().toString();
				name[i] = info.getName().asString();
				Member parameter = new Member(type[i] , name[i], fun);
				fun.addHas( parameter);
				datalist.addData(parameter);
				
			}
			fun.setParameter(type, name);
		}
	}

	public void setFunctionModifier(ResolvedMethodDeclaration n, Function fun)
	{
		fun.setStatic(n.isStatic());
		fun.setAbstract(n.isAbstract());
	}
	
	private void setParameter(ResolvedMethodDeclaration resolve, Function function)
	{
		ResolvedParameterDeclaration parameter;
		String typeParam[] = null;
		String nameParam[] = null;
		if(resolve.getNumberOfParams() > 0)
		{
			typeParam = new String[resolve.getNumberOfParams()];
			nameParam = new String[resolve.getNumberOfParams()];
			for(int i=0; i < resolve.getNumberOfParams(); i++)
			{	
				parameter = resolve.getParam(i);
				String desc = parameter.describeType();
				nameParam[i] = parameter.getName();
				typeParam[i] = TypeUtil.getSimpleTypeName( TypeUtil.getTypeStr( parameter.getType() ) );
			}		
			function.setParameter(typeParam, nameParam);
		}
	}
	
	@Override	//함수 정의 body
	public void visit(MethodDeclaration n, StringBuilder arg) 
	{
		Class cls = hostClass;
		ResolvedMethodDeclaration resolve = n.resolve();
		NodeList<MethodCallExpr> list = new NodeList<MethodCallExpr>();
		Object obj =n.findAll(	MethodCallExpr.class);
		String srcSignature = resolve.getQualifiedSignature();
		System.out.println("dataMaker (srcSig) : "+srcSignature);
		System.out.println("dataMake (sig) : " + n.getSignature());
		String forDebug = resolve.getSignature();
		if(forDebug.equals("setWater(int)"))
			System.out.println("for debug");
		Function fun = datalist.findFunction(srcSignature);
		if(fun == null)
		{
			fun = new Function(resolve.getSignature(), cls);
		//	fun = new Function( type, name, cls );
			datalist.addData(fun);
		}
		
		String name = n.getName().asString();
		String type = n.getType().toString();
		
		fun.setInit(type, name);
	//	if(n.getBody().isPresent())
	//		fun.setNode(n.getBody().get());
		
		setFunctionModifier(resolve, fun);
		setParameter ( n.getParameters(), fun);
		//setParameter(resolve, fun);	resolve = ResolvedMethodDeclaration 만 가능해서 constructor와 함께 적용 못함.
		
		cls.addHas(fun);
		arg.append("."+fun.getName()+fun.getParameterName());
		
		////system.outprintln("\n}");
		super.visit(n, arg);
		//////system.outprintln("after arg is "+arg);
	}



}
