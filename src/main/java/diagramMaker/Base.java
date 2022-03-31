package diagramMaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import complexity.FunctionComplexity;
import dataSet.Class;
import dataSet.DataList;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;

public abstract class Base {
	public static void main(String test[]) throws IOException
	{
		String target = "result/test/test.txt";
		String cmd = "java -jar lib/plantuml.jar -tsvg " +target;
		System.out.println("cmd : "+cmd);
		Process p = Runtime.getRuntime().exec(cmd);

		printLog(p);
	}
	protected static String makeDateFolder(String path) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time =  format.format(date);
		String src = path+"/"+time;
		File folder = new File(src);
		folder.mkdir();
		return time;
	}
	
	protected static void makeFile(String script, String src) throws IOException
	{
		src=src.replace("<", "(").replace(">", ")");
		System.out.println("make File "+src);
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
		
		String cmd = "java -jar lib/plantuml.jar -tsvg " +src;
		System.out.println("cmd : "+cmd);
		Process p = Runtime.getRuntime().exec(cmd);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printLog(p);
	}
	private static void printLog(Process p) throws IOException
	{
		p.getInputStream();	// p가 출력 한 스트림 (부모가 입력 받을 스트림)
		p.getOutputStream(); // p에 입력 할 스트림 (부모가 내보낼 스트림)
		
		//p의 Stream을 읽을 StreamReader 생성
		InputStreamReader streamReader = new InputStreamReader(p.getInputStream());
		//읽어온 Stream을 담을 buffer 생성
		BufferedReader br = new BufferedReader(streamReader);
	
		String line;
		//라인 단위로 버퍼 비우며 읽기 
		while ((line = br.readLine()) != null)	
		{
			System.out.println(line);
		}
	}
	protected static String getHash(Object obj, String add)
	{
		int code = obj.hashCode();
		if(0 < code)
			return ""+code;
		else
			return add+(code*-1);
	}
	public static Map<String, ArrayList<Class>> separatePackage(ArrayList<Class> classList)
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
}
