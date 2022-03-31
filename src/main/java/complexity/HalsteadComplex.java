package complexity;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import dataSet.Function;
import main.Parser;

public class HalsteadComplex extends VoidVisitorAdapter<StringBuilder>
{	
	public ArrayList<String> operatorList = new ArrayList<String>();
	public ArrayList<String> operandList = new ArrayList<String>();
	public int operator = 0;
	public int operand =0;
	public int progVoc;
	public int progL;
	public double cepl;
	public double volume;
	public double difficulty;
	public double effort;
	public HalsteadComplex(Function f) 
	{
		MethodDeclaration fun =f.getNode();
		StringBuilder sb=new StringBuilder();
		if(fun != null)
			fun.accept(this, sb);
		else if(f.getBlockStmt() != null)
			f.getBlockStmt().accept(this, sb);
		progVoc = operatorList.size()+operandList.size();
		progL = operator+operand;
		double n1 = operatorList.size();
		double n2 = operandList.size();
		cepl = n1*baseLog(n1, 2.0)+n2*baseLog(n2, 2.0);
		volume = progL*baseLog(progVoc, 2.0);
		difficulty = (n1*operand)/(2*n2);
		effort = difficulty*volume;
		// TODO Auto-generated constructor stub
	}
	private double baseLog(double x, double base)
	{
		return Math.log10(x)/Math.log10(base);
	}
	
	public void visit(MethodDeclaration n, StringBuilder sb)
	{
		sb.append(n.toString()+", ");
		String name = n.getName().toString();
		if(!operatorList.contains(name))
			operatorList.add(name);
		operator++;
		for(Parameter p : n.getParameters())
			p.accept(this, sb);
		if(n.getBody().isPresent())
			n.getBody().get().accept(this, sb);
	}
	public void visit(BlockStmt n, StringBuilder sb)
	{
		String line = n.toString();
		if( line.contains("{") )
		{
			if(!operatorList.contains("{}"))
				operatorList.add("{}");
			operator+=count(line , '{');
		}
		super.visit(n, sb);
	}
	public void visit(SimpleName n, StringBuilder sb) 
	{
		sb.append(n.toString()+", ");
		if(!operandList.contains(n.toString()))
			operandList.add(n.toString());
		operand++;
		//super.visit(n, sb);
	}
	public void visit(ExpressionStmt n, StringBuilder sb)
	{
		sb.append(n.toString()+", ");
		String line = n.toString();
		if( line.contains(";") )
		{
			if(!operatorList.contains(";"))
				operatorList.add(";");
			operator+=count(line , ';');
		}
		if(line.contains(","))
		{
			if(!operatorList.contains(","))
				operatorList.add(",");
			operator+=count(line , ',');
		}
		if(line.contains("("))
		{
			if(!operatorList.contains("()"))
				operatorList.add("()");
			operator+=count(line , '(');
		}
		super.visit(n, sb);
	}
	private int count(String line, char target) 
	{
		
		int count = 0;
		for(int i=0; i<line.length(); i++)
			if(line.charAt(i) == target)
				count++;
			else
				continue;
		return count;
	}

	public void visit(MethodCallExpr n, StringBuilder sb)
	{
		sb.append(n.toString()+", ");
		String name = n.getName().toString();
		if(!operatorList.contains(name))
			operatorList.add(name);
		operator++;
		
		for(Expression exp : n.getArguments())
			exp.accept(this, sb);
		
	}
	public void visit(AssignExpr n, StringBuilder sb)
	{
		sb.append(n.toString()+", ");
		Operator op = n.getOperator();
		String as =op.asString();
		if(!operatorList.contains(as))
			operatorList.add(as);
		operator++;
		super.visit(n, sb);
	}
	public void visit(BinaryExpr n, StringBuilder sb)
	{
		sb.append(n.toString()+", ");
		String op =n.getOperator().asString();
		if(!operatorList.contains(op))
			operatorList.add(op);
		operator++;
		super.visit(n, sb);
	}

	public void visit(PrimitiveType n, StringBuilder sb)
	{
		sb.append(n.toString()+", ");
		if(!operatorList.contains(n.toString()))
			operatorList.add(n.toString());
		operator++;
		
	}
	public void visit(StringLiteralExpr n, StringBuilder sb)
	{
		sb.append(n.toString()+", ");
		if(!operandList.contains(n.toString()))
			operandList.add(n.toString());
		operand++;
	}
	public void visit(IntegerLiteralExpr n, StringBuilder sb)
	{
		sb.append(n.toString()+", ");
		if(!operandList.contains(n.toString()))
			operandList.add(n.toString());
		operand++;
	}
}
