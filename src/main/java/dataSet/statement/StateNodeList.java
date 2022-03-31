package dataSet.statement;

import java.util.ArrayList;
import java.util.Vector;

import com.github.javaparser.ast.stmt.BlockStmt;

public class StateNodeList {
	BlockStmt block;
	StateNode headNode;
	ArrayList<StateNode> bifurcationList = new ArrayList<StateNode>();
	ArrayList<StateNode> callList = new ArrayList<StateNode>();;
	ArrayList<ForStateNode> repeatList = new ArrayList<ForStateNode>();;
	ArrayList<StateNode> writeList = new ArrayList<StateNode>();;
	public StateNodeList(StateNode head, BlockStmt blockStmt) {
		this.headNode = head;
		this.block = blockStmt;
	}
	public StateNodeList(StateNode insideNode) {
		headNode = insideNode;
	}
	public StateNode getHeadNode() {
		return headNode;
	}
	public ArrayList<StateNode> getBifurcationList() {
		return bifurcationList;
	}

	public ArrayList<StateNode> getCallList() {
		return callList;
	}

	public ArrayList<ForStateNode> getRepeatList() {
		return repeatList;
	}
	public ArrayList<StateNode> getWriteList() {
		return writeList;
	}


	
	
	public StateNode getHead() {
		// TODO Auto-generated method stub
		
		return null;
	}
	public void addRepeatList(ForStateNode forState) {
		this.repeatList.add(forState);
	}
	public void addBifurcationList(StateNode ifExp) {
		// TODO Auto-generated method stub
		this.bifurcationList.add(ifExp);
	}
	public void addCallList(StateNode stateNode) {
		// TODO Auto-generated method stub
		this.callList.add(stateNode);
	}
	public void addWriteList(StateNode stateNode) {
		// TODO Auto-generated method stub
		this.writeList.add(stateNode);
	}

}
