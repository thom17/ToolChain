package diagramMaker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import dataSet.Class;
import dataSet.DataList;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.statement.StateNode;

import java.util.Queue;
import java.util.Stack;
/**
 * actor 메서드 단위로 그룹 분리 및 생성
객체 : srcName
메서드 : sig

for (클래스 리스트 )
	그림 생성
	
*/
public class SequenceDeep extends Base
{	static ArrayList<Function> visitStack = new ArrayList<Function>();
	static ArrayList<Function> returnStack = new ArrayList<Function>();
	static String skipList[] = {
			"java.lang",
			//"java.util",
			"java.io"
	};
	//스킵 패키지 여부
	private static boolean isSkipList(String list[], String target)
	{
		for(String skip : list)
		{
			if(target.equals(skip)) return true;
		}
			
		return false;
	}
	
	public static void main(DataList list) throws IOException, InterruptedException
	{
		String time = Base.makeDateFolder("result/sequence/");
		String path = "result/sequence/"+ time +"/";
		
		for(Class target : list.getClassList())
		{
			if( isSkipList(skipList, target.getPackageName()) )
				continue;
		
			String srcName = target.getSrcName();
			String fileSrc = path + srcName + ".txt";
			String script = makeString(target);
			makeFile(script , fileSrc);
			System.out.println("file src:"+fileSrc);//"java -jar lib/plantuml.jar -tsvg "+fileSrc;
			Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg "+fileSrc);
			p.waitFor();
			System.out.println("SequenceDiagram draw done : " + srcName);
		}
	}

	private static String makeString(Class target) 
	{
		StringBuilder sb = new StringBuilder("@startuml\n");
		ArrayList<Function> funList = getCallerFunList(target);
		Stack<String> visitStack = new Stack<String>();
		for(Function fun : funList)
		{
			String groupName = fun.getTypeName() + " "+fun.getSig();
			sb.append("group "+groupName+"\n");
			drawConnect(fun, visitStack, "\t", sb);
			sb.append("end\n\n");
		}
		sb.append("@enduml\n");
		return sb.toString();
	}

	//target이 target이 아닌 다른 class를 호출하는 function 리스트를 반환
	private static ArrayList<Function> getCallerFunList(Class target) 
	{
		ArrayList<Function> funlist = new ArrayList<Function>();
		for(Function fun : target.getHasList().getFunctionList())
		{
			for(Class callTarget :fun.getCallList().getClassList())
			{
				if(callTarget == target)
					continue;
				else
				{
					funlist.add(fun);
					break;
				}
			}
		}
		
		return funlist;
	}
	private static void drawConnect(Function host, Stack<String> returnStack, String tab, StringBuilder sb) 
	{
		String hostName = host.getOwner().getName();
		
		if(visitStack.contains(host))
			return;
		else
			visitStack.add(host);
		for(Function client : host.getCallList().getFunctionList())
		{
			//Data_base clientOwner = client.getOwner();
			String clientName = client.getOwner().getName();
			String clientFunName = client.getSig();
			sb.append(tab+hostName+"->"+clientName+" : "+clientFunName+"\n");
			
			if( !hostName.equals( clientName ) )
			{
				returnStack.add(hostName);
				drawConnect(client, returnStack, tab+"\t", sb);
			}else
			{
				drawConnect(client, returnStack, tab+"\t", sb);
			}
		}
		
		if( !returnStack.isEmpty() )
		{
			String callerName = returnStack.pop();
			sb.append(tab+hostName+"-->"+callerName+"\n");	
		}
		
	}
	/*
	private static void drawConnect(Function caller, Queue<Function> visitQue, String tab, StringBuilder sb) 
	{
		Stack<Data_base> hostStack = new Stack<Data_base>();
		Data_base lastHost = caller.getOwner();
		int index =1;
		Data_base host = caller.getOwner();
		//boolean fin = false;
		while( !visitQue.isEmpty() )
		{
			Function target = visitQue.poll();
			Data_base client = target.getOwner();
			
			sb.append(tab+host.getName()+"->"+client.getName()+" : "+target.getSig() + "\n");
			
			host = client;
			//host = lastHost;
		}
	}
	*/
	public static void makeFile(String str, String fileSrc) throws IOException
	{
		File file = new File(fileSrc);
		FileWriter fw = new FileWriter(file, false);
		fw.write(str);
		fw.close();	
	}
	


}
