package diagramMaker;

import java.util.ArrayList;
import dataSet.Class;
import dataSet.OMS;

public class ClassUmlMaker extends UmlMaker
{
	public static void main( OMS datalist )
	{
		ArrayList<Class> classList = datalist.getClassList();
		
		for(Class cls : classList)
		{
			String clsName = cls.getName();
			cls.getHasList();
			
		}
	}

}
