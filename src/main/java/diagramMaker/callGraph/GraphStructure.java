package diagramMaker.callGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import complexity.ClassComplexity;
import complexity.FunctionComplexity;
import dataSet.Class;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;

public class GraphStructure 
{
	static boolean printImage = false;
	static StringBuilder tab = new StringBuilder();
	static String skipList[] = {
			"java.lang",
			"java.util",
			"java.io"
	};
	
	public static void main(String test[]) throws InterruptedException, IOException
	{
		Process p;
		p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/graph.txt");
		p.waitFor();
		System.out.println("test done! graph.svg");
	}
	public static void main(OMS list) throws IOException, InterruptedException
	{
		
		System.out.println("make Graph start...");
		String script = makeGraphScript(list);
		makeFile(script, "result/graph.txt");
		Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/graph.txt");
		p.waitFor();
		System.out.println("Graph draw done : graph.svg");	
	}

	public static void makeFile(String script, String src) throws IOException
	{
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}
	
	//클래스 작성 스크립트 메인
	public static String makeGraphScript(OMS list)
	{
		StringBuilder str= new StringBuilder("@startuml\n");
		str.append("skinparam nodesep 0.1\r\n"
				+ "skinparam ranksep 0\n");
		str.append("/' Class Def'/\n");
		for(Class cls : list.getClassList())
			classDef(cls, str);
		
		str.append("/' Function Def'/\n");
		for(Function fun: list.getFunctionList())
			functionDef(fun, str);
		
		str.append("/' Member Def'/\n");
		for(Member member: list.getMemberList())
			memberDef(member, str);
		
		str.append("/' Class Extends'/\n");
		for(Class cls : list.getClassList())
			classExtends(cls, str);
		
		str.append("\n@enduml");
		return str.toString();
	}
	//함수 작성 스크립트 메인 (dataList (Class) )

	private static void classExtends(Class cls, StringBuilder str)
	{
			if(is_skip_package(skipList, cls.getPackageName()) ) return;
			
			Class []superClassList = cls.getSuperClass();
			
			if(superClassList != null)
			{
				for(Class superClass : superClassList)
				{
					String child = getHash(cls);
					String superCls = getHash(superClass);
					if( !is_skip_package(skipList , superClass.getPackageName()))
						str.append(child + " -->" + superCls +": " + cls.getName()+" is "+superClass.getName()+"\n");
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

	private static String getHash(Data_base object)
	{
		String type;
		if(object instanceof Class) type = "class";
		else if(object instanceof Function) type = "fun";
		else type="member";
		
		int hash = object.hashCode();
		
		if(hash < 0)
		{
			return type+"_" + (hash * -1);
		}else
			return type+""+hash;
	}
	
	private static void addListSize(Data_base target, String hash, StringBuilder str)
	{
		OMS hasList = target.getHasList();
		OMS callList = target.getCallList();
		OMS callByList = target.getCallByList();
		
		str.append("/' has List '/\n");
		String tag = " : has\n";
		int hasSize = addList(hash,"[#green]" , tag ,hasList, str);
		
		tag = " : call\n";
		str.append("/' call List '/\n");
		int callSize = addList(hash,"[#blue]" , tag ,callList, str);
		/*
		tag = " : call by\n";
		str.append("/' call By List '/\n");
		int callBySize = addList(hash,"[#red]" , tag, callByList, str);
		*/
		str.append("\t"+hash+" : hasList Size "+hasSize+"\n");
		str.append("\t"+hash+" : callList Size "+callSize+"\n");
		//str.append("\t"+hash+" : callByList Size "+callBySize+"\n");
	}
	
	private static void classDef(Class target, StringBuilder str) 
	{
		String packageName = target.getPackageName();
		if(is_skip_package(skipList, packageName))
			return;
		
		String name = target.getSrcName();
		String hash = getHash(target);
		String title = '"' +"Class "+name +'"';
		
		str.append( "state " + hash + " as " + title +"{\n");
		str.append("\t"+hash+" : " + "package " + packageName+"\n");
		addListSize(target, hash, str);
		str.append("}\n");
	}
	//dataList 추가 has/call/call_by 중 하나
	private static int addList(String hash, String color , String tag , OMS datalist, StringBuilder str) 
	{
		int count=0;
		//add classList
		str.append("  /'..class..'/\n");
		for(Class target : datalist.getClassList() )
		{
			if(!is_skip_package(skipList , target.getPackageName() ) )
			{
				str.append("\t"+hash+" --"+color+"> "+getHash(target) + " " + tag);
				count++;
			}
		}
		// add MemberList
		str.append("  /'..member..'/\n");
		for (Member target : datalist.getMemberList()) 
		{
			if(!is_skip_package(skipList , target.getPackageName() ) )
			{
				str.append("\t"+hash+" --"+color+"> "+getHash(target)+"\n");
				count++;
			}
		}
		// add FunctionList
		str.append("  /'..method..'/\n");
		for (Function target : datalist.getFunctionList()) 
		{
			if(!is_skip_package(skipList , target.getPackageName() ) )
			{
				str.append("\t"+hash+" --"+color+"> "+getHash(target)+"\n");
				count++;
			}
		}
		return count;
	}
	//함수 작성 (function)
	private static void functionDef(Function target, StringBuilder str)
	{
		Data_base owner = target.getOwner();
		if(is_skip_package(skipList, owner.getPackageName()))
			return;
		
		String sig = target.getSig();
		String hash = getHash(target);
		
		String ownerHash = getHash(target.getOwner());
		String title = '"'+ target.getReturnType() + " "  + sig +'"';
		
		str.append( "state " + hash + " as " + title +"#lightblue{\n");
		str.append("\t"+hash+" : " + "owner " + owner.getName()+"\n");
		addListSize(target, hash, str);
		str.append("\n");
		str.append("\t"+ hash + "-->" +ownerHash +" : owner\n");
		str.append("}\n");
	}
	private static void memberDef(Member target, StringBuilder str)
	{
		Data_base owner = target.getOwner();
		if(is_skip_package(skipList, owner.getPackageName()))
			return;
		
		String hash = getHash(target);
		String title = '"'+target.getTypeName() + " "  + target.getName()+'"';
		
		str.append( "state " + hash + " as " + title +"#aliceblue{\n");
		str.append("\t"+hash+" : " + "owner " + owner.getName()+"\n");
		
		if(printImage) str.append("\t"+hash+" : " + "imageList " + target.getImageList()+"\n");
		
		addListSize(target, hash, str);
		str.append("}\n");
	}

}
