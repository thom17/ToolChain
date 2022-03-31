package complexity;

import java.util.Scanner;
import java.util.Stack;

import com.github.javaparser.ast.stmt.BlockStmt;

import dataSet.statement.StateNode;
import dataSet.statement.StateNodeList;

public class StateNodeComplex 
{
	public int lineNum;
	public int nodeNum;
	public int callSize;
	public int bifurSize;
	public int writeSize;
	public int repeatSize;
	
	public StateNodeComplex(BlockStmt blockStmt, StateNodeList nodeList)
	{
		if(blockStmt != null)
		{
			setLineNum(blockStmt.toString());
			setNodeNum(nodeList);
			setSize(nodeList);
			
		}else
		{
			lineNum = nodeNum =0;
		}
	}
	
	private void setSize(StateNodeList nodeList) {
		// TODO Auto-generated method stub
		this.bifurSize = nodeList.getBifurcationList().size();
		this.callSize = nodeList.getCallList().size();
		this.repeatSize = nodeList.getRepeatList().size();
		this.writeSize = nodeList.getWriteList().size();
		
	}

	void setNodeNum(StateNodeList nodeList)
	{
		Stack<StateNode> checkStack = new Stack<StateNode>();
		StateNode stateNode = nodeList.getHead();
		if(stateNode == null) 
			return;
		nodeNum = -1;
		countNodeNum(stateNode.getThen(), checkStack);
		countNodeNum(stateNode.getElse(), checkStack);
		//nodeNum--;
	
	}
	private void countNodeNum(StateNode node, Stack<StateNode> checkStack)
	{
		if(node != null && !checkStack.contains(node))
		{
			nodeNum++;
			checkStack.add(node);
			countNodeNum(node.getThen(), checkStack);
			countNodeNum(node.getElse(), checkStack);
		}
		else return;
		
	}
	/*
	private void setCyclomatic(StateNodeList nodeList)
	{
		int node = 0;
		int edge = 0;
		Stack<StateNode> nodeStack = new Stack<StateNode>();
		StateNode head = nodeList.getHead();
		cyclomatic = cyclomatic(node, edge, head, nodeStack);
	}
	private int cyclomatic(int node, int edge, StateNode head, Stack<StateNode> nodeStack) 
	{
		if(!nodeStack.contains(head))
		{
			node++;
			nodeStack.add(head);
			StateNode thenNode = head.getThen();
			if(thenNode != null)
			{
				edge++;
				return cyclomatic(node, edge, thenNode, nodeStack);
			}
		}	
		return 0;
	}
	 */
	void setLineNum(String code)
	{
		lineNum = 0;
		Scanner scan = new Scanner(code);
		while(scan.hasNext())
		{
			lineNum++;
			scan.nextLine();
		}
		lineNum -=2;
	}
}
