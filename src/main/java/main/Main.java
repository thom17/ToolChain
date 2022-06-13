package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import complexity.PackageCp;
import dataSet.OMS;
import dev.khe.DiagramRecover.PrintUtil;
import diagramMaker.ComplexDiagram;
import diagramMaker.DataListDiagram2;

/**
 * 태스트 및 개발을 위한 핵심적인 Main 클래스.<br>
 * static 변수 os 를 설정해 주어야함. <br>
 * 첫 실행시 makeInit메서드를 실행하면 프로젝트 폴더들 대상으로 상대경로를 사용하여 실행이 된다.<br>
 * 
 * @author thom1
 *
 */
public class Main {
  public final static String WIN = "\\";
  public final static String MAC = "/";
  static String os = WIN;

  /**
   * makeInit 메서드에서 호출한다.<br>
   * 해당 프로젝트 폴더에 있는 자바 파서와 관련된 jar 파일들을 writer에 추가한다.<br>
   * 
   * @param writer
   * @throws IOException
   */
  private static void addJavaParserLib(BufferedWriter writer) throws IOException {
    File dir = new File("lib/javaParser");
    String path = dir.getAbsolutePath();
    String list[] = dir.list();
    for (String str : list)
      writer.write(path + os + str + "\n");

  }

  /**
   * 첫 프로젝트 실행시 상대경로로 설정하여 깔금히 실행하기 위함. 이 메서드를 사용 후 주석처리하여 필요한 설정을 마치면 됨.
   * 
   * @throws IOException
   * 
   */
  public static void makeInitFile() throws IOException {
    System.out.println("기본 target_projcet.info 파일 생성...\n");
    File file = new File("target_project.info");
    String src = file.getAbsolutePath().replace(os + "target_project.info", "");
    String str = src + "/target/src\n" + src
        + "/lib/org.eclipse.equinox.launcher_1.5.700.v20200207-2156.jar\n";
    System.out.println(src + "/target_projcet.info 파일 생성 됨\n");
    // System.out.println(str);

    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write(str);
    addJavaParserLib(writer);
    writer.close();
  }

  /**
   * 단순히 plantUML을 실행시켜 태스트 하기 위한 것
   * 
   * @param filePath 스크립트 파일 경로 ex) result/test.txt
   * @throws IOException
   */
  public static void testUML(String filePath) throws IOException {
    Process p = Runtime.getRuntime().exec("java -jar lib/plantuml.jar -tsvg " + filePath);
    try {
      p.waitFor();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    PrintUtil.printlnInfomation("Make Test diagram");
  }

  /**
   * 다이어그램을 그리는 메인 함수
   * 
   * @param datalist 그리는 소스가 되는 데이터 리스트 주석을 사용해 제어중.
   * @throws IOException
   * @throws InterruptedException
   */
  public static void drawMain(OMS datalist) throws IOException, InterruptedException {
    // DataList targetList = new DataList();
    // targetList.addData(datalist.getClassMap().get("snavigator.SN_Dumper"));
    // ForQuiz4.main(datalist);
    // StateNodeDiagram.main(datalist);

    // CallGraph.main(datalist);
    // ClassDiagram.main(datalist);
    // ClassDiagram2.main(datalist);
    // ClassDiagram3.main(datalist);
    // DataListDiagram.draw(datalist);
    DataListDiagram2.draw(datalist);
    // DataListDiagram3.draw(datalist);
    // StateNodeDiagram.main(datalist);
    // GraphStructure.main(datalist);
    // GraphStructure_CF.main(datalist);
    // Sequence.main(datalist);
    // SequenceDeep.main(datalist);
    // NodeClassDg.main(datalist);
    // UseCase_C_C_F.main(datalist);
    // UseCase_C_F_C.main(datalist);
    // UseCase_C_F_F.main(datalist);
    // UseCase_C_CC_HF.main(datalist);
    // UseCase_C_CF_CC.main(datalist);
    // UseCase.main(datalist);
    // ForQuiz4.main(datalist);
    ComplexDiagram.main(datalist);
  }

  /**
   * 메인 함수.<br>
   * 첫 실행시는 makeInitFile을 사용하여 설정하자.
   * 
   * @param args 사용하지 않음.
   * @throws IOException
   * @throws InterruptedException
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    makeInitFile();
    Parser.initialize(new File("target_project.info"));
    OMS datalist = Parser.makeDataList();
    drawMain(datalist);

    PackageCp pcp = new PackageCp(datalist);
    pcp.printScore();

    // ModelManager mmg = new ModelManager("Dice.swvs");
    // DataMapper mapper = new DataMapper(datalist, mmg);
    // UsecaseDg2.main(mmg.project);


    // ClassDg.main(new XmiReade)
    // testUML();
    System.out.println("DiagramForJava's main end");

  }

}
