package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.DataKey;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.google.common.collect.ArrayListMultimap;

import complexity.ClassComplexity;
import dataSet.Class;
import dataSet.DataList;
import dataSet.Function;
import dataSet.statement.StateNode;
import dev.khe.DiagramRecover.PrintUtil;
import dev.khe.DiagramRecover.TargetProjectInfomationReader;
import dev.khe.DiagramRecover.model.ModelManager;
import visitor.DataMaker;
import visitor.FunctionMaker;
import visitor.StateNodeMaker;

public class Parser {
	private static JavaParser parser = null;
	private static List<File> javaFileList = null;
	private static final String STR_END_WITH_JAVA = ".java";
	public static void simpleInit()
	{
		TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
		ParserConfiguration parserConf = new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
		parser = new JavaParser(parserConf);
	}
	public static void initialize(File file) throws IOException 
	{
		// 분석대상 프로젝트 설정파일 읽기
		TargetProjectInfomationReader tpir = new TargetProjectInfomationReader();
		tpir.read(file);

		// 분석대상 프로젝트의 java 파일 목록 초기화
		javaFileList = new ArrayList<File>();
		for (String srcDirPath : tpir.getSrcPathList()) {
			addJavaFile(new File(srcDirPath));
		}

		// JavaParser 초기화
		TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
		for (String srcPath : tpir.getSrcPathList()) {
			((CombinedTypeSolver) typeSolver).add(new JavaParserTypeSolver(new File(srcPath)));
			PrintUtil.printlnInfomation("Add JavaParserTypeSolver(" + srcPath + ") to CombinedTypeSolver");
		}
		for (String jarPath : tpir.getJarPathList()) {
			((CombinedTypeSolver) typeSolver).add(new JarTypeSolver(new File(jarPath)));
			PrintUtil.printlnInfomation("Add JarTypeSolver(" + jarPath + ") to CombinedTypeSolver");
		}

		ParserConfiguration parserConf = new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
		parser = new JavaParser(parserConf);
	}

	/**
	 * javaFileList 초기화를 위해 호출되는 메소드이다.<br>
	 * 재귀적으로 인자(file)로 주어진 디렉토리 하위를 탐색하여 <br>
	 * *.java 파일을 발견하고 javaFileList에 추가한다.<br>
	 * initialize(File file) 메소드에서만 호출되어야한다.
	 * 
	 * @param file
	 */
	private static void addJavaFile(File file) {
		if (file.isDirectory()) {
			for (File nextFile : file.listFiles()) {
				addJavaFile(nextFile);
			}
		} else {
			if (file.getName().toLowerCase().endsWith(STR_END_WITH_JAVA)) {
				javaFileList.add(file);
				PrintUtil.printlnInfomation("Add java file \"" + file.getAbsolutePath() + "\"");
			}
		}
	}
	public static ArrayList<CompilationUnit> makeASTList() throws FileNotFoundException
	{
		ArrayList<CompilationUnit> list = new ArrayList<CompilationUnit>();
		CompilationUnit compUnit = null;

		for (File javaFile : javaFileList) 
		{
			PrintUtil.printlnInfomation("Start parse \"" + javaFile.getAbsolutePath() + "\"");
			compUnit = parser.parse(ParseStart.COMPILATION_UNIT, new StreamProvider(new FileReader(javaFile)))
					.getResult().get();
			list.add(compUnit);
		}

		return list;
	}
	
	public static DataList makeDataList() throws FileNotFoundException
	{
		ArrayList<CompilationUnit> list = Parser.makeASTList();
		
		DataList datalist = new DataList();
		DataMaker dataMaker = new DataMaker(datalist);
		ArrayList<String> classList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		
		//step 1 : make Data_base Structure
		for(CompilationUnit ast : list)
		{
			ast.accept(dataMaker, sb);
			classList.add(sb.toString());
			sb.delete(0, sb.length());
		}

		StateNodeMaker funMaker;
		
		//Step 2 : make Connects
		for(Function fun : datalist.getFunctionList() )
		{
			funMaker = new StateNodeMaker(fun, datalist);

		}
		
		//Step 3 : Make Complex (Top-Down)
		for( Class cls : datalist.getClassList() )
		{
			cls.setComplexity();
		}
		
		return datalist;
	}
	

	public static void main(String[] args) {
		JavaParser parser = new JavaParser();
		ParseResult<CompilationUnit> op = parser.parse("int x");
		Optional unit = op.getResult();
		Object u = unit.get();
		System.out.println("?");
	}

}
