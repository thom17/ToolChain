package diagramMaker.usecase;

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
public class UseCase_C_F_F extends Base
{
	static Stack<String> stack;
	private static String makeDateFolder() 
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		String time =  format.format(date);
		String src = "result/usecase/"+time;
		File folder = new File(src);
		folder.mkdir();
		return time;
	}
	
	public static void main(DataList datalist) throws IOException
	{
		String time=makeDateFolder();
		String src = "result/usecase/"+time+"/C-F-F";
		File folder = new File(src);
		folder.mkdir();
		Map<Class, ArrayList<Function>> seperateFiles = seperateFile(datalist);
		
		for(Class actor : seperateFiles.keySet())
		{	
			stack = new Stack<String>();
			String script = draw(actor ,seperateFiles.get(actor));
			String fileSrc = src+"/"+actor.getSrcName()+".txt";
			makeFile(script, fileSrc);
			
		}
		
		
	
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

	private static String draw(Class actor, ArrayList<Function> arrayList) 
	{
		StringBuilder sb = new StringBuilder("@startuml\n left to right direction\n\n");
		String id = "actor"+getHash(actor, "_");
		sb.append("actor "+actor.getName() + " as "+ id +"\n\n");
		Stack<Function> usecaseStack = new Stack<Function>();
		for(Function system : arrayList)
		{
			sb.append("\trectangle "+system.getName()+"{\n");
			getUseCase(usecaseStack, system, actor, sb);
			sb.append("\t}\n");
			while( !usecaseStack.isEmpty() )
			{
				Function usecase = usecaseStack.pop();
				String usecaseId ="UC"+getHash(usecase, "_");
				String usecaseActor = "actor"+getHash(usecase.getOwner(), "_");
				sb.append("\tactor "+usecase.getOwner().getName()+" as "+usecaseActor+"\n");
				sb.append("\t "+usecaseId+" -- "+usecaseActor+"\n");
			}
		}
			
			
		sb.append("\n");
		//drawSystem(arrayList, sb);
		sb.append("@enduml");
		return sb.toString();
	}

	private static void getUseCase(Stack<Function> usecase, Function system, Class actor, StringBuilder sb) 
	{
		for(Function client : system.getCallList().getFunctionList())
		{
			Data_base owner = client.getOwner();
			
			while(owner instanceof Function)
				owner = ((Function) owner).getOwner();
			
			if(actor != owner)
			{
				String actorId = "actor"+getHash(actor, "_");
				String id = "UC"+getHash(client, "_");
				sb.append("\t usecase "+client.getName()+" as "+id+"\n");
				sb.append("\t"+actorId+"--"+id+"\n");
				usecase.add(client);
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
