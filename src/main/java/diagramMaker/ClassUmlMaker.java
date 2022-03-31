package diagramMaker;

import java.util.ArrayList;
import dataSet.Class;
import dataSet.DataList;

public class ClassUmlMaker extends UmlMaker
{
	public static void main( DataList datalist )
	{
		ArrayList<Class> classList = datalist.getClassList();
		
		for(Class cls : classList)
		{
			String clsName = cls.getName();
			cls.getHasList();
			
		}
	}

}
