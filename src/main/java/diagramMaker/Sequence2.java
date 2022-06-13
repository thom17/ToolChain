package diagramMaker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import dataSet.Class;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dataSet.statement.ForStateNode;
import dataSet.statement.MethodCallStateNode;
import dataSet.statement.StateNode;
import dataSet.statement.StateNodeList;
import dataSet.statement.StmtType;
/**
*/
public class Sequence2 extends Base
{
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
	
	public static void main(OMS list) throws IOException, InterruptedException
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
		StateNodeList stNodeList = host.getStateNodeList();
		Stack<StateNode> visitList = new Stack<StateNode>();
		visit(stNodeList.getHead(), visitList, sb, "");
	
		
	}
	
	private static String visit(StateNode node, Stack<StateNode> visitList, StringBuilder sb, String tab) {
		// TODO Auto-generated method stub
		if(node == null || visitList.contains(node))
			return "";
		else 
			visitList.add(node);
		
		if(node.getType() == StmtType.methodCallExpr )
		{
			
			MethodCallStateNode callNode = (MethodCallStateNode) node;
			Function client = callNode.getClient();
			Function host = callNode.getHost();
			Class hostCls = host.getOwnerClass();
			Class clientCls = client.getOwnerClass();
			sb.append(tab+hostCls.getSrcName()+" --> "+clientCls.getSrcName() + " : "+node.getCode()+"\n");
			String returnStr = visit(client.getHeadNode(), new Stack<StateNode>(), sb, tab+"\t");
			sb.append(tab+clientCls.getSrcName()+"-->"+hostCls.getSrcName()+" : "+returnStr+"\n");
			return visit(node.getThen(), visitList, sb, tab);
		}else if(node.getType() == StmtType.ifStmt)
		{
			String cond = node.getCondition();
			sb.append(tab+"group if "+cond+"\n");
			visit(node.getThen(), visitList, sb, tab+"\t");
			sb.append(tab+"else\n");
			visit(node.getElse(), visitList, sb, tab+"\t");
			
		}else if(node.getType() == StmtType.connect )
		{
			sb.append(tab+"end\n");
			return visit(node.getThen(), visitList, sb, tab);
		}
		else if(node.getType() == StmtType.repeatStmt)
		{
			ForStateNode forNode = (ForStateNode) node;
			String cond = forNode.getCondition();
			sb.append(tab+"loop "+cond+"\n");
		}
		return null;
	}

	private static void altMake(StateNode node, Stack<StateNode> visitList, StringBuilder sb, String string) {
		// TODO Auto-generated method stub
		StateNode thenNode = node.getThen();
		StateNode elseNode = node.getElse();
		
		
		
		
	}

	public static void makeFile(String str, String fileSrc) throws IOException
	{
		File file = new File(fileSrc);
		FileWriter fw = new FileWriter(file, false);
		fw.write(str);
		fw.close();	
	}
	


}
