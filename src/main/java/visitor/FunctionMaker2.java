package visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

import dataSet.DataList;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dataSet.statement.MethodCallStateNode;
import dataSet.statement.StateNode;
import dataSet.Class;
import dev.khe.DiagramRecover.TypeUtil;
import dev.khe.DiagramRecover.model.MOperationCall;

public class FunctionMaker2 extends VoidVisitorAdapter<StringBuilder>
{
	Function function;
	DataList datalist;
	StateNode stateNode;
	StateNode head;
	String condition = "true";
//	
//	public FunctionMaker2(Function fn, DataList datalist)
//	{
//		this.datalist = datalist;
//		init(fn);
//		System.out.println("start Parsing ---" + fn.getSrcName());
//		//System.out.println(fn.getNode().toString());
//		System.out.println("End Parsing ================");
//	}
//	public FunctionMaker2(Function function, DataList datalist, StateNode stateNode)
//	{
//		this.function = function;
//		this.datalist = datalist;
//		this.head = stateNode;
//		this.stateNode = stateNode;
//		this.condition =condition;
//	}
//	public void init(Function fn)
//	{
//		function = fn;
//		stateNode = new StateNode(fn.getParameterName(), condition, fn.getName()+"parameter");
//		head = stateNode;
//		fn.setHeadNode(head);
//		stateNode = head.setThen(new StateNode());
//		this.condition = "true";
//		BlockStmt block = fn.getNode();
//		if(block != null)
//			block.accept(this, new StringBuilder());
//		//for( Statement stmt : block.getStatements() )
//		//	parsing(stmt);
//	}
//	private void parsing(Statement stmt) 
//	{
//		System.out.println(stmt.toString()+" start");
//		if(stmt instanceof ExpressionStmt)
//		{
//			expressionStmt(stmt);
//		}
//		System.out.println("=========== end");
//	}
//	private void expressionStmt(Statement stmt) 
//	{
//		ExpressionStmt node = stmt.asExpressionStmt();
//		Expression exp = node.getExpression();
//		expressionParsing(exp);
//		
//	}
//	private void expressionParsing(Expression exp) 
//	{
//		if(exp instanceof VariableDeclarationExpr)
//			variableDeclarationExpr(exp);
//		else if(exp instanceof AssignExpr)
//			assignExpr(exp);
//		
//	}
//	private void assignExpr(Expression exp) 
//	{
//		AssignExpr n = exp.asAssignExpr();
//		{
//			System.out.println("-----------------");
//			System.out.println(n.toString());
//			System.out.println("=======================");
//			Data_base owner = this.function.getOwner();
//			////System.out.println("Write /"+n.toString());
//			Expression target = n.getTarget();
//			String targetName = n.getTarget().toString();
//			String valueImg = n.getValue().toString();
//		
//			stateNode.init(n.toString(), targetName+"_change","true");
//			stateNode = stateNode.setThen(new StateNode() );
//			
//			if( target.isArrayAccessExpr() )
//			{
//				ArrayAccessExpr arrayExp = target.toArrayAccessExpr().get();
//				targetName = arrayExp.getName().toString();
//				
//			}else if(target.isFieldAccessExpr() )
//			{
//				System.out.println("field Access : "+n.toString());
//				ResolvedValueDeclaration targetResolve = target.asFieldAccessExpr().resolve();
//				targetName = targetResolve.getName();
//				String classSrcName = owner.getSrcName();
//				addMemberInfo(targetName, valueImg);
//			}
//			else	//this나 Array가 아니라면
//			{
//				Member localVal = getUseLocal(targetName);	//먼저 지역변수인지 확인해본다.
//				if(localVal != null)	//로컬변수인 경우임.
//				{
//					localVal.addImage(valueImg);
//				}
//				else	//그 외는 this를 사용하지 않은 맴버값 접근임.
//				{
//					addMemberInfo(targetName, valueImg);
//				}
//				
//			}
//			//super.visit(n, arg);
//		}
//	}
//	private void variableDeclarationExpr(Expression exp) 
//	{
//		VariableDeclarationExpr node = exp.asVariableDeclarationExpr();
//		Object modis = node.getModifiers();
//		Object vars = node.getVariables();
//		ResolvedType resolve = node.calculateResolvedType();
//		System.out.println(resolve.toString());
//	}
//	public StateNode getNodeHead() {return head; };
//	public StateNode getNode() {return stateNode; }
//
//	public void visit(VariableDeclarationExpr n, StringBuilder arg)
//	{
//		
//		stateNode.init(n.toString(), "true", "varDef");
//		stateNode = stateNode.setThen(new StateNode() );
//		
//		NodeList<VariableDeclarator> varList =n.getVariables();
//		for(VariableDeclarator var : varList)
//		{
//			String type = var.getType().toString();
//			String name = var.getName().toString();
//			////System.out.println(n.toString()+" : "+name);
//			Member member = new Member(type , name, function);
//			function.addHas(member);
//			datalist.addData(member);
//			Optional<Expression> exp = var.getInitializer();
//			
//			if(exp.isPresent())
//				member.addImage(exp.get().toString() );
//		}
//		//String variable = n.getVariables().toString();
//		////System.out.println("Local Val /"+n);
//	}
//	public void visit(FieldAccessExpr n, StringBuilder arg)
//	{
//		////System.out.println("FieldAccess /"+n.toString());
//		
//	}
//	private Member addMemberInfo(String name, String valueImg)
//	{
//		Data_base owner = function.getOwner();
//		String classSrcName = owner.getSrcName();
//		Member member = this.datalist.findMember(classSrcName+"."+name);
//	
//		if(member == null) 	//상속 받은 맴버 임. 상속 클래스들 탐색해야함. 일단 임시방편으로 작업
//		{
//			if( owner instanceof Class )
//				member = findSuperMember(name, (Class)owner);	//한계가 있음.
//		}
//		else
//			member.addImage(valueImg);
//		
//		this.function.addCall(member);
//	
////		addNode(n.toString(), name+"_change");	//아무튼 맴버 값 변경함.
//		return member;
//	}
//	private Member findSuperMember(String name, Class target)
//	{
//		Member member = null;
//		if(target.getSuperClass() != null) 
//		{
//			for(Class superClass : target.getSuperClass() )
//			{
//				if(superClass != null)
//					member = superClass.getHasList().findMember(superClass.getSrcName()+"."+name);
//				if(member != null) break;
//			}
//		}
//		return member;
//	}
//	public void visit(AssignExpr n, StringBuilder arg)
//	{
//		System.out.println("-----------------");
//		System.out.println(n.toString());
//		System.out.println("=======================");
//		Data_base owner = this.function.getOwner();
//		////System.out.println("Write /"+n.toString());
//		Expression target = n.getTarget();
//		String targetName = n.getTarget().toString();
//		String valueImg = n.getValue().toString();
//	
//		stateNode.init(n.toString(), targetName+"_change","true");
//		stateNode = stateNode.setThen(new StateNode() );
//		
//		if( target.isArrayAccessExpr() )
//		{
//			ArrayAccessExpr arrayExp = target.toArrayAccessExpr().get();
//			targetName = arrayExp.getName().toString();
//			
//		}else if(target.isFieldAccessExpr() )
//		{
//			System.out.println("field Access : "+n.toString());
//			ResolvedValueDeclaration targetResolve = target.asFieldAccessExpr().resolve();
//			targetName = targetResolve.getName();
//			String classSrcName = owner.getSrcName();
//			addMemberInfo(targetName, valueImg);
//		}
//		else	//this나 Array가 아니라면
//		{
//			Member localVal = getUseLocal(targetName);	//먼저 지역변수인지 확인해본다.
//			if(localVal != null)	//로컬변수인 경우임.
//			{
//				localVal.addImage(valueImg);
//			}
//			else	//그 외는 this를 사용하지 않은 맴버값 접근임.
//			{
//				addMemberInfo(targetName, valueImg);
//			}
//			
//		}
//		super.visit(n, arg);
//	}
//	public Member getUseLocal(String id)
//	{
//		return function.getHasList().findMember(function.getSrcName()+"."+id);
//	}
//	public void visit(ReturnStmt n, StringBuilder arg)
//	{
//		if(function.getOwner().getName().equals("Client"))
//			System.out.println("ReturnExpr : "+n.toString());
//		
//		String result="";
//		if(n.getExpression().isPresent())
//		{
//			result = n.getExpression().get().toString();
//		}
//		stateNode.init(n.toString(), result+"_return");
//		stateNode = stateNode.setThen(new StateNode());
//		//System.out.println(result);
//		n.toString();
//		super.visit(n, arg);
//	}
//	public void visit(MethodCallExpr n, StringBuilder arg) 
//	{
//		stateNode.init(n.toString(), function.getName()+"_call", "true");
//		boolean check =n.isMethodReferenceExpr();	
//		ResolvedMethodDeclaration resolve = n.resolve();
//		
//		String sig = resolve.getSignature();
//		String srcName = resolve.getQualifiedSignature();	
//		ResolvedReferenceTypeDeclaration type =resolve.declaringType();
//		if( type != null && type.isClass() )
//		{
//			ResolvedClassDeclaration clientClassInfo = resolve.declaringType().asClass();
//			String srcClassName = clientClassInfo.getQualifiedName();
//			String className = clientClassInfo.getName();
//			String packageSrc = clientClassInfo.getPackageName();
//			Function clientFun = datalist.findFunction(srcName);
//			Class clientClass = datalist.findClass(srcClassName);
//			
//			if(clientClass == null)
//			{
//				clientClass = new Class(packageSrc , className);
//				datalist.addData(clientClass);
//			}
//			
//			if(clientFun == null)
//			{			
//				clientFun = new Function(sig, clientClass);
//				datalist.addData(clientFun);
//				clientClass.addHas(clientFun);				
//				//callClass.addHasFunction(callFun);
//			}
//			
//			function.addCall(clientFun);
//			function.addCall(clientClass);
//			MethodCallStateNode methodStateNode = new MethodCallStateNode();
//			methodStateNode.setFuns(clientFun, function);
//			stateNode = stateNode.init(n.toString(), function.getName()+"_call", "true", methodStateNode);
//		}
//		////System.out.println("=============\n");
//		stateNode = stateNode.setThen(new StateNode() );
//		super.visit(n, arg);
//		//////System.out.println(n.toString()+"\n str: "+qualifiedName+"/"+name+"/"+returnType);
//	}
//	public void visit(IfStmt n, StringBuilder arg)
//	{
//		String condition = n.getCondition().toString();
//		Statement thenStmt = n.getThenStmt();
//		StateNode thenEnd;
//		
//		stateNode.init("if", condition, "if");
//		StateNode thenNode = stateNode.setThen(new StateNode());
//		StateNode elseNode = stateNode.setElse(new StateNode());
//		StateNode elseEnd = this.stateNode;
//		StateNode ifHead = this.stateNode;
//		Optional<Statement> elseStmt = n.getElseStmt();
//		
//		FunctionMaker2 thenVisitor = new FunctionMaker2(function, datalist, thenNode);
//		FunctionMaker2 elseVisitor = new FunctionMaker2(function, datalist, elseNode);
//				
//		thenStmt.accept(thenVisitor, arg);
//		thenEnd = thenVisitor.getNode();
//	
//		if( elseStmt.isPresent() )
//		{
//			elseStmt.get().accept(elseVisitor, arg);
//			elseEnd = elseVisitor.getNode();
//		}
//		
//		StateNode connectNode = new StateNode();
//		thenEnd.init("//connect Node", "connect Node", function.getName()+"_ifFin", connectNode);
//		elseEnd.init("//connect Node", "connect Node", function.getName()+"_ifFin", connectNode);
//		this.stateNode = connectNode;
//	}
//	public void visit(SwitchStmt n, StringBuilder arg)
//	{
//		NodeList<SwitchEntry> entries = n.getEntries();
//		Expression exp = n.getSelector();
//		for(SwitchEntry swe : entries)
//		{
//			
//			System.out.println("type : "+swe.getType());
//			System.out.println("labels : "+swe.getLabels());
//			System.out.println("stmt : "+swe.getStatements());
//		}
//	}
}
