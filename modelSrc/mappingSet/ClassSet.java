package mappingSet;

import java.util.HashMap;
import java.util.Map;

import SWVS.Method;
import dataSet.Class;
import dataSet.Function;
import xmiManage.ModelManager;

public class ClassSet {

	SWVS.Class m_cls;
	dataSet.Class d_cls;
	Map<String, FunctionSet> functionSet;
	
	public ClassSet(Class d_cls) 
	{
		this.d_cls = d_cls;
		functionSet = new HashMap<String, FunctionSet>();
		for(Function fun : d_cls.getHasList().getFunctionList())
		{
			String id;
			String cmd = fun.getCmd();
			if(cmd != null) {
				String list[] = cmd.split("\n");
				id =list[0];
			}else
				id = fun.getSig();
			functionSet.put(id, new FunctionSet(fun));
		}
		
	}

	public void addModel(SWVS.Class cls) {
		m_cls = cls;
		for(Method m : cls.getMethod())
		{
			String id = m.getId();
			FunctionSet set = functionSet.get(id);
			if(set == null && functionSet.get(m.getObjectName()) == null)
				m.setData_base_SrcName("");
			else 
				set.add(m);
		}
		
	}
	public void setFunctionMappingInfo(ModelManager mmg)
	{
		for(FunctionSet set : functionSet.values())
		{
			if(set.isSet())
				mmg.implementM(set.m_fun, set.d_fun.getSrcName());
			else
			{
				mmg.addMethod(m_cls, set.d_fun.getSig());		
			}
		}
	}
	
}
