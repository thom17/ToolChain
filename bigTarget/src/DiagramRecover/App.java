package dev.khe.DiagramRecover;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import dev.khe.DiagramRecover.model.ModelManager;

/**
 * Hello world!
 *
 */
public class App {

	private static final String STR_END_WITH_JAVA = ".java";

	private JavaParser parser = null;

	private List<File> javaFileList = null;

	private ModelManager manager = null;

	/**
	 * javaFileList, parser 객체를 초기화한다.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void initialize(File file) throws IOException {
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
	private void addJavaFile(File file) {
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

	public void makeModel() throws FileNotFoundException {
		manager = new ModelManager();

		CompilationUnit compUnit = null;

		for (File javaFile : javaFileList) {
			PrintUtil.printlnInfomation("Start parse \"" + javaFile.getAbsolutePath() + "\"");
			compUnit = parser.parse(ParseStart.COMPILATION_UNIT, new StreamProvider(new FileReader(javaFile)))
					.getResult().get();
			StringBuilder sb = new StringBuilder();
			compUnit.accept(new ModelMaker(manager), sb);
		}
	}

	public void makeClassDiagram() throws IOException {
		// 클래스다이어그램 생성을 위한 PlantUML 스크립트 작성
		ClassDiagramScriptMaker cdsm = new ClassDiagramScriptMaker(manager);
		cdsm.makeScript();

		// 클래스다이어그램 생성을 위한 PlantUML 스크립트 파일 출력
		BufferedWriter br = new BufferedWriter(new FileWriter("classDiagramScript.txt"));
		br.write(cdsm.getScript());
		br.close();
		PrintUtil.printlnInfomation("Make class diagram script file \"classDiagramScript.txt\"");

		// PlantUML 실행
		Runtime.getRuntime().exec("java -jar lib/plantuml.jar classDiagramScript.txt");
		PrintUtil.printlnInfomation("Make class diagram \"classDiagram.png\"");
	}

	public void makeSequenceDiagram() throws IOException {
		// 순차다이어그램 생성을 위한 PlantUML 스크립트 작성
		SequenceDiagramScriptMaker sdsm = new SequenceDiagramScriptMaker(manager);
		sdsm.makeScript();

		// 순차다이어그램 생성을 위한 PlantUML 스크립트 파일 출력
		BufferedWriter br = new BufferedWriter(new FileWriter("sequenceDiagramScript.txt"));
		br.write(sdsm.getScript());
		br.close();
		PrintUtil.printlnInfomation("Make squence diagram script file \"sequenceDiagramScript.txt\"");

		// PlantUML 실행
		Runtime.getRuntime().exec("java -jar lib/plantuml.jar sequenceDiagramScript.txt");
		PrintUtil.printlnInfomation("Make squence diagram \"sequenceDiagram.png\"");
	}
	public static void main(String args[]) throws IOException
	{
		//App.testMain();
		App.Main();
	}
	public static void testMain() throws IOException
	{
		Runtime.getRuntime().exec("java -jar lib/plantuml.jar test.txt");
		PrintUtil.printlnInfomation("Make squence diagram \"test.png\"");
	}
	public static void Main() throws IOException {
		App app = new App();
		app.initialize(new File("target_project.info"));
		//app.initialize(new File("sample_target.info"));
		app.makeModel();
		app.makeClassDiagram();
		app.makeSequenceDiagram();

		PrintUtil.printlnInfomation("Successfully finished!");
	}

}
