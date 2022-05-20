package dataListTester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import complexity.HalsteadComplex;
import dataSet.DataList;
import dataSet.Function;
import main.Parser;

public class GetterComplex 
{
	public final static String WIN = "\\";
	public final static String MAC = "/";
	static String os = WIN;
	public static Map<String,  HalsteadComplex> halSet;
	public static Map<String, HalsteadComplex> getHalsteadComplex(File file) throws FileNotFoundException
	{
		halSet = new HashMap<String, HalsteadComplex>();
		try {
			Parser.initialize(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataList datalist = Parser.makeDataList();
		for(Function fun : datalist.getFunctionList())
		{
			HalsteadComplex cp = fun.getComplex().getHalstead();
			String src = fun.getSrcName();
			halSet.put(src, cp);
		}
		return halSet;
	}
	public static void main(String args[])
	{
		System.out.println("기본 target_projcet.info 파일 생성...\n");
		File file = new File("target_project.info");
		String src = file.getAbsolutePath().replace(os+"target_project.info", "");
		String str = src+"/target/src\n"+src+"/lib/org.eclipse.equinox.launcher_1.5.700.v20200207-2156.jar\n";
		System.out.println(src+"/target_projcet.info 파일 생성 됨\n");
		//System.out.println(str);
		
		
	}
}
