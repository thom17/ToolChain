package diagram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.Expression;

import dataSet.Class;
import dataSet.DataList;
import dataSet.Function;
import dataSet.Member;
import dataSet.stateNode.StateNode;
public class ForQuiz4 
{
	static String skipMember[] = {};
	static String skipClass[] = {};
	static String skipKey[] ={};// {"[*]" , "connect"}; //해당 노드 스킵 ([])
	static int funNum=0;
	static ArrayList<StateNode> nodelist;

	//test용 
	public static void main(String args[]) throws IOException, InterruptedException
	{
		Process p = Runtime.getRuntime().exec("java -jar libs/plantuml.jar result/quiz4.txt");
		p.waitFor();
		System.out.println("quiz draw done : quiz3.svg");
	}
	
	
	//datalist를 기반으로 그림을 생성하는 메인 함수
	public static void main(DataList datalist) throws IOException, InterruptedException
	{
 		nodelist = new ArrayList<StateNode>();
		String script = makeScript(datalist);
		makeFile(script, "result/quiz4.txt");
		//Process p = Runtime.getRuntime().exec("java -jar libs/plantuml.jar -tsvg result/quiz.txt");
		Process p = Runtime.getRuntime().exec("java -jar libs/plantuml.jar -tsvg result/quiz4.txt");
		p.waitFor();
		System.out.println("quiz draw done : quiz4.svg");
	}

	//script의 내용을 가지는 파일을 src경로 및 파일 이름으로 생성
	public static void makeFile(String script, String src) throws IOException
	{
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}
	
	//target이 checkList에 하나라도 존재한다면 스킵 대상이므로 return true;
 	private static boolean checkSkipList(String target , String checkList[])
 	{
 		for( String check : checkList)
 		{
 			if( target.equals(check) ) return true;
 		}
 		
 		return false;
 	}
 	
 	//스크립트 문을 생성하는 함수 datalist의 클래스들의 정보를 순차적으로 작성
	private static String makeScript(DataList datalist) 
	{
		StringBuilder sb = new StringBuilder("@startuml\n");
		ArrayList<Class> classList = datalist.getClassList();
		
		for(Class target : classList)
		{
			String targetName = target.getName();
			if( checkSkipList(target.getName(), skipClass)) continue;
			
			sb.append("state " + '"' + targetName + '"' + " as cl"+target.hashCode() + " {\n");
			makeMemberInfo(target, sb);
			makeFunctionInfo(target, sb);
			sb.append("}\n");
		}
		sb.append("@enduml");
		return sb.toString();
	}
	
	//makeScript로부터 호출되는 멤버의 정보를 순차적으로 작성 (target이 소유한 변수들)
	private static void makeMemberInfo(Class target, StringBuilder sb)
	{
		ArrayList<Member> memberList = target.getHasList().getMemberList();
		int hash = target.getHasList().hashCode();
		String hashCode="";
		if( hash < 0)
		{
			hash = hash * -1;
			hashCode = "_";
		}
		hashCode += hash;
		sb.append("\tstate "+'"' + "memberList" + '"' + " as has"+ hash+" #green{\n");
		for(Member var : memberList)
		{
			String typeName = var.getTypeName();
			String name = var.getName();
		
			if( checkSkipList(name, skipMember)) continue;
		
			String str = '"'+typeName + " " +name+'"';
			String key = "var"+var.hashCode();
			sb.append("\t\tstate " + str + " as " + key+":"+var.getImageList().toString()+"\n");			
		}
		sb.append("\t}\n");
	}
	
	//makeScript로부터 호출되는 함수의 정보를 순차적으로 작성 (target이 소유한 함수들)
	private static void makeFunctionInfo(Class target, StringBuilder sb) 
	{
		ArrayList<Function> functionList = target.getHasList().getFunctionList();
		
		for(Function fun : functionList)
		{
			//funNum =0;
			String funSig = fun.getSig();
			String typeName = fun.getTypeName();
			sb.append("\tstate " + '"' +typeName+" " +funSig+ '"' + " as fun"+fun.hashCode() + "{\n");
			makeExpInfo(fun.getHeadNode(), sb);
			sb.append("\t}\n");
		}	
	}
	
	//StateNode의 key값에 따라 색상을 반환
	private static String getColor(StateNode node)
	{
		String key = node.getKey();
		String color = "";
		
		if( key == null) 
			return color;
		
		if ( key.equals("expression") )
			color = "#Aquamarine"; //vardef_
		else if( key.equals("ifStmt"))
			color = "#SlateGray";
		else if( key.equals("forStmt"))
			color = "#red";
		else if( key.equals("forFin"))
			color = "#SlateGray";
		else if ( key.equals("returnStmt"))
			color = "#Tomato";
		else
			color="#pink";
		
		return color;
	}
	
	//StateNode의 일부 정보를 스킵하는 것을 반영한 함수. target의 thenNode를 반환함.
	private static StateNode nextThen(String skipList[] , StateNode target)
	{
		do{
			target = target.getThen();
		}while(target != null && checkSkipList(target.getKey(), skipList) );
		return target;
	}
	
	//StateNode의 일부 정보를 스킵하는 것을 반영한 함수. target의 elseNode를 반환함.
	private static StateNode nextElse(String skipList[] , StateNode target)
	{
		target = target.getElse();
		while(target != null && checkSkipList(target.getKey(), skipList) ) 
		{
			
			target=target.getThen();
		}
		return target;
	}
	
	//StateNode의 정보를 작성하는 함수. makeFunctionInfo로부터 호출됨.
	private static void makeExpInfo(StateNode node, StringBuilder sb) 
	{
		if(nodelist.contains(node))
			return;

		nodelist.add(node);
		
		String code = node.getCode();
		//if(code.contains('('));
		String check = node.getCondition();
		String before = "node"+node.hashCode();
		String color = getColor(node);
		System.out.println(code);
		sb.append("\tstate " + '"' + node.getKey() + '"' + " as node"+node.hashCode() + color +":"+code+"\n");
		StateNode thenNode = nextThen(skipKey, node);
		if( thenNode != null)
		{
			if(check.equals("true")) check = "";
			else check = ":"+check;
			
			sb.append("\t"+before + "--> "+"node"+thenNode.hashCode()+" "+check+"\n");
			makeExpInfo(thenNode,sb);
			
			StateNode elseNode = nextElse(skipKey, node);
			if(elseNode != null)
			{
				sb.append("\t"+before + "--> "+ "node"+elseNode.hashCode() +" : else"+"\n");
				makeExpInfo(elseNode, sb);
			}
		}
	}
}
