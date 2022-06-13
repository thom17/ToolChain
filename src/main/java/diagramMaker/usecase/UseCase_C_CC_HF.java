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

import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dev.khe.DiagramRecover.PrintUtil;
import diagramMaker.Base;
import dataSet.Class;
public class UseCase_C_CC_HF extends Base
{
	static int idNum = 0;
	static Stack<String> stack;
	public static void main(String args[])
	{
		String target = "test/C_CC_HF.txt";
		String cmd = "java -jar lib/plantuml.jar -tsvg " +'"' +target+'"';
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
	
	public static void main(OMS datalist) throws IOException
	{
		String time=makeDateFolder();
		String src = "result/usecase/"+time+"/C-CC-HF";
		File folder = new File(src);
		folder.mkdir();
		Map<String , ArrayList<Class>> separate = separatePackage(datalist.getClassList());
		for(String packageName : separate.keySet())
		{
			stack = new Stack<String>();
			String script = draw(separate.get(packageName));
			String fileSrc = src+"/"+packageName+".txt";
			makeFile(script, fileSrc);	
		}
	}
	
	

	private static String draw(ArrayList<Class> actorList) 
	{
		StringBuilder sb = new StringBuilder("@startuml\n left to right direction\n\n");
		actorList = deleteNotCaller(actorList);
		Stack<Class> systemStack = new Stack<Class>();
		for(Class actor : actorList)
		{
			String id = "actor"+getHash(actor,"_");
			sb.append("actor "+actor.getName()+ " as "+id+"\n");
			for( Class cls : actor.getCallList().getClassList() )
			{
				if( !systemStack.contains(cls) )
				{
					systemStack.add(cls);
				}
			}
		}
		while( !systemStack.empty())
		{
			Class system = systemStack.pop();
			sb.append("rectangle "+system.getName()+"{\n");
			getUseCase(system, actorList, sb);
			sb.append("}\n");
		}
		sb.append("@enduml");
		return sb.toString();
	}

	private static void getUseCase(Class system, ArrayList<Class> actorList, StringBuilder sb) {
		// TODO Auto-generated method stub
		for(Function fun : system.getHasList().getFunctionList() )
		{
			//def Usecase
			String tab = "\t";
			String ucId = "UC"+getHash(fun,"_");
			sb.append(tab+"usecase "+fun.getName()+" as "+ucId+"\n");
			Stack<Class> connectList = new Stack<Class>();
			for( Class cls : fun.getCallByList().getClassList())
			{
				if(actorList.contains(cls))
					connectList.add(cls);
			}
			
			//def Actor--usecase
			if(connectList.isEmpty())
				continue;
			else 
			{
				while(!connectList.isEmpty())
				{
					String actorId = "actor"+getHash(connectList.pop(),"_");
					sb.append("\t"+tab+actorId+" -- "+ucId+"\n");
				}
			}
		}
	}

	private static ArrayList<Class> deleteNotCaller(ArrayList<Class> actorList) 
	{
		ArrayList<Class> list = new ArrayList<Class>();
		for(Class cls:actorList)
		{
			if ( 0 < cls.getCallList().getClassList().size() )
				list.add(cls);
		}
		return list;
	}
}
