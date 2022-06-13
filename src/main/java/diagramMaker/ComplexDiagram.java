package diagramMaker;
/*
 * Complex Print Dg
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import complexity.ClassComplexity;
import complexity.FunctionComplexity;
import complexity.HalsteadComplex;
import complexity.StateNodeComplex;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import javassist.scopedpool.ScopedClassPool;
import dataSet.Class;

public class ComplexDiagram
{
	public static final int passMax = 4;
	public static final int failMin = 9;
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
	private static String makeDateFolder() 
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time =  format.format(date);
		String src = "result/datalist/complex/"+time;
		File folder = new File(src);
		folder.mkdir();
		return time;
	}
	public static void main(OMS list) throws IOException, InterruptedException
	{
		String time = makeDateFolder();
		String folderSrc = "result/datalist/complex/"+time;
		HashMap<String, Class> classMap = list.getClassMap();							
		for(String key : classMap.keySet())
		{
			Class target = classMap.get(key);
			String packageName = target.getPackageName();
			
			if ( is_skip_package(skipList, packageName) ) 
				continue;
			else
			{
				String script = makeScript(target);
				String fileSrc = folderSrc+"/"+key+".txt";
				makeFile(script, fileSrc);
				String cmd = "java -jar lib/plantuml.jar -tsvg "+fileSrc;
				Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
				System.out.println(cmd);	
			}
		}	
	}

	public static void makeFile(String script, String src) throws IOException
	{
		File file = new File(src);
		FileWriter fw = new FileWriter(file, false);
		fw.write(script);
		fw.close();	
	}
	
	//클래스 작성 스크립트 메인
	public static String makeScript(Class cls)
	{
		StringBuilder str= new StringBuilder("@startuml\n");
		defClassComplexInfo(cls, str);
		str.append("\n");
		for(Function fun : cls.getHasList().getFunctionList() )
		{
			defFunctionComplexInfo(fun, str);
		}
		str.append("\n@enduml");
		return str.toString();
	}
	private static void defFunctionComplexInfo(Function fun, StringBuilder str) {
		FunctionComplexity complex = fun.getComplex();
		String tab = "\t";
		String color = "";
		if(failMin <= complex.getAbsComplex())
			color = "#back:salmon";
		else if( complex.getABS() <=passMax)
			color = "#line:green";
		String name = fun.getSig();
		String hashCode = fun.getOwner().getSrcName()+'.'+fun.getName()+fun.hashCode();
		str.append("class " + hashCode+" as "+'"'+name+'"' + color +" \n{\n");
		
		//test
		str.append(tab+"pram : "+fun.getParameterName()+"\n");
		str.append(tab+"SrcOwnClassName : "+fun.getOwner().getSrcName()+"\n");
		str.append(tab+"SrcSig : "+fun.getSrcName()+"\n");
		str.append(tab+"Sig : " + fun.getSig()+"\n");
		
		str.append(tab+"__**<color:orange>Complex Info</color>**__\n");
		str.append(tab+"parameter Score : "+complex.getParameterScore()+"\n");
		str.append(tab+"classCall Score : "+complex.getClassCallScore()+"\n");
		str.append(tab+"Call Score : "+complex.getCallScore()+"\n");
		str.append(tab+"CallBy Score : "+complex.getCallByScore()+"\n");
		str.append(tab+"memberUse Score : "+complex.getMemberUseScore()+"\n");
		str.append(tab+"localValue Score : "+complex.getLocalValueScore()+"\n");
		str.append(tab+"exp Score : "+complex.getExpScore()+"\n");
		str.append(tab+"repeat Score : "+complex.getRepeatScore()+"\n");
		str.append(tab+"brench Score : "+complex.getBrenchScore()+"\n");
		str.append(tab+"callCountNum : "+complex.getCallCountNum()+"\n");
		str.append(tab+"cyclomatic : "+complex.getCyclomatic()+"\n");
		
		StateNodeComplex scp = complex.getStatNodeComplex();
		str.append(tab+"State Node lineNum: "+scp.lineNum+"\n");
		str.append(tab+"State Node Num : "+scp.nodeNum+"\n");
		str.append(tab+"State Node callListSize: "+scp.callSize+"\n");
		str.append(tab+"State Node repeatSize : "+scp.repeatSize+"\n");
		str.append(tab+"State Node writeSize : "+scp.writeSize+"\n");
		str.append(tab+"State Node bifurSize : "+scp.bifurSize+"\n");
		DecimalFormat form = new DecimalFormat("#.####");
		HalsteadComplex hcp = complex.getHalstead();
		str.append(tab+"Calculated Estunated Program Length: "+form.format(hcp.cepl)+"\n");
		str.append(tab+"Volume : "+form.format(hcp.volume)+"\n");
		str.append(tab+"Diffifculty: "+form.format(hcp.difficulty)+"\n");
		str.append(tab+"Effort : "+form.format(hcp.effort)+"\n");
	
		
		
		
		str.append(tab+"Absolute Score : "+complex.getABS()+"\n");
		str.append(tab+"Relative Score : "+complex.getRES()+"\n");
		str.append("}\n");
		
	}
	private static void defClassComplexInfo(Class cls, StringBuilder str) {
		ClassComplexity cp = cls.getComplex();
		String tab = "\t";
		str.append("class ClassComplex{\n");
		str.append("__**<color:orange>Complex Info</color>**__\n");
		str.append(tab+"memberScor = "+cp.getMemberScore()+"\n");
		str.append(tab+"extendsClassScore = "+cp.getExtendClassScore()+"\n");
		str.append(tab+"callScore = "+cp.getCallScore()+"\n");
		str.append(tab+"callByScore = "+cp.getcallByScore()+"\n");
		str.append(tab+"methodNum = "+cp.getMethodNum()+"\n");
		str.append(tab+"memberNum = "+cp.getMemberNum()+"\n");
		str.append(tab+"constructorNum = "+cp.getConstructorNum()+"\n");
		str.append(tab+"selfCoverage = "+cp.getSelfCoverage()+"\n");
		str.append(tab+"needCallBy = "+cp.getNeedCallBy()+"\n");
		str.append(tab+"Absolute Score = "+cp.getABS()+"\n");
		str.append(tab+"Relative Score = "+cp.getRES()+"\n");
		

		
		
		int call = cp.getSelfCoverage();
		int needCall = cp.getNeedCallBy();
		int initNum = cp.getConstructorNum();
		float coverage = 0;
		if(0<(call+needCall))
			coverage = (100*call)/(call+needCall);
		String p = Float.toString(coverage);
		str.append(tab+"coverage = "+p+"%\n");
		/*
		if(0 < call+needCall-initNum)
		{
			coverage = (100*call)/(call+needCall-initNum);
			p = Float.toString(coverage);
			str.append(tab+"coverage(!constructor) = "+p+"%\n");
		}
		*/
		
		
		OMS haslist = cls.getHasList();
		str.append("__**<color:green>has list</color>**__\n");
		for(Member member : haslist.getMemberList())
		{
			String color = "<color:green>";
			int abs = member.getComplex().getABS();
			if(2 < abs)
				color ="<color:red>";
			String info = color+member.getTypeName()+" "+member.getName()+" ("+abs+")";
			str.append(tab+info+"\n");
			
		}
		for(Function fun : haslist.getFunctionList())
		{
			int cpLv = fun.getComplex().getAbsComplex();
			String color = "<color:orange>";	//salmon
			if(failMin <= cpLv)
				color = "<color:red>";
			else if( cpLv <= passMax)
				color = "<color:green>";
			String info = color+fun.getReturnType()+" "+fun.getSig()+" ("+cpLv+")";
			str.append(tab+info+"\n");
		}
		str.append("}\n");
		
		
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


}


