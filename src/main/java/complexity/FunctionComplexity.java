package complexity;

import java.util.Arrays;
import java.util.Stack;

import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dataSet.statement.StateNode;
import dataSet.statement.StateNodeList;
import dataSet.Class;
import dataSet.DataList;

public class FunctionComplexity extends Complex_Base
{
	int parameterScore = 0;

	int classCallScore = 0;
	int callScore = 0;
	int callByScore = 0;
	
	int ownerCallFunNum = 0;
	int memberUseScore = 0;
	int localValueScore = 0;
	
	int expScore = 0;
	
	int repeatScore = 0;
	int brenchScore = 0;
	
	int callCountNum = 0;
	int memberCountNum = 0;
	int cyclomatic = 1;
	
	HalsteadComplex halstedComplex;
	StateNodeComplex stateNodeComplex;
	
	
	//simple
	public FunctionComplexity(Function fun)
	{		
		simpleMake(fun);
	}
	
	public void simpleMake(Function fun)
	{
		stateNodeComplex = new StateNodeComplex(fun.getBlockStmt(), fun.getStateNodeList());	
		halstedComplex = new HalsteadComplex(fun);
		
		parameterScoreSet(fun.getParameterTypeList());
		
		callScoreSet(fun);
		
		brenchScoreSet(fun.getHeadNode());
		
		callByScoreSet(fun);
		
		int brBonus = 1;
		for(int i=1; i<brenchScore; i++)
		{
			brBonus *= 2;
		}
		brBonus = brBonus * expScore;
		absComplex = callScore + brBonus + parameterScore;
		int m = repeatScore+ownerCallFunNum;
		if(m == 0) m=1;
		this.relativeComplex = absComplex/m;
		//printScore();
	}
	public int getAbsComplex() {return absComplex;}
	private void printScore() 
	{
		System.out.println("Total : "+absComplex);
		System.out.println("파라미터 점수" + " : " + parameterScore);
		
		System.out.println("콜 관계 점수"+ " : "+callScore+"\n");
		System.out.println("\t클래스"+ " : "+classCallScore);
		System.out.println("\t맴버"+ " : "+memberUseScore);
		System.out.println("\t로컬"+ " : "+localValueScore);
		
		System.out.println("분기점"+ " : "+brenchScore);
		System.out.println("식"+ " : "+expScore);
		
		System.out.println("Line Num : "+stateNodeComplex.lineNum);
		System.out.println("Node Num : "+stateNodeComplex.nodeNum);
		
	}

	private void brenchScoreSet(StateNode headNode) 
	{
		Stack<StateNode> visitNode = new Stack<StateNode>();

		StateNode pos = headNode;
		if(pos!= null)
			dfs(pos, visitNode);
		cyclomatic=brenchScore+1;
		
	}

	private void dfs(StateNode pos, Stack<StateNode> visitNode) {
		
		this.expScore +=1;
		visitNode.add(pos);
		pos = pos.getThen();
		if(pos == null)
			return;
		else if(visitNode.contains(pos))
		{
			repeatScore++;
		}
		else
		{
			dfs(pos, visitNode);
			
			pos = pos.getElse();
			if(pos !=null && !visitNode.contains(pos))
			{
				this.brenchScore +=1;
				dfs(pos, visitNode);
			}
		}
	}

	private void callScoreSet(Function fun) 
	{
		while( !(fun.getOwner() instanceof Class) ) fun = (Function)fun.getOwner();
		
		Class owner = (Class) fun.getOwner();
		DataList callList = fun.getCallList();
		for(Class target : callList.getClassList() )
		{
			callCountNum++;
			if(target == owner) classCallScore += 2;
			else classCallScore = +3;
		}
		
		for(Function target : callList.getFunctionList() )
		{
			callCountNum++;
			if(target == fun) callScore += 3;	//재귀
			else 
				callScore = +2 ;
			if(target.getOwner() == owner)
				this.ownerCallFunNum++;
		}
		
		for(Member target : callList.getMemberList() )
		{
			callCountNum++;
			if(target.getOwner() == owner) {
				memberCountNum++;
				memberUseScore += 2;
			}else
				memberUseScore += 1;
			
		}
		
		DataList hasList = fun.getHasList();
		for(Member target : hasList.getMemberList() )
		{
			localValueScore += 1;
		}
		
		callScore += memberUseScore+classCallScore;
	}

	private void parameterScoreSet(String parameterTypeList[])
	{
		if(parameterTypeList != null)
		{
			for(String type : parameterTypeList)
			{
				parameterScore += TypeLevel.getTypeScore(type);
			}
		}
	}

	private void callByScoreSet(Function fun)
	{
		DataList callbyList = fun.getCallByList();
		while( !(fun.getOwner() instanceof Class) ) fun = (Function)fun.getOwner();
		Class owner = (Class) fun.getOwner();

		for(Class target : callbyList.getClassList() )
		{
			if(target == owner) continue;
			else callByScore+=3;
		}
		
		for(Function target : callbyList.getFunctionList() )
		{
			if(target == fun) callByScore += 3;	//재귀
			else callByScore = +2 ;
			
		}
		
	}
	
	public int getParameterScore() {
		// TODO Auto-generated method stub
		return this.parameterScore;
	}

	public int getClassCallScore() {
		// TODO Auto-generated method stub
		return this.classCallScore;
	}

	public int getCallScore() {
		// TODO Auto-generated method stub
		return this.callScore;
	}

	public int getMemberUseScore() {
		// TODO Auto-generated method stub
		return this.memberUseScore;
	}

	public int getLocalValueScore() {
		// TODO Auto-generated method stub
		return this.localValueScore;
	}

	public int getExpScore() {
		// TODO Auto-generated method stub
		return this.expScore;
	}

	public int getRepeatScore() {
		// TODO Auto-generated method stub
		return this.repeatScore;
	}

	public int getBrenchScore() {
		// TODO Auto-generated method stub
		return this.brenchScore;
	}

	public int getCallCountNum() {
		// TODO Auto-generated method stub
		return this.callCountNum;
	}

	public StateNodeComplex getStatNodeComplex() {
		// TODO Auto-generated method stub
		return this.stateNodeComplex;
	}

	public int getCyclomatic() {
		// TODO Auto-generated method stub
		return this.cyclomatic;
	}

	public int getCallByScore() {
		// TODO Auto-generated method stub
		return this.callByScore;
	}
	public HalsteadComplex getHalstead()
	{
		return this.halstedComplex;
	}
}
