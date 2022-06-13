package diagramMaker.stateDiagram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dataSet.OMS;
import dataSet.Function;
import dataSet.statement.StateNode;
import dataSet.Class;

public class StateNodeDiagram 
{
	static ArrayList<StateNode> visitList;
	static int idNum = 0;
	public static void exe(String src) throws IOException, InterruptedException
	{
		System.out.println("Draw " + src +" ...\n");
		Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg " + '"' + src +'"' );
		p.waitFor();
	}
	public static void makeFile(String src, String script) throws IOException
	{
		System.out.println("make File "+src);
		File file = new File(src);
		FileWriter fw = new FileWriter(file , false);
		fw.write(script);
		fw.close();	
	}
	private static void makeScript(Class cls, StringBuilder sb) throws IOException, InterruptedException 
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat( "yyyy-mm-dd HH:mm:ss");
		String time = format.format(date);
		String folderSrc = "result/stateNode/" + cls.getSrcName();
		ArrayList<Function> functionList = cls.getHasList().getFunctionList();
		for(Function method : functionList)
		{
			visitList = new ArrayList<StateNode>();
			idNum = 0;
			
			sb.append("@startuml\n");
			sb.append("note "+'"'+time+'"' + " as time\n");
			String src = folderSrc + "/" +method.getSig() + ".uml";
			StateNode headNode = method.getHeadNode();
			if(method.getSrcName().equals("dataSet.Class.findFunction(java.lang.String)"))
				System.out.println();
			if(headNode != null)
			{
				String id = headNode.getObjectKey();
				sb.append("state \"state " + (++idNum) + "\" as " + id + "\n");
				sb.append("[*]-->"+id+"\n");
				drawNode(headNode, sb);
			}
			sb.append("@enduml\n");
			
			makeFile(src, sb.toString() );
			exe(src);
			sb.delete(0,  sb.length());
		}
	}
	public static void main(OMS datalist) throws IOException, InterruptedException
	{
		String folderPath =  "result/stateNode";
		File folder = new File(folderPath);
		if( !folder.exists() ) 
			folder.mkdir();
		
		System.out.println("make StateNodeDiagram start...");
		ArrayList<Class> classList = datalist.getClassList();
		for(Class cls : classList)
		{
			folderPath =  "result/stateNode/"+cls.getSrcName();
			folder = new File(folderPath);
			if( !folder.exists() ) 
				folder.mkdir();
			StringBuilder sb = new StringBuilder();
			makeScript(cls , sb);
		}
	}
	
	private static void drawNode(StateNode node, StringBuilder sb)
	{
		
		String id = node.getObjectKey();
		String key = node.getKey();
		String code = node.getCode();
		String check = node.getCondition();
		if(key!= null && key.equals("if"))
			code = "if("+check+")";
		if(visitList.contains(node))
		{
			return;
		}
		sb.append(id + " : key: " + key+"\n");
		sb.append(id + " : code: " + code+"\n");
		visitList.add(node);


		StateNode thenState = node.getThen();
		StateNode elseState = node.getElse();
		
		if(thenState != null)
		{
			String next = thenState.getObjectKey();
			check = thenState.getCondition();
			sb.append("state \"state "+ (++idNum) +"\" as " + next + "\n");
			sb.append(id+"-->"+next+" : "+check+"\n");
			drawNode(thenState, sb);
			
			if(elseState != null)
			{
				next = elseState.getObjectKey();
				sb.append("state \"state "+ (++idNum) +"\" as " + next + "\n");
				sb.append(id+"-->"+next+" : "+"else\n");
				drawNode(elseState, sb);
			}
		}
	}
	
	/*
	private static void drawNode(StateNode node, StringBuilder sb, String before)
	{
		if(node == null)
		{
			sb.append(before + " : null \n");
			return;
		}
		String key = node.getKey();
		String code = node.getCode();
		String check = node.getCondition();
		sb.append(before + " : key :" + key+";\n");
		sb.append(before + " : code :" + code+";\n");
		
		String next;
		if(before.equals("head") ) next = "";
		else next = before;
		
		sb.append(before +" --> "+next+"L : "+ check+"\n");
		sb.append(before +" --> "+next+"R : else\n");
		
		if(node.getThen() != null)
			sb.append(next+"L : " +node.getThen().toString()+"\n");
		
		drawNode(node.getThen() , sb , next+"L");
		drawNode(node.getElse() , sb , next+"R");
	}
	*/
	
}
