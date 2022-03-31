package dataSet.stateNode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;

import main.Test_Visitor;

public class ForStateNode extends StateNode
{
	List init; 
	ArrayList<Statement> bodyList;
	String repeatCondition;
	StateNode cond;
	StateNode condElseNode;
	ArrayList<StateNode> forFinList;
	
	public ForStateNode(ForStatement node)
	{
		
		this.init = node.initializers();
		this.repeatCondition = node.getExpression().toString();
		this.bodyList = Test_Visitor.getStmtList(node.getBody());
	}
	
	public void setCond(StateNode cond) { this.cond = cond; }
	public void setForFin(ArrayList<StateNode> forFinList) {this.forFinList = forFinList;}
	public void setCondElseNode(StateNode elseNode) { this.condElseNode = elseNode; }
	
	public String getForLine()
	{
		StringBuilder sb = new StringBuilder("for(");
		for(Object obj : init)
		{
			sb.append(obj.toString()+" ");
		}
		sb.append("; "+cond.getCondition()+"; ");
		for(StateNode node : forFinList)
		{
			sb.append(node.getCode()+" ");
		}
		sb.append(" )");
		return sb.toString();
	}
	public StateNode getCondNode() {return this.cond;}
	public ArrayList<StateNode> getForFinList(){return this.forFinList;}
	
	public List getInitList( ) { return this.init; } 
	public StateNode getCond() {return this.cond;}
	
	public StateNode getLastFin() 
	{ 
		int size = forFinList.size();
		if( 0 < size)
			return forFinList.get(size-1);
		StateNode last = cond.getThen();
		while(last.getThen() != cond) {
			System.out.println(last.code);
			last = last.getThen();
		}
		return last;
	}
	
	
}
