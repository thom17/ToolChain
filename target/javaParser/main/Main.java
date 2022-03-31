package main;
import java.io.IOException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import dataSet.DataList;
import dataSet.Function;
import diagram.ForEx;
import diagram.ForQuiz4;
import diagram.NodeClassDg;
import hongik.selab.parser.java.JavaParser;
import main.DataListDiagram;
import main.Test_Visitor;
//ctrl+shift +O 
public class Main 
{
	public static void main(String[] args) throws IOException, InterruptedException 
	{
			String srcPath = "./target/";
			//ast 리스트 생성
			CompilationUnit astList[] = JavaParser.makeASTList(srcPath);
			//데이터를 담을 클래스 생성
			DataList datalist = new DataList();
		
			for(CompilationUnit ast : astList)
			{	//visitor를 돌려서 각각의 ast의 데이터 추출
				Test_Visitor.addDataList(ast, datalist);	//step 2
			}
			//DataListDiagram.main(datalist);		//step 3
			//ForQuiz.main(datalist);		//step 4
			//ForQuiz4.main(datalist);	//step3 plantUML
			//Flowchart.main(datalist);	//step4 Dot
			
			ForEx.main(datalist);	//최종 그래프 생성
			
			//for(Function fun : datalist.getFunctionList() )
			//	NodeClassDg.main(fun);
				
			
			
			System.out.println("done"); 
			//ForMidEx.print(datalist);
	}
}