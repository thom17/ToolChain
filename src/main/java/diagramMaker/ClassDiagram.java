package diagramMaker;
/**첫버전 = 잘안됨.
 * CLASS DG Composistion 등 추가해봄 (첫버전 잘안됨)
*/
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import complexity.ClassComplexity;
import dataSet.DataList;
import dataSet.Function;
import dataSet.Member;
import dataSet.Class;

public class ClassDiagram 
{
	static String skipList[] = {
			"java.lang",
		//	"java.util",
			"java.io" ,
			"shopTest"
	};
	
	public static void main(DataList list) throws IOException, InterruptedException
	{
		makeFile( makeString(list) );
		Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/classDg1.txt");
		p.waitFor();
		System.out.println("ClassDiagram draw done : classDg1");
	}
	public static void makeFile(String str) throws IOException
	{
		File file = new File("result/classDg1.txt");
		FileWriter fw = new FileWriter(file, false);
		fw.write(str);
		fw.close();	
	}
	
	private static boolean is_skip_package(String list[], String target)
	{
		for(String skip : list)
		{
			if(target.equals(skip)) return true;
		}
		
		return false;
	}
	
	public static String makeString(DataList list)
	{
		StringBuilder str= new StringBuilder("@startuml\n");
		
		str.append("/'do Class Define (클래스 정의)'/\n");
		classDef(list, str);
		str.append("/'Class Define (클래스 정의) end'/\n\n");
		
		str.append("/'do Extends Define (상속 관계 정의)'/\n");
		classExtends(list, str);
		str.append("/'Extends Define (상속 관계 정의) end'/\n\n");
		
		str.append("/'do call By Define-Class (클래스-클래스 호출 관계 정의)'/\n");
		callByDef(list, str);
		str.append("/'call By Define-Class (클래스-클래스 호출 관계 정의)' end/\n\n");
		
		
		//str.append( callDef(list, str) );
		str.append("\n@enduml");
		return str.toString();
	}

	private static void callByDef(DataList list, StringBuilder str ) 
	{
		
		ArrayList<Class> classList = list.getClassList();
		for(Class cls : classList)
		{
			if(is_skip_package(skipList, cls.getPackageName())) continue;
			
			DataList callList = cls.getCallList();
			ArrayList<Class> clientList = callList.getClassList();
			for(Class client : clientList)
			{
				if( is_skip_package(skipList, client.getPackageName())) continue;
				else if(cls.getHasList().findClass(client.getSrcName()) != null)
				{
					if(isComposition(cls, client, list))
						str.append("\t"+cls.getSrcName()+" *-- "+client.getSrcName()+" : has(Composition) "+ client.getSrcName()+"\n");
					else
						str.append("\t"+cls.getSrcName()+" o-- "+client.getSrcName()+" : has "+ client.getSrcName()+"\n");
				}
				else
					str.append("\t"+cls.getSrcName()+" -- "+client.getSrcName()+" : ref "+ client.getSrcName()+"\n");
			}
				
		}
		str.append("\n\n");
	}

	private static boolean isComposition(Class owner, Class client, DataList list) 
	{
		client.getCallByList();
		for(Class cls : list.getClassList())
		{
			if(owner == cls || client == cls) continue;
			else
			{
				if ( cls.getCallList().findClass(client.getSrcName()) != null)
						return false;
			}
		}
		return true;
	}
	private static void classExtends(DataList list, StringBuilder str)
	{
		ArrayList<Class> classList = list.getClassList();
		for( Class cls : classList )
		{
			Class []superClassList = cls.getSuperClass();
			if(superClassList != null)
			{
				for(Class superClass : superClassList)
				{
					if( is_skip_package(skipList, superClass.getPackageName())) continue;
					str.append("\t"+cls.getSrcName() + "-->" + superClass.getSrcName() +": "+cls.getSrcName()+" is "+superClass.getSrcName()+"\n");
				}
			}
		}
	}
	private static void classDef(DataList list, StringBuilder str) {
		// TODO Auto-generated method stub
		ArrayList<Class> classList = list.getClassList();
		for(Class cls : classList)
		{	
			if ( is_skip_package(skipList, cls.getPackageName()) ) continue;
			
			String name = cls.getSrcName();
			ClassComplexity complex  = cls.getComplex();
			String color = " ";
			if(3 <= complex.getRES() )
				color = " #back:salmon";
			else if( complex.getRES() <2)
				color = " #line:green";
			
			if(cls.isAbstract()) str.append("abstract ");
			else if(cls.isInterface()) str.append("interface ");
			else str.append("class ");

			str.append(name + " \n{\n");
			//str.append(name + color +  " \n{\n");
			
			DataList hasList = cls.getHasList();
			
			ArrayList<Member> varList = hasList.getMemberList();
			for(Member value : varList )
			{	
				str.append("\t"+value.getTypeName()+" "+value.getName()+"\n");
				
			}
			
			ArrayList <Function> functionList = hasList.getFunctionList();
			for(Function fun : functionList )
			{
				str.append("\t"+fun.getTypeName()+" "+fun.getSig());
				str.append(";\n");
			}
			str.append("}\n\n");	
			
		}
		
	}

}
