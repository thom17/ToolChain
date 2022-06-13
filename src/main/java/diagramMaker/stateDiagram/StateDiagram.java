package diagramMaker.stateDiagram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataSet.Class;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dataSet.statement.StateNode;

public class StateDiagram 
{
	static Class owner;
	static StringBuilder sb;
	
	public static void main(OMS list) throws IOException
	{
		ArrayList<Class> clsList = list.getClassList();
		for(Class cls : clsList )
		{
			String filePath = "result/state/"+cls.getSrcName()+".txt";
			drawMain(cls, filePath);
		}
	}
	public static void drawMain(Class target, String filePath) throws IOException
	{
		owner = target;
		sb = new StringBuilder("@startuml\n");
		ArrayList<Member> memberList = target.getHasList().getMemberList();
		for(Member member : memberList)
		{
			ArrayList<Function> callByList = member.getCallByList().getFunctionList();
			for(Function fun : callByList)
				draw(fun, member);
		}
		sb.append("@enduml");
		File file = new File(filePath);
		FileWriter fw = new FileWriter(file, false);
		fw.write(sb.toString());
		fw.close();	
		
		Runtime.getRuntime().exec("java -jar lib/plantuml.jar "+filePath);
		System.out.println("StateDiagram draw done : state.png");
	}
	private static void draw(Function target, Member member)
	{
		Data_base targetClass = target.getOwner();
		String targetSrc = targetClass.getSrcName() + "." + target.getSig();
		
		
		
	}
	public static void drawNode(StateNode node, String key)
	{
		if(node == null) return;
		
		String check = node.getCondition();
		//node.getStr = node.
		StateNode thenNode = node.getThen();
		StateNode elseNode = node.getElse();
		
		if(thenNode != null)
		{
			String thenKey = thenNode.getKey();		
			sb.append("\t"+ key + "-->" + thenKey + ": " + check+"\n");
			sb.append("\t"+thenKey+" : "+thenNode.getCode()+"\n");
			drawNode(thenNode, thenKey);
		} 
		else
			sb.append("\t"+ key + "--> [*]\n");
		
		if(elseNode != null)
		{
			String elseKey = elseNode.getKey();		
			sb.append( "\t"+key + "-->" + elseKey + ": else \n" );
			sb.append("\t"+elseKey+" : "+elseNode.getCode()+"\n");
			drawNode(elseNode, elseKey);
		}
		else
			sb.append("\t"+ key + "--> [*]\n");		
	}
	/*
	public static void drawNode(StateNode node, String key)
	{
		
		String check = node.getCondition();
		//node.getStr = node.
		StateNode thenNode = node.getThen();
		StateNode elseNode = node.getElse();
		
		if(thenNode != null)
		{
			String thenKey = thenNode.getKey();		
			sb.append("\t"+ key + "-->" + thenKey + ": " + check+"\n");
			sb.append("\t"+thenKey+" : "+thenNode.getCode()+"\n");
			drawNode(thenNode, thenKey);
		} 
		else
			sb.append("\t"+ key + "--> [*]\n");
		
		if(elseNode != null)
		{
			String elseKey = elseNode.getKey();		
			sb.append( "\t"+key + "-->" + elseKey + ": else \n" );
			sb.append("\t"+elseKey+" : "+elseNode.getCode()+"\n");
			drawNode(elseNode, elseKey);
		}
		else
			sb.append("\t"+ key + "--> [*]\n");		
	}
	*/
}
