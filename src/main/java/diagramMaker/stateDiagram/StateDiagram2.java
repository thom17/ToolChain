package diagramMaker.stateDiagram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataSet.Class;
import dataSet.OMS;
import dataSet.Function;
import dataSet.statement.StateNode;

public class StateDiagram2 
{

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
		sb = new StringBuilder("@startuml\n");
		ArrayList<Function> functionList = target.getHasList().getFunctionList();
		for(Function fun : functionList)
		{
			String funName = fun.getName();
			sb.append("[*]-->"+funName+"\n");
			sb.append("state "+funName+"{\n");
			
			StateNode stateNode = fun.getHeadNode();
			if(stateNode != null)
				drawNode(stateNode, "[*]");
			sb.append("}\n");
		}
		sb.append("@enduml");
		File file = new File(filePath);
		FileWriter fw = new FileWriter(file, false);
		fw.write(sb.toString());
		fw.close();	
		
		Runtime.getRuntime().exec("java -jar lib/plantuml.jar "+filePath);
		System.out.println("StateDiagram draw done : state.png");
	}
	public static void drawNode(StateNode node, String key)
	{
		if(node == null) 
		{
			sb.append("\t + key + --> [*]\n");
			return;
		}
		String check = node.getCondition();
	
		if( check.equals("true") ) 
			check = "";
		else 
			check = " : " + check;
		
		//node.getStr = node.
		StateNode thenNode = node.getThen();
		StateNode elseNode = node.getElse();
		
		if(thenNode != null)
		{
			String thenKey = thenNode.getKey();		
			sb.append("\t"+ key + "-->" + thenKey + check+"\n");
			sb.append("\t"+thenKey+" : "+thenNode.getCode()+"\n");
			drawNode(thenNode, thenKey);
		} 
		else
		{
			sb.append("\t"+ key + "--> [*]\n");
		}
		
		if(elseNode != null)
		{
			String elseKey = elseNode.getKey();		
			sb.append( "\t"+key + "-->" + elseKey + " : else \n" );
			sb.append("\t"+elseKey+" : "+elseNode.getCode()+"\n");
			drawNode(elseNode, elseKey);
		}
		else if( !check.equals("") )
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
