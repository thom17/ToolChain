package diagramMaker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import complexity.FunctionComplexity;
import dataSet.Class;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
/**
 * class, method, member 각각 생산
 * 하나의 파일당 하나의 Data_base
 * 생성경로 : result/datalist/time/Type/File
 * Data_base의 src이름으로 파일 생성
 * Map<String, Integer>와 같은 벡터 타입의 <>는 윈도우에선 사용불가능한 파일명
 * <> 를 ()로 변경 
 *
 */
public class DataListDiagram
{
	static String skipList[] = {
			"java.lang",
			"java.util",
			"java.io"
	};
	
	public static void main(String test[]) throws InterruptedException, IOException
	{
		/*
		Process p;
		p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg \"result/2021-06-18 14-45-35/datalist/class/forMidEx.Game.txt\"");
		p.waitFor();
		System.out.println("test done! TestClass.svg");
		*/
	}
	public static void draw(OMS list) throws IOException, InterruptedException
	{
		String folderSrc = makeDataListFolder();
		String src = folderSrc+"/class/";
		
		for(Data_base target : list.getClassList() )
			makeData_base(target, src);
		
		src =folderSrc+"/function/";
		for(Data_base target : list.getFunctionList())	
			makeData_base(target, src);	
		
		src =folderSrc+"/member/";
		for(Data_base target : list.getMemberList())
			makeData_base(target, src);
		
	}
	
	private static String makeDateFolder() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time =  format.format(date);
		String src = "result/datalist/"+time;
		File folder = new File(src);
		folder.mkdir();
		return time;
	}
	private static String makeDataListFolder()
	{
		String time = makeDateFolder();
		String src = "result/datalist/"+time;
	
		File clsFile = new File(src+"/class");
		clsFile.mkdir();
		
		File funFile = new File(src+"/function");
		funFile.mkdir();
		
		File memberFile = new File(src+"/member");
		memberFile.mkdir();
		return src;
	}
	private static void makeData_base(Data_base target, String srcPath) throws IOException {
		String srcName = target.getSrcName();
		String fileName = srcName+".txt";
		if(is_skip_package(skipList , target.getPackageName() )) return;
		
		StringBuilder str= new StringBuilder("@startuml\n");
		classDef(target, str );
		str.append("\n@enduml");
		
		makeFile(str.toString(), srcPath+fileName);
		String cmd = "java -jar lib/plantuml.jar -tsvg "+srcPath+fileName;
		System.out.println("process : "+cmd);
		Process p = Runtime.getRuntime().exec(cmd);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(srcName + " draw done");
	}
	
	
	public static void makeFile(String script, String src) throws IOException
	{
		src=src.replace("<", "(").replace(">", ")");
		System.out.println("make File "+src);
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}
	

	
	//상속 정보 작성

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
		
		//test
		str.append("__**<color:green>has list</color>**__\n");
		addList(target.getHasList(), str);
		
		str.append("__**<color:blue>calling list</color>**__\n");
		addList(target.getCallList(), str);
		
		str.append("__**<color:orange>called by list</color>**__\n");
		addList(target.getCallByList(), str);
		
		str.append("}\n");
	}
	//dataList 추가 has/call/call_by 중 하나
	private static void addList(OMS datalist, StringBuilder str) 
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
			Data_base owner = target.getOwner();
			String hashCode = "("+target.hashCode()+")";
			str.append("\t" + target.getTypeName()+" " +target.getName() +" "+hashCode+ ";\n");
		}
		// add FunctionList
		str.append("..method..\n");
		for (Function target : datalist.getFunctionList()) 
		{
			String hashCode = "("+target.hashCode()+")";
			str.append("\t" + target.getReturnType() +" "+ target.getSig()+" "+hashCode+ ";\n");
		}
		// TODO Auto-generated method stub
		
	}
	//함수 작성 (function)
	private static void functionDef(Function fun, StringBuilder str)
	{
		FunctionComplexity complex = fun.getComplex();
		String color = "";
		if(10 < complex.getABS())
			color = "#back:salmon";
		else if( complex.getABS() <= 3)
			color = "#line:green";
		String name = fun.getSig();
		String hashCode = fun.getOwner().getSrcName()+'.'+fun.getName()+fun.hashCode();
		str.append("class " + hashCode+" as "+'"'+name+'"' + color +" \n{\n");
		
		//test
		str.append("pram : "+fun.getParameterName()+"\n");
		str.append("SrcOwnClassName : "+fun.getOwner().getSrcName()+"\n");
		str.append("SrcSig : "+fun.getSrcName()+"\n");
		str.append("Sig : " + fun.getSig()+"\n");
		str.append("hashCode : " + fun.hashCode()+"\n");
		str.append("ABS Score : "+complex.getABS()+"\n");
		str.append("Relative Score : "+complex.getRES()+"\n");
		
		//test		
		str.append("__**<color:green>has list</color>**__\n");
		addList(fun.getHasList(), str);
		
		str.append("__**<color:blue>calling list</color>**__\n");
		addList(fun.getCallList(), str);
		
		str.append("__**<color:orange>called by list</color>**__\n");
		addList(fun.getCallByList(), str);
		str.append("}\n");
	}
}


