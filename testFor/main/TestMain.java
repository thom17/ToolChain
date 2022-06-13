package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.ClassOrInterfaceDeclarationMetaModel;

import dataSet.Class;
import dataSet.Function;
import dataSet.Member;
import dev.khe.DiagramRecover.PrintUtil;
import diagramMaker.DataListDiagram2;
import testDataSet.Datalist;
import dataSet.OMS;

public class TestMain {
	public static void makeInitFile() throws IOException
	{
		File file = new File("test_project.info");
		String src = file.getAbsolutePath().replace("\\test_project.info", "");
		String str = src+"\\target\\src\n"+src+"\\lib\\org.eclipse.equinox.launcher_1.5.700.v20200207-2156.jar";
		System.out.println(str);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(str);
		writer.close();
		
		
	}

	public static void testUML() throws IOException
	{
		Runtime.getRuntime().exec("java -jar lib/plantuml.jar StateTest.uml");
		PrintUtil.printlnInfomation("Make Test diagram");
	}
	
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		makeInitFile();	//초기 실행시 해당 코드를 실행하면 자동으로 설정 파일을 작성해줌.
		
		TestParser.initialize(new File("target_project.info"));
		System.out.println("Parser initialize Finish");
		
		Datalist datalist = new Datalist();
		
		ArrayList<CompilationUnit> list = TestParser.makeASTList();
		
		for(CompilationUnit ast : list)
		{
			ast.accept(new TestVisitor(datalist), new StringBuilder());
		}
		
		DataListDiagram.main(datalist);
		
		System.out.println("DataList Make Finish");
		//DataListDiagram2.main(datalist);
		
		//testUML();
	}

}
