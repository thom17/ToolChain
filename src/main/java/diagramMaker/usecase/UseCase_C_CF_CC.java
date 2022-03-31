package diagramMaker.usecase;
//C-F-C 기반
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import dataSet.DataList;
import dataSet.Data_base;
import dataSet.Function;
import dev.khe.DiagramRecover.PrintUtil;
import diagramMaker.Base;
import dataSet.Class;
public class UseCase_C_CF_CC extends Base
{
	static int idNum=0;
	static Stack<String> stack;
	static DataList datalist;
	public static void main(String args[])
	{
		String target = "test/C_CF_CC.txt";
		String cmd = "java -jar lib/plantuml.jar -tsvg "  +target;
		System.out.println("cmd : "+cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static String makeDateFolder() 
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time =  format.format(date);
		String src = "result/usecase/"+time;
		File folder = new File(src);
		folder.mkdir();
		return time;
	}
	
	public static void main(DataList datalists) throws IOException
	{
		String time=makeDateFolder();
		String src = "result/usecase/"+time+"/C_CF_CC";
		File folder = new File(src);
		folder.mkdir();
		UseCase_C_CF_CC.datalist = datalists;
		stack = new Stack<String>();
		String script = draw(datalist.getClassList() , stack);
		String fileSrc = src+"/C_CF_CC.txt";
		makeFile(script, fileSrc);
	}
	public Map<String, ArrayList<Class>> separateFile(ArrayList<Class> classList)
	{
		Map<String, ArrayList<Class>> map = new HashMap<String, ArrayList<Class>>();
		for(Class cls : classList)
		{
			String packageName = cls.getPackageName();
			ArrayList<Class> list = map.get(packageName);
			if(list == null)
			{
				list = new ArrayList<Class>();
				map.put(packageName, list);
			}
			list.add(cls);
		}
		
		return map;
	}

	private static String draw(ArrayList<Class> classList, Stack<String> stack) 
	{
		StringBuilder sb = new StringBuilder("@startuml\n left to right direction\n\n");
		for(Class cls : classList)
		{
			String id = "actor"+getHash(cls, "_");
			sb.append("actor "+cls.getName() + " as "+ id +"\n");
			for(Function fun : cls.getCallList().getFunctionList())
			{
				if(!stack.contains( fun.getSrcName()) )
					stack.add(fun.getSrcName());
				else
					continue;
			}
		}
		for(String src : stack)
		{
			Function system = datalist.findFunction(src);
			String funId = "fun"+getHash(system, "_");
			sb.append("\trectangle \""+system.getName()+"\" as "+funId+"{\n");
			getUseCase(system, sb);
			sb.append("\t}\n");
		}
			
			
		sb.append("\n");
		//drawSystem(arrayList, sb);
		sb.append("@enduml");
		return sb.toString();
	}

	private static void getUseCase(Function system, StringBuilder sb) 
	{
		for(Class usecase : system.getCallList().getClassList())
		{
			String ucId = "UC"+(idNum++);
			sb.append("\tusecase \""+usecase.getName()+"\" as "+ucId+"\n");
			for(Class actor : usecase.getCallByList().getClassList())
			{					
				String actorId = "actor"+getHash(actor, "_");
				sb.append("\t\t"+actorId+"--"+ucId+"\n");
			}
		}
	}

	private static Map<Class, ArrayList<Function>> seperateFile(DataList datalist) 
	{
		Map<Class, ArrayList<Function>> map = new HashMap<Class, ArrayList<Function> >();
		for(Class actor : datalist.getClassList() )
		{
			for(Function system : actor.getHasList().getFunctionList() )
			{
				if( 0 < system.CallOtherClass())
				{
					ArrayList<Function> funlist = map.get(actor);
					if(funlist == null)
					{
						funlist = new ArrayList<Function>();
						map.put(actor, funlist);
					}
					funlist.add(system);
				}
			}
		}
		
		return map;
	}



/*
	private static void addPackage(Class actor, String tab, StringBuilder sb) 
	{
		String intab = tab+"\t";
		Map<String, ArrayList<Class> > callSystemMap = actor.getCallList().separatePackage();
		for(String systemName : callSystemMap.keySet())
		{
			sb.append(tab+"package "+systemName+" {\n");
			for(Data_base usecase : callSystemMap.get(systemName))
			{
				String caseName = usecase.getName();
				String caseId = "UC"+getHash(usecase , "_");
				stack.add(caseId);
				sb.append(intab+"usecase "+caseName+" as "+caseId+"\n");
			}	
			sb.append("}\n");
		}
		
		
	}
	*/




}
