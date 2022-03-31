package diagramMaker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import complexity.ClassComplexity;
import complexity.FunctionComplexity;
import complexity.StateNodeComplex;
import dataSet.DataList;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;


import dataSet.Class;
/**
 * Class-Function Class 모듈 기준 생산
 *
 *
 */
public class DataListDiagram2
{
	public static final int passMax = 4;
	public static final int failMin = 9;
	static String skipList[] = {
			"java.lang",
			"java.util",
			"java.io"
	};
	
	public static void main(String test[]) throws InterruptedException, IOException
	{
		Process p;
		p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/dataList.txt");
		p.waitFor();
		System.out.println("test done! TestClass.svg");
	}
	private static String makeDateFolder() 
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time =  format.format(date);
		String src = "result/datalist/ver2/"+time;
		File folder = new File(src);
		folder.mkdir();
		return time;
	}
	public static void draw(DataList list) throws IOException, InterruptedException
	{
		String time = makeDateFolder();
		String folderSrc = "result/datalist/ver2/"+time;
		HashMap<String, Class> classMap = list.getClassMap();							
		for(String key : classMap.keySet())
		{
			Class target = classMap.get(key);
			String packageName = target.getPackageName();
			
			if ( is_skip_package(skipList, packageName) ) 
				continue;
			else
			{
				String script = makeScript(target);
				String fileSrc = folderSrc+"/"+key+".txt";
				makeFile(script, fileSrc);
				String cmd = "java -jar lib/plantuml.jar -tsvg "+fileSrc;
				Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
				System.out.println(cmd);	
			}
		}	
	}

	public static void makeFile(String script, String src) throws IOException
	{
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}
	
	//클래스 작성 스크립트 메인
	public static String makeScript(Class cls)
	{
		StringBuilder str= new StringBuilder("@startuml\n");
		classDef(cls, str);
		str.append("\n package Functions{\n");
		for(Function fun : cls.getHasList().getFunctionList() )
		{
			functionDef(fun, str);
		}
		str.append("}\n");
		connectDef(cls, str);
		
		str.append("\n@enduml");
		return str.toString();
	}
	

	
	//상속 정보 작성

	private static void connectDef(Class cls, StringBuilder str) 
	{
		// TODO Auto-generated method stub
		for(Function host : cls.getHasList().getFunctionList())
		{
			String hostName = "f"+host.hashCode();
			for(Function client : host.getCallList().getFunctionList())
			{
				if(client.getOwner() != cls)
					continue;
				else
				{
					String clientName = "f"+client.hashCode();
					str.append(hostName+"-->"+clientName+" : call \n");
				}
				
			}
			
		}
		
	}
	//스킵 패키지 여부
	private static boolean is_skip_package(String list[], String target)
	{
		for(String skip : list)
		{
			if(target.equals(skip)) return true;
		}
		
		return false;
	}
	//클래스 작성 (class)
	private static void classDef(Data_base target, StringBuilder str)
	{
		String name = target.getName();
		str.append("class \""+name  +  "\" as "+ "cl1" +"{\n");
		
		defClassInfo((Class)target, str);
		
		str.append("__**<color:blue>calling list</color>**__\n");
		addList(target.getCallList(), str);
		
		str.append("__**<color:orange>called by list</color>**__\n");
		addList(target.getCallByList(), str);
		
		str.append("}\n");
	}
	//dataList 추가 has/call/call_by 중 하나
	private static void addList(DataList datalist, StringBuilder str) 
	{
		//add classList
		str.append("..class..\n");
		for(Class target : datalist.getClassList() )
		{
			String hashCode = "("+target.hashCode()+")";
			str.append("\t"+target.getSrcName()+" "+hashCode+";\n");
		}
		// add MemberList
		str.append("..member..\n");
		for (Member target : datalist.getMemberList()) 
		{
			String hashCode = "("+target.hashCode()+")";
			str.append("\t"+target.getSrcName() +" "+hashCode+ ";\n");
		}
		// add FunctionList
		str.append("..method..\n");
		for (Function target : datalist.getFunctionList()) 
		{
			String hashCode = "("+target.hashCode()+")";
			str.append("\t" +" "+ target.getSrcName()+" "+hashCode+ ";\n");
		}
		// TODO Auto-generated method stub
		
	}
	//함수 작성 (function)
	private static void functionDef(Function fun, StringBuilder str)
	{
		FunctionComplexity complex = fun.getComplex();
		String color = " ";
		if(ComplexDiagram.failMin <= complex.getABS())
			color = " #back:salmon";
		else if( complex.getABS() <= ComplexDiagram.passMax)
			color = " #line:green";
		String name = "f"+fun.hashCode();
		str.append("class \""+fun.getSig()+"\" as "+name+" "+color +"\n{\n");
		
		//test
		str.append("Static : "+fun.isStatic()+"\n");
		str.append("return Type : "+fun.getReturnType()+"\n");
		str.append("hashCode : " + fun.hashCode()+"\n");
		str.append("ABS : "+complex.getABS()+"\n");
		str.append("RES : "+complex.getRES()+"\n");
		
		
		//test		
		str.append("__**<color:green>has list</color>**__\n");
		addList(fun.getHasList(), str);
		
		str.append("__**<color:blue>calling list</color>**__\n");
		addList(fun.getCallList(), str);
		
		str.append("__**<color:orange>called by list</color>**__\n");
		addList(fun.getCallByList(), str);
		str.append("}\n");
	}

	private static void defClassInfo(Class cls, StringBuilder str) {
		String tab = "\t";

		str.append("Package "+cls.getPackageName()+"\n");
		str.append("Lib "+cls.isLib()+"\n");
		str.append("HashCode : " + cls.hashCode() + "\n");
		str.append("ABS : "+cls.getComplex().getABS()+"\n");
		str.append("RES : "+cls.getComplex().getRES()+"\n");

		DataList haslist = cls.getHasList();
		str.append("__**<color:green>has list</color>**__\n");
		for(Member member : haslist.getMemberList())
		{
			String color = "<color:green>";
			int abs = member.getComplex().getABS();
			if(2 < abs)
				color ="<color:red>";
			String info = color+member.getTypeName()+" "+member.getName()+" ("+abs+")";
			str.append(tab+info+"\n");			
		}
		str.append("....\n");
		for(Function fun : haslist.getFunctionList())
		{
			int cpLv = fun.getComplex().getAbsComplex();
			String color = "<color:salmon>";
			if(failMin < cpLv)
				color = "<color:red>";
			else if( cpLv <= passMax)
				color = "<color:green>";
			String info = color+fun.getReturnType()+" "+fun.getSig()+" ("+cpLv+")";
			str.append(tab+info+"\n");
		}
	}

}


