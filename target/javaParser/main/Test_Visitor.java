package main;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.Assignment;

import dataSet.Class;
import dataSet.DataList;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dataSet.stateNode.ForStateNode;
import dataSet.stateNode.StateNode;
import dataSet.stateNode.SwitchNode;
import dataSet.stateNode.VarDecNode;

public class Test_Visitor
{
	public static String packageName ="default";
	public static DataList datalist;
	public static StateNode stateNode;
	
	//DataList db 에 ast 파싱하여 데이터 추가
	public static void addDataList( CompilationUnit ast, DataList db )
	{
		PackageDeclaration pack = ast.getPackage();
		datalist = db;
		packageName = pack.getName().getFullyQualifiedName();
		List<AbstractTypeDeclaration> types = ast.types();
		for(AbstractTypeDeclaration type : types)
		{
			//System.out.println("type is "+type + ", "+type.getName().getFullyQualifiedName());
			typeDeclaration(type);
		}
		
	}
	
	//Block을 ArrayList<statment>로 변환하여 반환
	public static ArrayList<Statement> getStmtList( Statement block )
	{
		List ast;
		ArrayList<Statement> list = new ArrayList<Statement>();
		//ArrayList<Statement> list;
		if(block instanceof Block)
		{
			Block body =  (Block)block;
			 ast = body.statements();
			 for(int i=0; i<ast.size(); i++)
			 {
				list.add((Statement) ast.get(i));
			 }
		}
		else 
		{
			list.add(block);
		}
		
		return list;
	}
	
	//Class 선언문. 클래스 정보 생성후 메서드 필드 정보 추가 
	private static void typeDeclaration( AbstractTypeDeclaration type ) 
	{
		TypeDeclaration node = (TypeDeclaration)type;
		String name = node.getName().toString();
		Class cls = new Class(packageName , name);
		
		datalist.addData(cls);
		
		MethodDeclaration methodList[] = node.getMethods();
		FieldDeclaration memberList[]  = node.getFields();
		
		for(FieldDeclaration member : memberList)
		{
			fieldDeclaration(member, cls);
		}
		
		for(MethodDeclaration method : methodList)
		{
			methodDeclaration(method, cls);
		}
		cls.setComplex();
	}
	
	//변수 선언 구문 typeDeclaration으로부터 호출됨
	private static void fieldDeclaration( FieldDeclaration node, Data_base owner ) 
	{
		// TODO Auto-generated method stub
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) node.fragments().get(0);
		Type type = node.getType();
		String typeName = type.toString();
		String name = fragment.getName().getIdentifier();
		Member member = new Member(typeName, name, owner);
		owner.getHasList().addData(member);
		datalist.addData(member);
		
