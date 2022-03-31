package dataSet.statement;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;

public class ExpState extends StateNode{

	private Expression exp;
	private String str = " = ";
	public ExpState(String code, String nextCheck) 
	{
		super(code, nextCheck);
		// TODO Auto-generated constructor stub
	}
	public ExpState(Expression exp , String code)
	{
		super(code, "true");
		this.exp = exp;
		this.key = "exp";
	}
	
	public String getStr() 
	{
		return str; 
	}
	/*
	 * 오버라이딩 되버려서 아래 if문 안들어가짐.
	public String getKey()
	{
		return "overriding";
	}
	*/


}
