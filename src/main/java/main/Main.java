package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import complexity.PackageCp;
import dataSet.DataList;
import dev.khe.DiagramRecover.PrintUtil;
import diagram.UsecaseDg2;
import diagramMaker.ClassDiagram;
import diagramMaker.ClassDiagram2;
import diagramMaker.ClassDiagram3;
import diagramMaker.ComplexDiagram;
import diagramMaker.DataListDiagram2;
import diagramMaker.DataListDiagram3;
import diagramMaker.Sequence;
import diagramMaker.SequenceDeep;
import diagramMaker.callGraph.CallGraph;
import diagramMaker.callGraph.GraphStructure;
import diagramMaker.callGraph.GraphStructure_CF;
import diagramMaker.stateDiagram.ForQuiz4;
import diagramMaker.stateDiagram.NodeClassDg;
import diagramMaker.usecase.UseCase_C_CC_HF;
import diagramMaker.usecase.UseCase_C_CF_CC;
import modelManage.DataMapper;
import xmiManage.ModelManager;

public class Main {
	public final static String WIN = "\\";
	public final static String MAC = "/";
	static String os = WIN;
	private static void addJavaParserLib(BufferedWriter writer) throws IOException
	{
		File dir = new File("lib/javaParser");
		String path = dir.getAbsolutePath();
		String list[] = dir.list();
		for(String str : list)
			writer.write(path+os+str+"\n");
	
	}
	public static void makeInitFile() throws IOException
	{
		System.out.println("기본 target_projcet.info 파일 생성...\n");
		File file = new File("target_project.info");
		String src = file.getAbsolutePath().replace(os+"target_project.info", "");
		String str = src+"/target/src\n"+src+"/lib/org.eclipse.equinox.launcher_1.5.700.v20200207-2156.jar\n";
		System.out.println(src+"/target_projcet.info 파일 생성 됨\n");
		//System.out.println(str);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(str);
		addJavaParserLib(writer);
		writer.close();	
	}
	public static void testUML() throws IOException
	{
		Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg result/classDg3.txt");
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintUtil.printlnInfomation("Make Test diagram");
	}
	public static void drawMain(DataList datalist) throws IOException, InterruptedException 
	{		
		//DataList targetList = new DataList();
		//targetList.addData(datalist.getClassMap().get("snavigator.SN_Dumper"));
		//ForQuiz4.main(datalist);
		//StateNodeDiagram.main(datalist);
		
		//CallGraph.main(datalist);
		//ClassDiagram.main(datalist);
		//ClassDiagram2.main(datalist);
		//ClassDiagram3.main(datalist);
		//DataListDiagram.draw(datalist);
		DataListDiagram2.draw(datalist);
		//DataListDiagram3.draw(datalist);
		//StateNodeDiagram.main(datalist);
		//GraphStructure.main(datalist);
		//GraphStructure_CF.main(datalist);
		//Sequence.main(datalist);
		//SequenceDeep.main(datalist);
		//NodeClassDg.main(datalist);
		//UseCase_C_C_F.main(datalist);
		//UseCase_C_F_C.main(datalist);
		//UseCase_C_F_F.main(datalist);
		//UseCase_C_CC_HF.main(datalist);
		//UseCase_C_CF_CC.main(datalist);
		//UseCase.main(datalist);
		//ForQuiz4.main(datalist);
		ComplexDiagram.main(datalist);
	}
	
	//DiagramForJara
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		makeInitFile();	
		Parser.initialize(new File("target_project.info"));
		DataList datalist = Parser.makeDataList();
		drawMain(datalist);
		PackageCp pcp = new PackageCp(datalist);
		pcp.printScore();
		
		ModelManager mmg = new ModelManager("Dice.swvs");
		DataMapper mapper = new DataMapper(datalist, mmg);
		UsecaseDg2.main(mmg.project);
		
		
		//ClassDg.main(new XmiReade)
		//testUML();
		System.out.println("DiagramForJava's main end");
		 
	}

}