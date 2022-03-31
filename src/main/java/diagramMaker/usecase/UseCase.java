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
public class UseCase extends Base
{
	static Stack<String> stack;
	private static String makeDateFolder() 
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String time =  format.format(date);
		String src = "result/usecase/"+time;
		File folder = new File(src);
		folder.mkdir();
		return time;
	}
	
	public static void main(DataList datalist) throws IOException
	{
		String time=makeDateFolder();
		drawPackageLv(time, datalist);
	}
	private static void drawPackageLv(String time, DataList datalist) throws IOException {
	
		String src = "result/usecase/"+time+"/packageLv";
		File folder = new File(src);
		folder.mkdir();
		Map< String, ArrayList<Class> > packageSet = datalist.separatePackage();
		
		for(String packageName : packageSet.keySet())
		{	
			stack = new Stack<String>();
			String script = packageLvScript(packageSet.get(packageName));
			String fileSrc = src+"/"+packageName+".txt";
			makeFile(script, fileSrc);
			
		}
		
		
	}

	private static String packageLvScript(ArrayList<Class> arrayList) 
	{	
		StringBuilder sb = new StringBuilder("@startuml\n left to right direction\n\n");
	
		for(Class actor : arrayList)
		{
			String id = "actor"+getHash(actor, "_");
			sb.append("actor "+actor.getName() + " as "+ id +"\n\n");
			addPackage(actor, "\t", sb);
			
		}
		sb.append("\n");
		addSuperClass(arrayList, sb);
		sb.append("@enduml");
		return sb.toString();
	}
	private static void addSuperClass(ArrayList<Class> arrayList, StringBuilder sb) 
	{
		for(Class actor : arrayList)
		{
			String id = "actor"+getHash(actor, "_");
			if(actor.getSuperClass() != null)
			{
				for (Class cls : actor.getSuperClass() )
				{
					String superId = "actor"+getHash(cls,"_");
					sb.append(id+"->"+superId+"\n");
				}
			}
		}
		
	}

	private static Map<String, ArrayList<Function>> makeUseCase(Class actor)
	{
		Map<String, ArrayList<Function>> map = new HashMap<String, ArrayList<Function>>();
		for(Function fun  : actor.getCallList().getFunctionList())
		{
			if(fun.getOwner() != actor)
			{
				String packageName = fun.getPackageName();
				ArrayList<Function> funList = map.get(packageName);
				if(funList == null)
				{
					funList = new ArrayList<Function>();
					map.put(packageName, funList);
				}
				funList.add(fun);
			}
		}
		return map;
	}
	private static void addPackage(Class actor, String tab, StringBuilder sb) 
	{
		String intab = tab+"\t";		
	//	Map<String, ArrayList<Class> > callSystemMap = actor.getCallList().separatePackage();
		Map<String, ArrayList<Function>> systemMap = makeUseCase(actor);
		for(String systemName : systemMap.keySet())
		{
			sb.append(tab+"package "+systemName+" {\n");
			for(Function callFun : systemMap.get(systemName))
			{
				String caseName = callFun.getName();
				String caseId = "UC"+getHash(callFun , "_");
				stack.add(caseId);
				sb.append(intab+"usecase "+caseName+" as "+caseId+"\n");
			}	
			sb.append(tab+"}\n");
			while( !stack.empty() )
			{
				String actorId = "actor"+getHash(actor, "_");
				sb.append(tab+actorId+" -- "+stack.pop()+"\n");
			}
		}
		
		
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
