package diagramMaker;
/**3사용 권장
 * CLASS DG Composistion 등 추가해봄 (첫버전=diagram1의 개선 잘안됨)
 * 그래서 생성자 기준으로 수정해봄
*/
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import complexity.ClassComplexity;
import dataSet.OMS;
import dataSet.Function;
import dataSet.Member;
import dataSet.Class;

public class ClassDiagram3 
{
	static String skipList[] = {
			"java.lang",
		//	"java.util",
			"java.io" ,
			"shopTest"
	};
	
	public static void main(OMS list) throws IOException, InterruptedException
	{
		makeFile( makeString(list) );
		Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/classDg3.txt");
		p.waitFor();
		System.out.println("ClassDiagram draw done : classDg3");
	}
	public static void makeFile(String str) throws IOException
	{
		File file = new File("result/classDg3.txt");
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
	
	public static String makeString(OMS list)
	{
		StringBuilder str= new StringBuilder("@startuml\n");
		
		str.append("/'do Class Define (클래스 정의)'/\n");
		classDef(list, str);
		str.append("/'Class Define (클래스 정의) end'/\n\n");
		
		str.append("/'do Extends Define (상속 관계 정의)'/\n");
		classExtends(list, str);
		str.append("/'Extends Define (상속 관계 정의) end'/\n\n");
		
		str.append("/'do Has Relation Define (집합 관계 정의)'/\n");
		hasByDef(list, str);
		str.append("/'end Has Relation Define (집합 관계 정의)'/\n\n");
		
		str.append("/'do call By Define-Class (클래스-클래스 호출 관계 정의)'/\n");
		callByDef(list, str);
		str.append("/'call By Define-Class (클래스-클래스 호출 관계 정의)' end/\n\n");
		
		
		//str.append( callDef(list, str) );
		str.append("\n@enduml");
		return str.toString();
	}

	private static void hasByDef(OMS list, StringBuilder str) 
	{
		for(Class cls : list.getClassList())
		{
			if(is_skip_package(skipList, cls.getPackageName())) 
				continue;
			
			for(Class client : cls.getHasList().getClassList())
			{
				if(is_skip_package(skipList, cls.getPackageName())) 
					continue;
				
				for(Function fun : client.getConstructors())
				{
					if(fun.getCallByList().checkHasOtherClass(cls) ) //다른 클래스에서 해당 생성자 사용함.
					{//Aggregation
						str.append("\t"+cls.getSrcName()+" o-- "+client.getSrcName()+" :"+cls.getName()+" has "+ client.getName()+"\n");
					}else
					{//Composition
						str.append("\t"+cls.getSrcName()+" *-- "+client.getSrcName()+" :"+cls.getName()+" has(Composition) "+ client.getName()+"\n");
					}
				}
			}			
			
			
		}
		
	}
	private static void callByDef(OMS list, StringBuilder str ) 
	{
		
		ArrayList<Class> classList = list.getClassList();
		for(Class cls : classList)
		{
			if(is_skip_package(skipList, cls.getPackageName())) continue;
			
			OMS callList = cls.getCallList();
			ArrayList<Class> clientList = callList.getClassList();
			for(Class client : clientList)
			{
				if( is_skip_package(skipList, client.getPackageName()) || client == cls) 
					continue;
				else if(cls.getHasList().findClass(client.getSrcName()) == null)
					str.append("\t"+cls.getSrcName()+" --> "+client.getSrcName()+" : "+cls.getName()+" use "+ client.getName()+"\n");
				
			}	
		}
		str.append("\n\n");
	}
	private static void classExtends(OMS list, StringBuilder str)
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
					str.append("\t"+cls.getSrcName() + "--|>" + superClass.getSrcName() +": "+cls.getName()+" is "+superClass.getName()+"\n");
				}
			}
		}
	}
	private static void classDef(OMS list, StringBuilder str) {
		// TODO Auto-generated method stub
		ArrayList<Class> classList = list.getClassList();
		for(Class cls : classList)
		{	
			if ( is_skip_package(skipList, cls.getPackageName()) ) continue;
			
			String name = cls.getSrcName();
			ClassComplexity complex  = cls.getComplex();
			String color = " ";
			if(ComplexDiagram.failMin <= complex.getRES() )
				color = " #back:salmon";
			else if( complex.getRES() <= ComplexDiagram.passMax)
				color = " #line:green";
			
			if(cls.isAbstract()) str.append("abstract ");
			else if(cls.isInterface()) str.append("interface ");
			else str.append("class ");

			str.append(name + color+" \n{\n");
			//str.append(name + color +  " \n{\n");
			
			OMS hasList = cls.getHasList();
			
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
