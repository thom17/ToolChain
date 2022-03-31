package hongik.selab.parser.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

import dataSet.DataList;

//��ǥ jdk parser ast���� �д� �ļ�
public class JavaParser 
{
	public static ASTParser parser;
	public static CompilationUnit compilationUnit;
	public static ArrayList<File> targetList;
	public static File settingFile;
	public static int fileNum;
	
	//src 경로의 자바 파일들을 찾아내어 파싱하여 AST[] 생성 (메인 함수)
	public static CompilationUnit[] makeASTList(String src)
	{
		 File srcFile = new File(src);	
		 targetList = new ArrayList<File>();
		 makeFileList(srcFile);
		 fileNum = targetList.size();
		 
		 CompilationUnit astList[] = new CompilationUnit[fileNum];
		 
		 for(int i=0; i<fileNum; i++)
		 {
			 File target = targetList.get(i);
			 init(target);
			 astList[i] = parsing();
		 }
		 return astList;	
	}
	
	//해당 파일이 자바파일인지 체크
	private static boolean isJavaFile(File target)
	{
		String fileName = target.getName();
		String ext = fileName.substring( fileName.lastIndexOf(".") + 1 );
		
		if ( ext.equals("java") )
			return true;
		else 
			return false;
	}	
	//해당 file이 폴더라면 내부 파일 리스트를 생성하여 재귀함
	//java 파일이라면 targetList에 추가함. 
	private static void makeFileList( File file )
	{
		File fileList[] = file.listFiles();
		
		for(File target : fileList)
		{
			if ( target.isDirectory() ) 
				makeFileList(target);
			else if( isJavaFile(target) )
				targetList.add(target);
		}
	}
	
	//해당 File을 기반으로 파서 설정
	public static void init(File file) 
	{
		BufferedReader reader = null;
		
		try {
			reader =new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
		}catch(FileNotFoundException e){e.printStackTrace();}
		
		StringBuilder source = new StringBuilder();
		char[]buf=new char[10];
		int numRead =0;
		
		try {
			while((numRead = reader.read(buf))!= -1) {
				String readData = String.valueOf(buf, 0 ,numRead);
				source.append(readData);
				buf =new char[1024];
			
			}
			reader.close();
		}catch (IOException e) {
			e.printStackTrace();
			};
			
			parser =ASTParser.newParser(AST.JLS8); 
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			
			parser.setSource(source.toString().toCharArray());
			parser.setStatementsRecovery(true);
			parser.setBindingsRecovery(true);
			parser.setResolveBindings(true);
			
			Map<?,?> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_8,options);
			parser.setCompilerOptions(options);
		
	};
	
	//현재 설정으로 파싱하여 AST 생성 및 반환 새방식
	public static CompilationUnit parsing()	
	{
		compilationUnit = (CompilationUnit)parser.createAST(null);
		return compilationUnit;
	}
	
	//태스트용 메인함수
	public static void main(String test[])
	{
		String src = "./target";
		CompilationUnit list[] = makeASTList(src);
		for(CompilationUnit ast : list)
		{
			System.out.println(ast);
		}
	}

}
