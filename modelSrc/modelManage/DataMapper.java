package modelManage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dataSet.OMS;
import diagram.ClassDg;
import mappingSet.ClassSet;
import mappingSet.FunctionSet;
import xmiManage.ModelManager;
import xmiManage.XmiReader;

public class DataMapper 
{
	OMS datalist;
	ModelManager modelManager;
	Map<String, ClassSet> classSet = new HashMap<String, ClassSet>();
	Map<String, FunctionSet> functionSet;
	
	public DataMapper(OMS datalist, ModelManager modelManager)
	{
		this.datalist = datalist;
		this.modelManager = modelManager;
		makeClass(datalist);
	}
	private void makeClass( OMS datalist)
	{
		for(dataSet.Class cls : datalist.getClassList() )
		{
			String clsSrcName = cls.getSrcName();
			classSet.put(clsSrcName, new ClassSet(cls));
		}
		for(SWVS.Class cls : modelManager.getClassList())
		{
			String clsName = cls.getData_base_SrcName();
			//String clsName = cls.getPackage()+"."+cls.getObjectName();
			ClassSet set =classSet.get(clsName);
			set.addModel(cls);	//set 완성 
			set.setFunctionMappingInfo(modelManager);
		}	
	}

	public static void main(String args[])
	{
		XmiReader xmi = new XmiReader("Music.swvs");
		try {
			ClassDg.main(xmi.getProject());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

