package diagram;

import dataSet.DataList;
import dataSet.Data_base;
import dataSet.Member;
import dataSet.stateNode.ForStateNode;
import dataSet.stateNode.StateNode;
import dataSet.stateNode.SwitchNode;
import dataSet.Function;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.Statement;

import complexity.Complex;
import dataSet.Class;


public class ForEx 
{
	static Map<String, ArrayList<Class>> separateMap;	//클래스를 패키지 단위로 분류 <패키지, 클래스_리스트>
	static String skipKey[] = {"[*]" , "connect", "forCondition", "forfin"}; //해당 노드 스킵 (stateNode 파싱에 사용)
	//개수는 노드명 id에 사용
	static int packageNum = 0;	//패키지 개수
	static int classNum = 0;	//클래스 개수
	static int funNum = 0;		//함수 개수
	static StringBuilder sb = new StringBuilder();
	
	//디버깅용내부 구조 print 출력 / print datalist console for debug
	public static void print(DataList datalist)
	{
		for(Class cls : datalist.getClassList())
		{
			System.out.println("package "+cls.getPackageName());
			System.out.println("class "+cls.getName());
			DataList hasList = cls.getHasList();
			
			System.out.println("\t=has member");
			for(Member member : hasList.getMemberList())
			{
				System.out.println("\t"+member.getTypeName()+" "+member.getName());
			}
			
			System.out.println("\n\t=has method");
			for(Function fun : hasList.getFunctionList())
			{
				System.out.println("\t\t"+fun.getTypeName() + " " + fun.getSig());
				//System.out.println("\t\tline is "+fun.getLine());
				for(Member member : fun.getHasList().getMemberList())
				{
					System.out.print("\t\t\t"+member.getTypeName()+" "+member.getName());	
					ArrayList<String> imgList = member.getImageList();
					if( 0 < imgList.size())
						System.out.print(" = " + imgList);
					System.out.println();
				}
			}
			
		}
		
	}
	//메인 함수 main Method
	public static void main(DataList datalist) throws IOException, InterruptedException
	{
		separateMap = datalist.separatePackage();
		sb = new StringBuilder("@startuml\n"+ "digraph G{\n");
		packageNum = classNum = funNum = 0;
		
		for(String packageName : separateMap.keySet())
		{
			packageMake(packageName, "\t");
		}
		sb.append("}\n@enduml");
		
		makeFile(sb.toString(), "result/final.txt");
		Process p = Runtime.getRuntime().exec("java -jar libs/plantuml.jar -tsvg result/final.txt");
		p.waitFor();
		System.out.println("quiz draw done : final.svg");
	}
	//script의 내용을 가지는 파일을 src경로 및 파일 이름으로 생성
	public static void makeFile(String script, String src) throws IOException
	{
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}
	//패키지 클러스터(cluster) 생성
	private static void packageMake(String packageName, String tab) 
	{
		String intab = tab+"\t";
		sb.append(tab+"subgraph cluster_package_"+ ++packageNum + "\n");
		sb.append(tab+"{\n");
		sb.append(intab+"label = \"package "+packageName+"\"\n");
		sb.append(intab+"color = orange\n\n");
		
		for(Class cls : separateMap.get(packageName))
			classMake(cls, intab);
		
		sb.append(tab+"}\n");
	}
	//클래스 클러스터(cluster) 생성
	public static void classMake(Class cls, String tab) 
	{
		String intab = tab+"\t";
		sb.append(tab+"subgraph cluster_class_"+ ++classNum + "\n");
		sb.append(tab+"{\n");
		sb.append(intab+"label = \"class "+cls.getName()+"\"\n");
		
		sb.append(intab+"style = filled\n");
		
		DataList hasList = cls.getHasList();
		int size = sizeMake(cls, "memberSize" ,tab);
		if(size < 40)
			sb.append(intab+"color = lightblue\n");
		else 
			sb.append(intab+"color = red\n");
		
		//memberMake(hasList.getMemberList(), intab);
		for(Function fun : hasList.getFunctionList())
		{
			funMake(fun, intab);
		}
		
		sb.append(tab+"}\n");
	}
	//함수 클러스터(cluster) 생성
	public static void funMake(Function fun, String tab) 
	{
		String intab = tab+"\t";
		sb.append(tab+"subgraph cluster_fun_"+ ++funNum + "\n");
		sb.append(tab+"{\n");
		sb.append(intab+"label = \""+fun.getSig()+"\"\n");
		sb.append(intab+"color = white\n");
		//sb.append(intab+"style = filled\n\n");
		
		sizeMake(fun, "lineNum" ,intab);
		stateMake(fun.getHeadNode(), intab);
		sb.append(tab+"}\n");
	}
	//클래스/함수 크기 노드 생성 (record 타입 노드 생성)
	private static int sizeMake(Data_base owner, String txt ,String tab)
	{
		Complex complex = owner.getComplex();
		int lineNum = complex.getLine();
		int memberScore = complex.getScore();
		String intab = tab+"\t";
		String unDefTypeSize = complex.getUndefSize();
		Map<String, String> memberScoreMap = complex.getMemberScoreMap();
		sb.append(tab+"size_"+owner.hashCode()+"[shape=record ,\n");
		sb.append(tab+"label = \"{ "+txt+" "+lineNum+"\n");
		
		for( String target : memberScoreMap.keySet())
		{
			String size = memberScoreMap.get(target);
			sb.append(intab + "| "+size + " : " + target+"\n");
		}
		
		int score = lineNum+memberScore;
		
		sb.append(intab+"| "+lineNum+" + "+memberScore+" = " + score);
		
		if(0 < unDefTypeSize.length() )
			sb.append(" "+unDefTypeSize);
		
		sb.append("\n"+tab+"}\"]\n\n");
		return score;
	}
	//함수 state 노드 생성
	private static void stateMake(StateNode headNode, String tab) 
	{
		String intab = tab+"\t";
		ArrayList<StateNode> visitStack = new ArrayList<StateNode>();
		StateNode stateNode = headNode;
		StateNode beforeNode = null;
		String id = "node"+stateNode.hashCode();
		sb.append(intab + id+"[ label =  \""+ stateNode.getCode()+"\"]\n");
		visitStack.add(stateNode);
		beforeNode = stateNode;
		stateNode = stateNode.getThen();
		
		stateParsing(stateNode, id, intab, true);
		
		
	}
	//state 노드 타입을 파싱하여 메서드 호출 및 재귀
	private static void stateParsing(StateNode stateNode, String parentId, String tab, boolean thenConnect) 
	{
		
		if(stateNode == null || checkIn(stateNode.getKey() , skipKey)) {
			//System.out.println("[*] find");
			return;
		}
		else if(stateNode instanceof ForStateNode){
			ForStateNode forStmt = (ForStateNode)stateNode;
			stateNode = forNodeMake(stateNode, parentId, tab);
			String condId = "node"+forStmt.getCondNode().hashCode();
			stateParsing(stateNode, condId, tab, false);
		}else if(stateNode instanceof SwitchNode) {
			String switchId = switchNodeStateMake(stateNode, parentId, tab);
			stateParsing(stateNode.getThen(), switchId, tab, true);
		}else{
			String id = "node"+stateNode.hashCode();
			if(thenConnect) sb.append(tab+parentId+"->"+id + "\n");
			else sb.append(tab+parentId+"->"+id + "[color = red]\n");
			if(stateNode.getElse() != null) //if StateNode
			{
				sb.append(tab+id+"[shape = diamond, color=gold, style=filled,"); //css
				sb.append("label = \""+stateNode.getCondition()+"\"]\n");
				stateParsing(stateNode.getElse(), id, tab, false);	//else Parsing
			}else if( stateNode.getKey().equals("forFin"))
			{
				sb.append(tab+id+"[color=hotpink, style = filled, "); //css
				sb.append("label = \""+stateNode.getCode()+"\"]\n");
			}
			else {
				sb.append(tab+id+"["); //css
				sb.append("label = \""+stateNode.getCode()+"\"]\n");
			}
			//System.out.println(tab+stateNode.getCode());
			stateParsing(stateNode.getThen(), id, tab, true);
		}	
		
	}
	//contain 함수와 같은 역활(string 용) String target이 list에 있으면 반환 
	private static boolean checkIn(String target, String[] skipList) {
		for(String in : skipList )
			if(in.equals(target))
				return true;
		return false;
	}
	//stateParsing으로 호출되며 swtichState를 swtich 클러스터로 생성 
	private static String switchNodeStateMake(StateNode stateNode, String parentId, String tab) 
	{
		SwitchNode node = (SwitchNode)stateNode;
		Map<String, ArrayList<Statement>> caseMap =node.getCaseMap();
		Set<String> caseSet = caseMap.keySet();
		String intab = tab+"\t";
		String mainId = "sw_"+stateNode.hashCode();
		
		sb.append(tab+parentId+"->"+mainId+"\n");
		sb.append(tab+"subgraph cluster_switchStmt_"+stateNode.hashCode()+"\n");
		sb.append(tab+"{\n");
		sb.append(intab+"color = skyblue label = \"switch("+node.getTarget()+")\"\n");
		sb.append(intab+mainId+"[shape = record label = \""+node.getTarget()+"\n");
		for(String caseStr : caseSet )
		{
			sb.append(intab+"| "+caseStr+"\n");
		}
		sb.append(intab+"\"]\n\n");
		
		for(String caseStr : caseSet)
		{
			ArrayList<Statement> stmtList = caseMap.get(caseStr);
			String caseId = "case_"+Math.abs(stmtList.hashCode());
			sb.append(intab+mainId+"-> "+caseId+"[color = blue]\n");
			sb.append(intab+caseId+"[shape = record label = \"{"+caseStr+"\n");
			
			for(Statement stmt : stmtList) 
				sb.append(intab+"| "+stmt.toString()+"\n");
		
			sb.append(intab+"}\"]\n\n");
		}
		sb.append(tab+"}\n");
		return mainId;
	}
	//stateParsing으로 호출되며 forState를 for 클러스터로 생성
	private static StateNode forNodeMake(StateNode stateNode, String parentId, String tab) 
	{

		ForStateNode forstmt = (ForStateNode)stateNode;
		String intab = tab+"\t";
		String id;
		sb.append(tab+parentId+"-> for_"+forstmt.hashCode()+"\n");
		sb.append(tab+"subgraph cluster_forStmt_"+stateNode.hashCode()+"\n");
		sb.append(tab+"{\n");
		sb.append(intab+"label = \""+forstmt.getForLine()+"\" style = bold color = pink\n");
		sb.append(intab+"for_"+forstmt.hashCode()+"[shape=none, label = \"ForStmt\"]\n");
		String beforeId = "for_"+forstmt.hashCode();
		StateNode pos = stateNode.getThen();
		while( pos != forstmt.getCondNode() )
		{
			id = "node"+pos.hashCode();
			sb.append(intab+beforeId+"->"+id+"\n");
			sb.append(intab+id+"[color = hotpink, style=filled, "); //css 
			sb.append("label = \""+pos.getCode()+"\"]\n");
			beforeId = id;
			pos = pos.getThen();
		}
		id = "node"+pos.hashCode();
		sb.append(intab + beforeId+ " -> " +id+"\n");
		sb.append(intab + id+"[shape = diamond, color=gold, style = filled, ");	//css
		sb.append(intab + "label = \""+pos.getCondition()+"\"]\n");
		
		StateNode ifState = pos;
		String ifkey = "node"+ifState.hashCode();
		pos = ifState.getThen();

		stateParsing(pos, ifkey, intab, true);
		id = "node"+forstmt.getLastFin().hashCode();
		
		sb.append(intab + id + "->"+ifkey+"\n");
		sb.append(tab+"}\n");
		return forstmt.getCond().getElse();
	}
}

/*
private static void memberMake(ArrayList<Member> memberList, String tab)
{
	int memberScore=0;
	String intab = tab+"\t";
	StringBuilder unDefTypeSize = new StringBuilder();
	sb.append(tab+"member_"+classNum+"[shape=record ,\n");
	sb.append(tab+"label = \"{ memberList\n");
	
	for( Member member : memberList)
	{
		String typeName = member.getTypeName();
		String size;
		int memberSize = Member.getMemberScore(member);
		//memberSize += getMemberSize(typeName , undDefTypeSize);
		if(0 <  memberSize ) 
		{
			memberScore += memberSize;
			size = Integer.toString(memberSize);
		}else
		{
			size = "sizeof("+typeName+")";
			unDefTypeSize.append(" + "+size);
		}
		sb.append(intab + "| "+size + " : " + typeName +" " +member.getName()+"\n");
	}
	sb.append(intab+"| "+memberScore+unDefTypeSize+"\n");
	sb.append(tab+"}\"]\n\n");

}
*/
