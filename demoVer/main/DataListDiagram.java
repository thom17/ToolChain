package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import testDataSet.Datalist;
import testDataSet.ClassInfo;
import testDataSet.MemberInfo;
import testDataSet.FunctionInfo;

public class DataListDiagram
{
	static String skipList[] = {
			"java.lang",
			"java.util",
			"java.io"
	};
	
	private static boolean is_skip_package(String list[], String target)
	{
		for(String skip : list)
		{
			if(target.equals(skip)) return true;
		}
		
		return false;
	}

	
	//for Test Funcition
	public static void main(String test[]) throws InterruptedException, IOException
	{
		Process p;
		p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/datalist.txt");
		p.waitFor();
		System.out.println("test done! TestClass.svg");
	}
	
	public static void main(Datalist list) throws IOException, InterruptedException
	{
		
		System.out.println("make Datalist start...");
		String script = makeClassScript(list);	//Script 생성
		makeFile(script, "result/Datalist.txt");	
		Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/datalist.txt");
		p.waitFor();
		System.out.println("ClassDiagram draw done : Datalist.svg");		
	}
	public static void makeFile(String script, String src) throws IOException
	{
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}

	public static String makeClassScript(Datalist list)
	{
		StringBuilder str= new StringBuilder("@startuml\n");
		Map<String, ClassInfo> classMap = list.getClassMap();						
		str.append("/'do Class Define (클래스 정의)'/\n");
		for(String key : classMap.keySet())
		{
			ClassInfo target = classMap.get(key);
			String packageName = target.getPackageName();
			
			if ( is_skip_package(skipList, packageName) ) //해당 패키지를 가지고 스킾할 여부 판단.
				continue;
			else
			{
				classDef(target, str, key);
			}
		}
		str.append("/'Class Define (클래스 정의) end'/\n\n");
		
		//str.append( callDef(list, str) );
		str.append("\n@enduml");
		return str.toString();
	}
	private static void classDef(ClassInfo cls, StringBuilder str, String key) 
	{
		String name = cls.getSrcName();
		cls.getHasList();
		str.append("class ");
		str.append(name + "\n{\n");
		
		//test
		str.append("SrcName : "+cls.getSrcName()+"\n");
		str.append("keyString : "+ key + "\n");
		
		//add Has List
		str.append("__**<color:green>has list</color>**__\n");
		addListInfo(cls.getHasList() , str);
		//add Call List
		str.append("__**<color:blue>call list</color>**__\n");
		addListInfo(cls.getCallList(), str);

		str.append("}\n");
	}
	private static void addListInfo(Datalist datalist, StringBuilder str) 
	{
		
		//add classList
		str.append("..class..\n");
		
		Map<String, ClassInfo> map = datalist.getClassMap();
		for( ClassInfo target : map.values() )
		{
			str.append( target.getSrcName() +";\n" );
		}
		
		// add MemberList
		str.append("..member..\n");
		for (MemberInfo target : datalist.getMemberMap().values()) 
		{
			str.append("\t " + target.getName() + ";\n");
		}

		// add FunctionList
		str.append("..method..\n");
		for (FunctionInfo target : datalist.getFunctionMap().values()) 
		{
			str.append("\t" + target.getSig() + ";\n");
		}

	}
	
	
		
}