		Expression init = fragment.getInitializer();
		if(init != null)
			member.addImage(init.toString());
		
	}
	
	//메서드 정보 객체를 생성하기 위한 함수 생성자도 메서드로 판단되어 함수 사이즈를 위해 정의
	private static Function makeFunction( MethodDeclaration method, Data_base owner )
	{
		String name = method.getName().toString();
		List parameter = method.parameters();
		String paramStr = parameter.toString();
		
		String sig = name+"("+parameter.toString().substring(1, paramStr.length()-1) +")";
		Type type = method.getReturnType2();
		String returnType;
		if(type == null)
		{
			returnType = "constructor";
		}
		else
			returnType = type.toString();
		
		System.out.println("\n"+sig);
		
		Function fun = new Function(returnType ,sig, owner);
		for(Object obj : parameter)
		{
			SingleVariableDeclaration var =(SingleVariableDeclaration)obj;
			String varName = var.getName().toString();
			String varType = var.getType().toString();
			Member pMember = new Member(varType, varName, fun);
			fun.addHas(pMember);
			datalist.addData(pMember);
			
		}
		
		
		
		StateNode pram = new StateNode("param"+paramStr, "true" , "parameter");
		fun.setHeadNode(pram);
		fun.setCode(method.toString());
		fun.setName(name);
		fun.setComplex();
		stateNode = pram;
		stateNode = new StateNode();
		pram.setThen(stateNode);
		datalist.addData(fun);
		owner.getHasList().addData(fun);
		
		return fun;
	}
	
	//메서드 선언문 makeFunction함수를 사용하여 정보를 생성후 block을 stmtParsing으로 분석
	private static void methodDeclaration(MethodDeclaration method, Data_base owner)
	{		
		Block block = method.getBody();
		Function fun = makeFunction(method, owner);
	
		if(block != null)
		{
			List<Statement> stmtList = block.statements();	

			for(Statement stmt : stmtList)
			{
				stmtParsing(stmt, fun);
			}
		}
	}
	
	//Statement의 구체적인 정보를 instance로 찾아내어 해당 함수로 호출 (함수 한정)
	private static void stmtParsing(Statement stmt, Function fun)
	{
		if(stmt instanceof VariableDeclarationStatement) 
		{
			variableDeclarationStatement(stmt, fun);
		}else if(stmt instanceof ExpressionStatement)
		{
			expressionStatement(stmt, fun);
		}else if(stmt instanceof IfStatement){
			ifStatement(stmt, fun);		
		}else if(stmt instanceof ForStatement){
			forStatement(stmt, fun);
		}else if(stmt instanceof ReturnStatement) {
			returnStatement(stmt, fun);
		}else if(stmt instanceof SwitchStatement) {
			switchStatement(stmt, fun);
		}
	}
	
	//return statment 종결 node이므로 다음 statenode는 없음.
	private static void returnStatement(Statement stmt, Function fun) 
	{
		ReturnStatement node = (ReturnStatement)stmt;
		stateNode.init(node.toString(), "true", "returnStmt");
		
	}

	//forstmt - initStmtList - blockList - update - forEnd 구조로 StateNode를 생성
	private static void forStatement(Statement stmt, Function fun)
	{
		ForStatement node = (ForStatement)stmt;
		ForStateNode forState = new ForStateNode(node);
		stateNode = stateNode.init("forStmt", "true", "forStmt", forState);
		stateNode = stateNode.setThen(new StateNode());
		
		
		//ArrayList<Statement> block = (ArrayList<Statement>) getStmtList( node.getBody() );
		List block = getStmtList(node.getBody());
		Expression condition = node.getExpression();
		List init = node.initializers();
		
		for(int i=0; i<init.size(); i++)
		{
			String str =  init.get(0).toString();
			stateNode.init(str, "true", "for_init");
			stateNode = stateNode.setThen(new StateNode());	
		}
		
		StateNode checkNode = stateNode.init("if", condition.toString() , "forCondition");
		stateNode = checkNode.setThen(new StateNode());//inBlock;
		StateNode forEndNode = checkNode.setElse(new StateNode());
		forState.setCond(checkNode);
		for(int i=0; i < block.size(); i++)
		{
			stmt =(Statement)block.get(i);
			stmtParsing(stmt, fun);		//for {}
		}
		
		List updaters = node.updaters();
		ArrayList<StateNode> forFinList = new ArrayList<StateNode>();
		for(int i=0; i < updaters.size(); i++)
		{
			String str = updaters.get(i).toString();
			stateNode.init(str, "true", "forFin");
			forFinList.add(stateNode);
			stateNode = stateNode.setThen(new StateNode());
			//stmtParsing(stmt, fun);		
		}
		stateNode.getBefore().setThen(checkNode);
		forState.setCondElseNode(forEndNode);
		forState.setForFin(forFinList);
		stateNode = forEndNode;
		//System.out.println(condition.toString());
	}
	
	//getStmtList 함수를 사용하여 thenStmtList, elseStmtList를 얻어 각각 재귀시킨후 stateNode를 연결
	private static void ifStatement(Statement stmt, Function fun) 
	{
		IfStatement node = (IfStatement)stmt;
		StateNode ifExp = stateNode;
		String condition = node.getExpression().toString();
		ifExp.init(condition, condition, "ifStmt");
		StateNode thenStateNode = ifExp.setThen(new StateNode());
		
		
		ArrayList<Statement> thenList = getStmtList(node.getThenStatement());
		
		
		stateNode = thenStateNode;
		for(Statement thenStmt : thenList)
			stmtParsing(thenStmt, fun);
		//StateNode connected = stateNode;
		StateNode connected = stateNode.setThen(new StateNode("Connected","true","connect"));
		
		
		ArrayList<Statement> elseList = getStmtList(node.getElseStatement());
		if(elseList.get(0) != null)
		{
			StateNode elseStateNode = ifExp.setElse(new StateNode());
			stateNode = elseStateNode;
			for(Statement elseStmt : elseList)
				stmtParsing(elseStmt, fun);
			//stateNode = stateNode.init("Connected", "true", "connect", connected);
			
			stateNode = stateNode.setThen(connected);
		}else
			stateNode = ifExp.setElse(connected);
			
	}
	
	//간단히 연산식을 stateNode에 추가하여 연결
	private static void expressionStatement(Statement stmt, Function fun) 
	{
		ExpressionStatement expStmt = (ExpressionStatement)stmt;
		Expression exp = expStmt.getExpression();
		stateNode.init(exp.toString(), "true", "expression");
		stateNode = stateNode.setThen(new StateNode());
		if(exp instanceof Assignment)
		{
			assignment(exp, fun);
		}

	}
	
	//expressionStatements에서 호출됨 대입식의 경우 member정보 업데이트
	private static void assignment(Expression exp, Function fun) 
	{

		Assignment node = (Assignment)exp;
		Data_base owner = fun;
		String name = node.getLeftHandSide().toString();
		Member member = owner.getHasList().findMember( owner.getSrcName()+"."+name );
	
		while( member == null && owner instanceof Function)
		{
			owner =((Function) owner).getOwner();
			member = owner.getHasList().findMember(owner.getSrcName()+"."+name);
		}
		String img = node.getRightHandSide().toString();
		if(member != null)
			member.addImage(img);
		
	}

	//변수의 선언식을 연결하여 노드에 추가. 지역변수 선언이므로 fun의 hasList에 추가
	private static void variableDeclarationStatement(Statement stmt, Function fun)
	{
		DataList hasList = fun.getHasList();
		VariableDeclarationStatement node = (VariableDeclarationStatement)stmt;
		List<VariableDeclarationFragment> fragList = node.fragments();
		Type type = node.getType();
		VarDecNode varNode = new VarDecNode();
		stateNode = stateNode.init(node.toString(), "true", "varDef_" + fragList.get(0).getName().toString() , varNode);
		stateNode=stateNode.setThen(new StateNode());
	//	varStmt.
	//	System.out.println("varDec "+ node.toString());
		for( VariableDeclarationFragment frag : fragList)
		{
			String typeName = type.toString();
			String valName = frag.getName().toString();
			
			Member member = new Member(typeName, valName, fun);
			hasList.addData(member);
			varNode.setType(typeName);
			Expression init = frag.getInitializer();
			if(init != null)
			{
				member.addImage(init.toString());
			}
			fun.addHas(member);
			datalist.addData(member);
		}
	}

	//스위치의 경우 switchStatement로 변경하여 연결
	private static void switchStatement(Statement stmt, Function fun)
	{
		//System.out.println(stmt.toString());
		SwitchStatement node = (SwitchStatement)stmt;
		SwitchNode swNode = new SwitchNode(node);
		stateNode = stateNode.init(stmt.toString(), "true", "swtich_"+node.getExpression().toString(), swNode);
		stateNode = stateNode.setThen(new StateNode());
		//swNode.print();
	}
}
