package complexity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dataSet.Class;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;

public class ClassComplexity extends Complex_Base
{	
	int memberScore;
	int extendsClassScore = 0;
	int callByScore = 0;
	int callScore = 0;
	int methodNum;
	int memberNum;
	int constructorNum = 0;
	int functionScore = 0;

	int needCallBy = 0;
	int selfCoverage = 0;

	
	FunctionComplexity funList[];
	MemberComplexity varList[];
	
	public int getMemberScore() { return memberScore; }
	public int getcallByScore() {return callScore; };
	public int getFunctionScore() { return functionScore;};
	
	
	public ClassComplexity(Class cls)
	{
		if(!cls.isLib())
			simpleMake(cls);
	}
	public void simpleMake(Class cls)
	{
		
		callScore(cls);
		callByScore(cls);
	//	memberScore(cls);
		extendScore(cls);
		setCoverAge(cls);
		methodNum = cls.getHasList().getFunctionList().size();
		memberNum = cls.getHasList().getMemberList().size();
		funList = new FunctionComplexity[methodNum];
		varList = new MemberComplexity[memberNum];
		int count = 0;
	
		for( Function fun : cls.getHasList().getFunctionList() )
		{
			funList[count] = fun.getComplex();			
			functionScore += funList[count++].getAbsComplex();
		}
		count = 0;
		for( Member member : cls.getHasList().getMemberList() )
		{
			varList[count] = member.getComplex();
			memberScore += varList[count++].getABS();
		}
		this.absComplex = functionScore + memberScore+callScore+extendsClassScore;
		int r = methodNum + memberNum;
		if(r == 0) r=1;
		this.relativeComplex = absComplex / ( r);
		//printScore();	
	}

	/**
	 * 
	 */
	private void memberScore(Class cls) 
	{
		for(Member var : cls.getHasList().getMemberList())
		{
		
			memberScore += var.getScore();
		}
		
	}
	private void extendScore(Class cls) 
	{
		Class superList[] = cls.getSuperClass();
		// TODO Auto-generated method stub
		 if( superList != null )
		 {
			extendsClassScore += superList.length;
			for(Class superClass : superList)
			{
				int superScore = superClass.getComplex().getExtendClassScore();
				extendsClassScore += superScore*superScore;
			}
			
		 }
		
	}
	
	public void setCoverAge(Class cls)
	{
		OMS haslist = cls.getHasList();
		OMS callList = cls.getCallList();
		
		for(Function fun : haslist.getFunctionList() )
		{
			if(callList.getFunctionList().contains(fun))
				selfCoverage++;
			else
				needCallBy++;
			
			if(fun.isConstructor())
				this.constructorNum++;
		}
		for(Member var : haslist.getMemberList() )
		{
			if(callList.getMemberList().contains(var))
				selfCoverage++;
			else
				needCallBy++;
		}
		
		
	}
	
	public void callScore(Class cls)
	{
		OMS list = cls.getCallList();
		for( Class target : list.getClassList() )
		{
			callScore += 3;
		}
		for( Function target : list.getFunctionList() )
		{
			callScore += 2;
		}
		for( Member target : list.getMemberList() )
		{
			callScore += 1;
		}
	}
	public void callByScore(Class cls)
	{
		OMS list = cls.getCallByList();
		for( Class target : list.getClassList() )
		{
			callByScore += 3;
		}
		for( Function target : list.getFunctionList() )
		{
			callByScore += 2;
		}
	}
	public void printScore()
	{
		System.out.println("==============\n");
		System.out.println("Total : "+absComplex+"\n");
		System.out.println("realative : "+ relativeComplex + "\n");
		System.out.println("상속 점수" + " : " + extendsClassScore);
		
		System.out.println("콜 관계 점수"+ " : "+callScore+"\n");
		System.out.println("맴버"+ " : "+ memberScore+"\n");
		System.out.println("함수"+ " : "+functionScore+"\n");
		
		
	}
	public int getSelfCoverage() {
		return selfCoverage;
	}
	public int getNeedCallBy() {
		return needCallBy;
	}
	public int getConstructorNum(){
		return constructorNum;
	}
	public int getExtendClassScore() {
		// TODO Auto-generated method stub
		return this.extendsClassScore;
	}
	public int getMethodNum() {
		// TODO Auto-generated method stub
		return this.methodNum;
	}
	public int getMemberNum() {
		// TODO Auto-generated method stub
		return memberNum;
	}
	public int getCallScore() {
		// TODO Auto-generated method stub
		return this.callScore;
	}
	
}
