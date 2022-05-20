package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import dataSet.Class;
import dataSet.Function;
import dataSet.Member;
import dataSet.statement.StateNode;

public class TestVisitor2 extends VoidVisitorAdapter<StringBuilder>
{
	public void visit(ClassOrInterfaceDeclaration n, StringBuilder arg)
	{
		System.out.println(arg+"Class :\t" + n.getName().asString() +"\n" );
		super.visit(n, arg);
	}
	public void visit(MethodDeclaration n, StringBuilder arg)
	{
		System.out.println(arg+"Function :\t" + n.getSignature() +"\n" );
		super.visit(n, arg);
	}
	
	
	public void visit(VariableDeclarationExpr n, StringBuilder arg)
	{
		System.out.println(arg + "VariableDeclarationExpr :\n\t" + n.toString() );
		super.visit(n, arg);
	}
	public void visit(FieldAccessExpr n, StringBuilder arg)
	{
		System.out.println(arg+"FieldAccessExpr :\n\t" + n.toString() );
		super.visit(n, arg);
	}
	public void visit(AssignExpr n, StringBuilder arg)
	{
		System.out.println(arg+"AssignExpr :\n\t" + n.toString() );
		super.visit(n, arg);
	}
	public void visit(ReturnStmt n, StringBuilder arg)
	{
		System.out.println(arg+"ReturnStmt :\n\t" + n.toString() );
		super.visit(n, arg);
	}
	public void visit(MethodCallExpr n, StringBuilder arg) 
	{
		System.out.println(arg+"MethodCallExpr :\n\t" + n.toString() );
		super.visit(n, arg);
	}
	public void visit(IfStmt n, StringBuilder arg)
	{	
		System.out.println(arg+"Ifstmt :\tif " + n.getCondition().toString() +"\n" );
		arg.append(n.getCondition().toString()+ " - ");
		TestVisitor2 thenVisitor = new TestVisitor2();
		TestVisitor2 elseVisitor = new TestVisitor2();
		
		Statement thenStmt = n.getThenStmt();	
		thenStmt.accept(thenVisitor, arg);
		
		Optional<Statement> elseStmt = n.getElseStmt();
		
		if( elseStmt.isPresent() )
		{
			arg.insert(0, "!");
			elseStmt.get().accept(elseVisitor, arg);
		}
		arg.delete(0, arg.length());
	}

}
