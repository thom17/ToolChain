package main;
//madeBy khe

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TargetProjectInfomationReader {

	private static final String STR_END_WITH_JAR = ".jar";

	private static final String STR_END_WITH_SRC1 = "/src";

	private static final String STR_END_WITH_SRC2 = "\\src";

	private List<String> srcPathList = null;

	private List<String> jarPathList = null;

	public void read(File file) throws IOException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		srcPathList = new ArrayList<String>();
		jarPathList = new ArrayList<String>();

		String line = null;
		int lineNum = 1;
		while ((line = br.readLine()) != null) {
			if (line.toLowerCase().endsWith(STR_END_WITH_JAR)) {
				jarPathList.add(line);
			} else if (line.toLowerCase().endsWith(STR_END_WITH_SRC1)
					|| line.toLowerCase().endsWith(STR_END_WITH_SRC2)) {
				srcPathList.add(line);
			} 
			lineNum++;
		}

		br.close();
	}

	public List<String> getSrcPathList() {
		if (null == srcPathList) {
			return new ArrayList<String>();
		}
		return srcPathList;
	}

	public List<String> getJarPathList() {
		if (null == jarPathList) {
			return new ArrayList<String>();
		}
		return jarPathList;
	}

}
