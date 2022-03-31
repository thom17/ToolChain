package dataSet.statement;

import java.util.ArrayList;

import dataSet.Member;

public class StateTree {
	State root;
	ArrayList<Member> writeList = null;
	ArrayList<Member> readList = null;
	ArrayList<Member> localList = null;

	public StateTree(State root)
	{
		this.root = root;
	}
	
}
