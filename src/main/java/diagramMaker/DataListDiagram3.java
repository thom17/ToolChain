package diagramMaker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import complexity.ClassComplexity;
import complexity.FunctionComplexity;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;

import dataSet.Class;
/**
 * Class, Function, Member 각각 생산
 * 하나의 파일에 모든 Data_base 포함
 * 경로 result/
 *
 */
public class DataListDiagram3
{
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
	public static void draw(OMS list) throws IOException, InterruptedException
	{
		
		System.out.println("make DataList start...");
		String script = makeClassScript(list);
		makeFile(script, "result/dataList(class).txt");
		Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/dataList(class).txt");
		p.waitFor();
		System.out.println("ClassDiagram draw done : DataList(Class).svg");
		
		script = makeFunctionScript(list);
		makeFile(script, "result/dataList(Function).txt");
		p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/dataList(Function).txt");
		p.waitFor();
		System.out.println("FunctionDiagram draw done : DataList(Function).svg");
		
		script = makeMemberScript(list);
		makeFile(script, "result/dataList(Member).txt");
		p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/dataList(Member).txt");
		p.waitFor();
		System.out.println("FunctionDiagram draw done : DataList(Member).svg");
		
	}
	private static String makeMemberScript(OMS list) 
	{
		StringBuilder str = new StringBuilder("@startuml\n");

		ArrayList<Member> members = list.getMemberList();
		str.append("/'do Class Define (클래스 정의)'/\n");
		for (Member target : members) 
		{
			Data_base owner = target.getOwner();
			String packageName = owner.getPackageName();

			if (is_skip_package(skipList, packageName))
				continue;
			else
				MemberDef(target, str);

		}
		str.append("/'Class Define (클래스 정의) end'/\n\n");

		str.append("/'do Extends Define (상속 관계 정의)'/\n");
		str.append("/'Extends Define (상속 관계 정의) end'/\n\n");

		// str.append( callDef(list, str) );
		str.append("\n@enduml");
		return str.toString();
	}
	private static void MemberDef(Member target, StringBuilder str) 
	{
		String name = target.getOwner().getSrcName()+"."+target.getName();
		str.append("class " + name + "\n{\n");
		
		//test
		str.append("modifier : "+target.getModifier()+"\n");
		str.append("SrcOwnClassName : "+target.getOwner().getSrcName()+"\n");
		str.append("TyeName : " + target.getTypeName()+"\n");
		str.append("hash : " + target.hashCode()+"\n");
		
		//test
		//addHasList(fun, str);
		str.append("__**<color:green>Image List</color>**__\n");
		for(String image : target.getImageList() )
		{
			str.append(image+"\n");
		}

		str.append("__**<color:blue>call list</color>**__\n");
		addList(target.getCallList(), str);
		
		str.append("__**<color:orange>call by list</color>**__\n");
		addList(target.getCallByList(), str);
		str.append("}\n\n");
	
		
	}
	public static void makeFile(String script, String src) throws IOException
	{
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}
	
	//클래스 작성 스크립트 메인
	public static String makeClassScript(OMS list)
	{
		StringBuilder str= new StringBuilder("@startuml\n");
		ArrayList<Class> cls = list.getClassList();
		HashMap<String, Class> classMap = list.getClassMap();							
		str.append("/'do Class Define (클래스 정의)'/\n");
		for(String key : classMap.keySet())
		{
			Class target = classMap.get(key);
			String packageName = target.getPackageName();
			
			if ( is_skip_package(skipList, packageName) ) continue;
			else
			{
	//			ClassComplexity cp = target.getComplex();
				classDef(target, str, key);
			}
		}
		str.append("/'Class Define (클래스 정의) end'/\n\n");
		
		str.append("/'do Extends Define (상속 관계 정의)'/\n");
		classExtends(list, str);
		str.append("/'Extends Define (상속 관계 정의) end'/\n\n");
		
		
		//str.append( callDef(list, str) );
		str.append("\n@enduml");
		return str.toString();
	}
	//함수 작성 스크립트 메인 (dataList (Class) )
	private static String makeFunctionScript(OMS list) 
	{
		StringBuilder str = new StringBuilder("@startuml\n");

		ArrayList<Function> cls = list.getFunctionList();
		str.append("/'do Class Define (클래스 정의)'/\n");
		for (Function target : cls) 
		{
			Data_base owner = target.getOwner();
			String packageName = owner.getPackageName();

			if (is_skip_package(skipList, packageName))
				continue;
			else
				functionDef(target, str);

		}
		str.append("/'Class Define (클래스 정의) end'/\n\n");

		str.append("/'do Extends Define (상속 관계 정의)'/\n");
		str.append("/'Extends Define (상속 관계 정의) end'/\n\n");

		// str.append( callDef(list, str) );
		str.append("\n@enduml");
		return str.toString();
	}
	//상속 정보 작성
	private static void classExtends(OMS list, StringBuilder str)
	{
		ArrayList<Class> classList = list.getClassList();
		for( Class cls : classList )
		{
			if(is_skip_package(skipList, cls.getPackageName()) ) continue;
			
			Class []superClassList = cls.getSuperClass();
			
			if(superClassList != null)
			{
				for(Class superClass : superClassList)
				{
					if( !is_skip_package(skipList , superClass.getPackageName()))
						str.append("\t"+cls.getSrcName() + "-->" + superClass.getSrcName() +": "+cls.getName()+" is "+superClass.getName()+"\n");
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
	private static void classDef(Data_base cls, StringBuilder str, String key) 
	{
		Class target = (Class)cls;
		String name = target.getSrcName();
		ClassComplexity complex  = target.getComplex();
		String color = " ";
		if(3 <= complex.getRES() )
			color = " #back:salmon";
		else if( complex.getRES() <2)
			color = " #line:green";
		
		if(cls.isAbstract()) str.append("abstract ");
		else if(target.isInterface()) str.append("interface ");
		else str.append("class ");
		
		str.append(name + color +  " \n{\n");
		
		//test
		str.append("SrcName : "+target.getSrcName()+"\n");
		//str.append("keyString : "+ key + "\n");
		str.append("hashCode : " + cls.hashCode() + "\n");
		str.append("ABS Score : "+complex.getABS()+"\n");
		str.append("Relative Score : "+complex.getRES()+"\n");
		
		//test
		str.append("__**<color:green>has list</color>**__\n");
		addList(cls.getHasList(), str);
		
		str.append("__**<color:blue>calling list</color>**__\n");
		addList(cls.getCallList(), str);
		
		str.append("__**<color:orange>called by list</color>**__\n");
		addList(cls.getCallByList(), str);
		
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
			str.append("\t"+target.getName()+" "+hashCode+";\n");
		}
		// add MemberList
		str.append("..member..\n");
		for (Member target : datalist.getMemberList()) 
		{
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


