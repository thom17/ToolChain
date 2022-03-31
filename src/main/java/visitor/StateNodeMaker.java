package visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.Position;
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
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import dataSet.DataList;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dataSet.statement.ForStateNode;
import dataSet.statement.MethodCallStateNode;
import dataSet.statement.StateNode;
import dataSet.statement.StateNodeList;
import dataSet.statement.SwitchNode;
import dataSet.Class;
import dev.khe.DiagramRecover.TypeUtil;
import dev.khe.DiagramRecover.model.MOperationCall;

public class StateNodeMaker extends VoidVisitorAdapter<StringBuilder>
{
	Function function;
	DataList datalist;
	StateNodeList nodeList;
	StateNode stateNode;
	StateNode head;
	StateNode fin = new StateNode();
	String condition = "true";
	
	public StateNodeMaker(Function fn, DataList datalist)
	{
		this.datalist = datalist;
		init(fn);
		//System.out.println("start Parsing ---" + fn.getSrcName());
		//System.out.println(fn.getNode().toString());
		//System.out.println("End Parsing ================");
	}
	public void init(Function fn)
	{
		function = fn;
		stateNode = new StateNode(fn.getParameterName(), condition, fn.getName()+" parameter");
		head = stateNode;
		nodeList = new StateNodeList(head, fn.getBlockStmt());
		fn.setNodeList(nodeList);
		stateNode = head.setThen(new StateNode());
		this.condition = "true";
		if(fn.getBlockStmt() != null)
		{
			BlockStmt block = fn.getBlockStmt(); 
			for( Statement stmt : block.getStatements())
				stmtParsing(stmt);
		}
	}
	private void stmtParsing(Statement stmt) 
	{
		System.out.println(stmt.toString()+" start");
		if(stmt instanceof LocalClassDeclarationStmt) {
			localClassDecStatement(stmt);
		}else if(stmt instanceof IfStmt){
			ifStatement(stmt);
		}else if(stmt instanceof ForStmt){
			forStatement(stmt);
		}else if(stmt instanceof ReturnStmt) {
			returnStatement(stmt);
		}else if(stmt instanceof SwitchStmt) {
			switchStatement(stmt);
		}else if(stmt instanceof ExpressionStmt){
			expressionStatement(stmt.asExpressionStmt().getExpression());
		}else if(stmt instanceof WhileStmt) {
			WhileStatement(stmt);
		}else if(stmt instanceof BlockStmt) {
			blockStatement(stmt);
		}
		System.out.println("=========== end");
	}
	private void WhileStatement(Statement stmt) {
		// TODO Auto-generated method stub

		WhileStmt node = stmt.asWhileStmt();
		ForStateNode forState = new ForStateNode(node);
		
		
		stateNode = stateNode.init("WhileStmt", "true", "forStmt", forState);
		stateNode = stateNode.setThen(new StateNode());
		
		
		//ArrayList<Statement> block = (ArrayList<Statement>) getStmtList( node.getBody() );
		List block = getStmtList(node.getBody());
		String condition = node.getCondition().toString();

		StateNode checkNode = stateNode.init("if", condition, "forCondition");
		stateNode = checkNode.setThen(new StateNode());//inBlock;
		StateNode forEndNode = checkNode.setElse(new StateNode());
		forState.setCond(checkNode);
		for(int i=0; i < block.size(); i++)
		{
			stmt =(Statement)block.get(i);
			stmtParsing(stmt);		//for {}
		}

		stateNode.getBefore().setThen(checkNode);
		forState.setCondElseNode(forEndNode);
		stateNode = forEndNode;
		//System.out.println(condition.toString());
	}
	private void blockStatement(Statement stmt) {
		// TODO Auto-generated method stub
		for(Statement n : stmt.asBlockStmt().getStatements())
		{
			stmtParsing(n);
		}
	}
	private void localClassDecStatement(Statement stmt) {
		// TODO Auto-generated method stub
		System.out.println("localClassDecStmt : "+stmt.toString());
	}
	private void switchStatement(Statement stmt) {
		// TODO Auto-generated method stub
		System.out.println("switchStmt : "+stmt.toString());
		//System.out.println(stmt.toString());
		SwitchStmt node = (SwitchStmt)stmt;
		SwitchNode swNode = new SwitchNode(node);
		stateNode = stateNode.init(stmt.toString(), "true", "swtich_"+node.getSelector().toString(), swNode);
		stateNode = stateNode.setThen(new StateNode());
		//swNode.print();
	}
	private void returnStatement(Statement stmt) {
		// TODO Auto-generated method stub
		System.out.println("returnStmt : "+stmt.toString());
		ReturnStmt n = stmt.asReturnStmt();		
		String result="";
		if(n.getExpression().isPresent())
		{
			result = n.getExpression().get().toString();
			StateNode insideNode = new StateNode("return", "true");
			stateNode.setInsideNode(new StateNodeList(insideNode));
			StateNode returnNode = stateNode;
			stateNode = insideNode.setThen(new StateNode());
			expressionStatement(n.getExpression().get());
			stateNode = returnNode;
		}
		stateNode.init(n.toString(), result+"_return");
		stateNode = stateNode.setThen(fin);
	}
	private void forStatement(Statement stmt) {
		// TODO Auto-generated method stub
		ForStmt node = (ForStmt)stmt;
		ForStateNode forState = new ForStateNode(node);
		nodeList.addRepeatList(forState);
		stateNode = stateNode.init("forStmt", "true", "forStmt", forState);
		stateNode = stateNode.setThen(new StateNode());
		
		
		//ArrayList<Statement> block = (ArrayList<Statement>) getStmtList( node.getBody() );
		List block = getStmtList(node.getBody());
		String condition = "true";
		if(node.getCompare().isPresent())
			condition = node.getCompare().get().toString();
		List init = node.getInitialization();
		
		for(int i=0; i<init.size(); i++)
		{
			String str =  init.get(0).toString();
			stateNode.init(str, "true", "for_init");
			stateNode = stateNode.setThen(new StateNode());	
		}
		
		StateNode checkNode = stateNode.init("if", condition, "forCondition");
		stateNode = checkNode.setThen(new StateNode());//inBlock;
		StateNode forEndNode = checkNode.setElse(new StateNode());
		forState.setCond(checkNode);
		for(int i=0; i < block.size(); i++)
		{
			stmt =(Statement)block.get(i);
			stmtParsing(stmt);		//for {}
		}
		
		List updaters = node.getUpdate();
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
	private void ifStatement(Statement stmt) {
		// TODO Auto-generated method stub
		System.out.println("ifStmt : "+stmt.toString());
		IfStmt node = (IfStmt)stmt;
		StateNode ifExp = stateNode;
		nodeList.addBifurcationList(ifExp);
		
		String condition = node.getCondition().toString();
		Expression exp = node.getCondition();
	
		condition = exp.removeComment().toString();
		
		ifExp.init(condition, condition, "ifStmt");
		StateNode thenStateNode = ifExp.setThen(new StateNode());
		StateNode elseStateNode = ifExp.setElse(new StateNode());

		Statement thenStmt = node.getThenStmt(); 
	
		stateNode = thenStateNode;
		stmtParsing(thenStmt);
		//StateNode connected = stateNode;
		StateNode connected = stateNode.setThen(new StateNode("Connected","true","connect"));
		
		stateNode = elseStateNode;
		if( node.getElseStmt().isPresent())	
			stmtParsing(node.getElseStmt().get());
		stateNode = stateNode.setThen(connected);
	}
	private void expressionStatement(Expression exp) {
		// TODO Auto-generated method stub
		if(exp instanceof VariableDeclarationExpr)
			variableDeclarationExpr(exp.asVariableDeclarationExpr());
		else if(exp instanceof AssignExpr)
			assignExpr(exp.asAssignExpr());
		else if(exp instanceof MethodCallExpr) {
			methodCallExpr(exp.asMethodCallExpr());
		}else if(exp instanceof MethodReferenceExpr){
			methodReferExpr(exp.asMethodReferenceExpr());
		}else if(exp instanceof ObjectCreationExpr) {
			objectCreationExpr(exp.asObjectCreationExpr());
		}else if(exp instanceof ThisExpr)
		{
			
		}
		
		
	}
	private void objectCreationExpr(ObjectCreationExpr n) {
		// TODO Auto-generated method stub
		ResolvedReferenceType resolveCls = n.getType().resolve();
		String desc = resolveCls.describe();
		Class makeClass = datalist.findClass(desc);
		if(makeClass == null)
		{
			makeClass = new Class(desc);
			datalist.addData(makeClass);
		}
		function.addCall(makeClass);
		ResolvedConstructorDeclaration resolveFun = n.resolve();
		String srcSignature = resolveFun.getQualifiedSignature();
		Function fun = datalist.findFunction(srcSignature);
		if(fun == null)
		{			
			fun = new Function(srcSignature, makeClass);
			fun.setConstructor(true);
			datalist.addData(fun);
			makeClass.addHas(fun);				
			//callClass.addHasFunction(callFun);
		}
		
		function.addCall(fun);
		function.addCall(makeClass);
	
		stateNode.init(n.toString(), "true", "new_"+desc);
		stateNode = stateNode.setThen(stateNode);
	}
	private void methodReferExpr(MethodReferenceExpr n) {
		// TODO Auto-generated method stub
		System.out.println("MethodReferenceExpr : "+n.toString());
	}
	private void methodCallExpr(MethodCallExpr n) {
		// TODO Auto-generated method stub
		MethodCallStateNode methodCallNode = new MethodCallStateNode();
		System.out.println("MethodCallExpr : "+n.toString());
		stateNode = stateNode.init(n.toString(), function.getName()+"_call", "true", methodCallNode);
		nodeList.addCallList(stateNode);

		Member caller = null;
	
		ResolvedMethodDeclaration resolve = n.resolve();		

		if(n.getScope().isPresent())	// hostName.methodName(); 의 hostName에 대한 정보
		{
			Expression exp = n.getScope().get();
			String hostName = exp.toString();
			caller = getUseLocal(hostName); //값이 있다면 locaValue
			if(caller == null)	//없다면 필드값 접근
			{
				caller = findOwnerMember(hostName);	
				methodCallNode.setCaller(caller);
			}
			
		}
		
		String sig = resolve.getSignature();
		String srcName = resolve.getQualifiedSignature();	
		ResolvedReferenceTypeDeclaration type =resolve.declaringType();
		if( type != null && type.isClass() )
		{
			ResolvedClassDeclaration clientClassInfo = resolve.declaringType().asClass();
			String srcClassName = clientClassInfo.getQualifiedName();
			String className = clientClassInfo.getName();
			String packageSrc = clientClassInfo.getPackageName();
			Function clientFun = datalist.findFunction(srcName);
			Class clientClass = datalist.findClass(srcClassName);
			
			if(clientClass == null)
			{
				clientClass = new Class(packageSrc , className);
				datalist.addData(clientClass);
			}
			
			if(clientFun == null)
			{			
				clientFun = new Function(sig, clientClass);
				datalist.addData(clientFun);
				clientClass.addHas(clientFun);				
				//callClass.addHasFunction(callFun);
			}
			
			function.addCall(clientFun);
			function.addCall(clientClass);
			methodCallNode.setFuns(clientFun, function);
		
			if(caller != null)
			{
				caller.addCall(clientFun);
				clientFun.addCallBy(caller);
				function.addCall(caller);
			}
		}else
		{
			function.addCall(caller);
		}
		stateNode = stateNode.setThen(new StateNode() );
		
	}
	private void assignExpr(AssignExpr n) {
		////System.out.println("Write /"+n.toString());
		Expression target = n.getTarget();
		String targetName = n.getTarget().toString();
		String valueImg = n.getValue().toString();
		
		stateNode.init(n.toString(), targetName+"_change","true");
		nodeList.addWriteList(stateNode);

		stateNode = stateNode.setThen(new StateNode() );
		
		if( target.isArrayAccessExpr() )
		{
			ArrayAccessExpr arrayExp = target.toArrayAccessExpr().get();
			targetName = arrayExp.getName().toString();
			
		}else if(target.isFieldAccessExpr() )
		{
			System.out.println("field Access : "+n.toString());
			ResolvedValueDeclaration targetResolve = target.asFieldAccessExpr().resolve();
			targetName = targetResolve.getName();
			addMemberInfo(targetName, valueImg);
			
		}
		else	//this나 Array가 아니라면
		{
			Member localVal = getUseLocal(targetName);	//먼저 지역변수인지 확인해본다.
			if(localVal != null)	//로컬변수인 경우임.
			{
				localVal.addImage(valueImg);
			}
			else	//그 외는 this를 사용하지 않은 맴버값 접근임.
			{
				addMemberInfo(targetName, valueImg);
			}
			
		}
		StateNode nextNode = stateNode;
		StateNode detailHead = new StateNode(targetName, "true", "=");
		StateNodeList inside = new StateNodeList(detailHead);
		stateNode.setInsideNode(inside);
		stateNode = detailHead.setThen(new StateNode());
		expressionStatement(n.getValue());
		stateNode = nextNode;
	}
	private void variableDeclarationExpr(VariableDeclarationExpr n) {
		// TODO Auto-generated method stub
		StateNode varDefNode = stateNode.init(n.toString(), "true", "varDef");
		stateNode = stateNode.setThen(new StateNode() );
		
		NodeList<VariableDeclarator> varList =n.getVariables();

		for(VariableDeclarator var : varList)
		{
			ResolvedValueDeclaration resolve = var.resolve();
			String type = resolve.getType().describe();
			String name = var.getName().toString();
			////System.out.println(n.toString()+" : "+name);
			Member member = new Member(type , name, function);
			function.addHas(member);
			datalist.addData(member);
		
			String elementTypeName  = type.replace("[]", "");
			member.setTypeDef(datalist.findClass(elementTypeName));
			
			Optional<Expression> exp = var.getInitializer();
			
			if(exp.isPresent())
			{
				member.addImage(exp.get().toString());
				StateNode nextNode = stateNode;
				StateNode detailHead = new StateNode(name, "true", "=");
				StateNodeList inside = new StateNodeList(detailHead);
				varDefNode.setInsideNode(inside);
				stateNode = detailHead.setThen(new StateNode());
				expressionStatement(exp.get());
				stateNode = nextNode;
				
			}
		}
		
	}
	private Member addMemberInfo(String name, String valueImg)
	{
		Data_base owner = function.getOwner();
		String classSrcName = owner.getSrcName();
		Member member = this.datalist.findMember(classSrcName+"."+name);
	
		if(member == null) 	//상속 받은 맴버 임. 상속 클래스들 탐색해야함. 일단 임시방편으로 작업
		{
			if( owner instanceof Class )
				member = findSuperMember(name, (Class)owner);	//한계가 있음.
		}
		else
			member.addImage(valueImg);
		
		this.function.addCall(member);
	
//		addNode(n.toString(), name+"_change");	//아무튼 맴버 값 변경함.
		return member;
	}
	private Member findSuperMember(String name, Class target)
	{
		Member member = null;
		if(target.getSuperClass() != null) 
		{
			for(Class superClass : target.getSuperClass() )
			{
				if(superClass != null)
					member = superClass.getHasList().findMember(superClass.getSrcName()+"."+name);
				if(member != null) break;
			}
		}
		return member;
	}
	private Member getUseLocal(String id)
	{
		return function.getHasList().findMember(function.getSrcName()+"."+id);
	}
	private Member findOwnerMember(String varName)
	{
		Data_base owner = function.getOwner();
		String classSrcName = owner.getSrcName();
		Member member = this.datalist.findMember(classSrcName+"."+varName);
	
		if(member == null) 	//상속 받은 맴버 임. 상속 클래스들 탐색해야함. 일단 임시방편으로 작업
		{
			if( owner instanceof Class )
				member = findSuperMember(varName, (Class)owner);	//한계가 있음.
		}
		return member;
	}
	public static ArrayList<Statement> getStmtList( Statement block )
	{
		List<?> ast;
		ArrayList<Statement> list = new ArrayList<Statement>();
		//ArrayList<Statement> list;
		if(block instanceof BlockStmt)
		{
			BlockStmt body =  (BlockStmt)block;
			 ast = body.getStatements();
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
}
