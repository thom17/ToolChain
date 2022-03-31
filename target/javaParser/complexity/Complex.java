package complexity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dataSet.stateNode.StateNode;
import dataSet.Class;
import dataSet.DataList;

public class Complex 
{					//class    |  function
	int line = 0;	//line	   |  mebmers
	int score=0;	//variable |  field
	
	StringBuilder undefSize = new StringBuilder();	//sizeof List
	Map<String, String> memberScore = new LinkedHashMap();	//member type sig
	
	//simple
	public Complex(Function fun)
	{
		simpleMake(fun);
	
	}
	public Complex(Class cls)
	{
		simpleMake(cls);
	}
	public int getScore() {return this.score;}
	public int getLine() {return this.line;}
	public String getUndefSize() {return this.undefSize.toString();}
	public Map<String, String> getMemberScoreMap(){return this.memberScore;}
	private void simpleMake(Class cls)
	{
		line=getMemberScore(cls);
		addFunctionScore(cls);
		
	}
	
	private void addFunctionScore(Class cls) 
	{
		for(Function fun : cls.getHasList().getFunctionList())
		{
			Complex funComplex = fun.getComplex();
			int line = funComplex.line;
			int funscore = funComplex.score;
			int sum = line+funscore;
			String undef = funComplex.getUndefSize();
			String size = sum + undef;
			score += funComplex.score+funComplex.getLine();
			undefSize.append(undef);
			memberScore.put(fun.getSig(), size);
		}
		
	}
	private void simpleMake(Function fun)
	{
		getLineNum(fun);
		score=getMemberScore(fun);
		
	}
	private int getMemberScore(Data_base fun) 
	{
		int defScore = 0;
		ArrayList<Member> memberList = fun.getHasList().getMemberList();
		if(0 < memberList.size() )
			memberScore = new LinkedHashMap();
		
		for(Member target : memberList)
		{
			String key = target.getTypeName()+" "+target.getName();
			String score = "";
			int w = Member.getMemberScore(target);
			if(0 < w)
			{
				score = ""+w;
				defScore+=w;
			}
			else 
			{
				score = "sizeOf("+target.getTypeName()+")";
				undefSize.append("+ "+score);
			}
			memberScore.put(key, score);
		}
		return defScore;
	}

	private void getLineNum(Function fun)
	{
		Scanner scan = new Scanner(fun.getCode());
		while(scan.hasNext())
		{
			String str = scan.nextLine();
			if(str.contains(";"))
				line++;
		}
	}
}
