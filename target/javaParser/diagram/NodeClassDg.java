package diagram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

import dataSet.Class;
import dataSet.DataList;
import dataSet.Function;
import dataSet.Member;
import dataSet.stateNode.StateNode;
import dataSet.stateNode.SwitchNode;

public class NodeClassDg 
{
	static StringBuilder sb;
	static int num=0;
	static ArrayList<StateNode> nodelist;
 	public static void main(Function target) throws IOException, InterruptedException
	{
 		nodelist =  new ArrayList<StateNode>();
		String script = makeScript(target);
		String fileSrc = "result/stateNode/"+target.getName()+".txt";
		makeFile(script, fileSrc);
		//Process p = Runtime.getRuntime().exec("java -jar libs/plantuml.jar -tsvg result/quiz.txt");
		Process p = Runtime.getRuntime().exec("java -jar libs/plantuml.jar -tsvg "+fileSrc);
		p.waitFor();
		System.out.println("quiz draw done : "+fileSrc);
	}
 	public static void makeFile(String script, String src) throws IOException
	{
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}
 	private static boolean checkSkipList(String target , String checkList[])
 	{
 		for( String check : checkList)
 		{
 			if( target.equals(check) ) return true;
 		}
 		
 		return false;
 	}
	private static String makeScript(Function fun) 
	{
		sb = new StringBuilder("@startuml\n");
		StateNode start = fun.getHeadNode();
		stateNodeParsing(start, "");
		sb.append("@enduml\n");
		return sb.toString();
	}
	
	private static void stateNodeParsing(StateNode target, String tab) 
	{
		if(target == null || nodelist.contains(target))
			return;
		else if(target instanceof SwitchNode)
			switchNodeInfo(target, tab);
		else
		{
			stateNodeInfo(target, tab);
		}
			
		
	}
	private static void switchNodeInfo(StateNode target, String tab) 
	{
		nodelist.add(target);
		SwitchNode node = (SwitchNode)target;
		Map<String, ArrayList<Statement>> caseMap =node.getCaseMap();
		Set<String> caseSet = caseMap.keySet();
		
		
		String id = getId(target);
		String key = target.getKey();
		String intab = tab+"\t";
		sb.append(tab+"state \""+key+"\" as "+id+"\n");
		sb.append(intab+id+" : key : "+key+"\n");
		sb.append(intab+id+" : check : "+target.getCondition()+"\n");
		
		StateNode then = target.getThen();
		StateNode elseThen = target.getElse();
		String nextId = getId(then);
		
		sb.append("\n"+tab+id+"-->"+nextId+"\n");
		stateNodeParsing(then, tab);
		if(elseThen != null)
		{
			nextId = getId(elseThen);
			sb.append("\n"+tab+id+"-->"+nextId+" : else\n");
			stateNodeParsing(elseThen, tab);
		}
		
	}
	private static void stateNodeInfo(StateNode target, String tab) 
	{
		nodelist.add(target);
		String id = getId(target);
		String key = target.getKey();
		String code = target.getCode();
		String intab = tab+"\t";
		sb.append(tab+"state \""+key+"\" as "+id+"\n");
		sb.append(intab+id+" : key : "+key+"\n");
		sb.append(intab+id+" : code : "+code+"\n");
		sb.append(intab+id+" : check : "+target.getCondition()+"\n");
		
		StateNode then = target.getThen();
		StateNode elseThen = target.getElse();
		String nextId = getId(then);
		
		sb.append("\n"+tab+id+"-->"+nextId+"\n");
		stateNodeParsing(then, tab);
		if(elseThen != null)
		{
			nextId = getId(elseThen);
			sb.append("\n"+tab+id+"-->"+nextId+" : else\n");
			stateNodeParsing(elseThen, tab);
		}
	}
	
	private static String getId(StateNode target) 
	{
		if(target == null)
			return "[*]";
		int hash = target.hashCode();
		
		if(0 < hash)
			return "node"+hash;
		else 
			return "node_"+(-1*hash);
	}
}
