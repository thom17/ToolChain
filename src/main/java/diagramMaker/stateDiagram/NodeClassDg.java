package diagramMaker.stateDiagram;
/**
 * StateNode의 정보들 출력 StateNodeList는 hashId 리스트로 출력하여 확인 
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataSet.OMS;
import dataSet.Function;
import dataSet.statement.StateNode;
import dataSet.statement.StateNodeList;
import diagramMaker.Base;

public class NodeClassDg extends Base
{
	static String srcBase = "result/stateNode/nodeClassDg/";
	static StringBuilder sb;
	static int num=0;
	static ArrayList<StateNode> nodelist;
 	public static void main(OMS list) throws IOException, InterruptedException
	{
 		srcBase = srcBase+NodeClassDg.makeDateFolder(srcBase)+"/";
 		for(Function fun : list.getFunctionList())
 		{
 			String script = makeScript(fun);
 			String fileName = fun.getSrcName()+".txt";
 			makeFile(script, srcBase+fileName);
 			System.out.println("NodeClassDg draw done : "+srcBase+fileName);	
 		}
		
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
		nodelist =  new ArrayList<StateNode>();
		sb = new StringBuilder("@startuml\n");
		StateNode start = fun.getHeadNode();
		if(start != null)
		{
			stateNodeListInfo(fun.getNodeList(), "");
			stateNodeParsing(start, "");
		}
		sb.append("@enduml\n");
		return sb.toString();
	}
	
	private static void stateNodeParsing(StateNode target, String tab) 
	{
		if(target == null || nodelist.contains(target))
			return;
//		else if(target instanceof SwitchNode)
//			switchNodeInfo(target, tab);
		else
		{
			nodelist.add(target);
			stateNodeInfo(target, tab);
		}
			
		
	}
	/*
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
	*/
	private static void stateNodeListInfo(StateNodeList nodeList, String tab)
	{
		String intab = tab+"\t";
		String listId = getId(nodeList);
		String headId = getId(nodeList.getHead());
		String infoId;
		sb.append(tab+"state "+listId+" as \"StateNodeList\"{\n");
		makeListInfo(nodeList.getCallList(), "CallList" ,intab);
		makeListInfo(nodeList.getBifurcationList(), "BifurList" ,intab);
		makeListInfo(nodeList.getWriteList(), "WriteList" ,intab);
		makeListInfo(nodeList.getRepeatList(), "RepeatList" ,intab);
		sb.append(tab+"}\n");
		sb.append(tab+listId+"-->"+headId+" : headNode\n");
	}
	private static void makeListInfo(ArrayList list, String name ,String tab) 
	{
		String intab = tab+"\t";
		String infoId = getId(list);
		sb.append(tab+"state \""+name+"\" as "+infoId+"\n");
		for(Object obj : list)
		{
			sb.append(intab+infoId+" : "+getId(obj)+"\n");
		}
		
	}
	private static void stateNodeInfo(StateNode target, String tab) 
	{
		String id = getId(target);
		String key = target.getKey();
		String code = target.getCode();
		String intab = tab+"\t";
		sb.append(tab+"state \""+key+"\" as "+id+"\n");
		sb.append(intab+id+" : id : "+id+"\n");
		sb.append(intab+id+" : key : "+key+"\n");
		sb.append(intab+id+" : code : "+code+"\n");
		sb.append(intab+id+" : check : "+target.getCondition()+"\n");
		sb.append(intab+id+" : before : "+getId(target.getBefore())+"\n");
	
		
		StateNode then = target.getThen();
		StateNode elseThen = target.getElse();
		StateNodeList insideList = target.getInsideNode();
		
		String nextId = getId(then);
		
		sb.append("\n"+tab+id+"-->"+nextId+"\n");
		stateNodeParsing(then, tab);
		
		if(elseThen != null)
		{
			nextId = getId(elseThen);
			sb.append("\n"+tab+id+"-->"+nextId+" : else\n");
			stateNodeParsing(elseThen, tab);
		}
		if(insideList != null)
		{
			stateNodeListInfo(insideList, tab+"\t");
			nextId = getId(insideList.getHead());
			sb.append("\n"+tab+id+"-->"+nextId+" : inside\n");
			stateNodeParsing(insideList.getHead(), tab+"\t");
		}
	}
	
	private static String getId(Object target) 
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
