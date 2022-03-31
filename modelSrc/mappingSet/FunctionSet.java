package mappingSet;

import SWVS.Method;
import dataSet.Function;

public class FunctionSet
{
	SWVS.Method m_fun;
	dataSet.Function d_fun;
	String id;
	
	public FunctionSet(Function fun) 
	{
		d_fun = fun;
		d_fun.getCmd();	//cmd parsing
		// TODO Auto-generated constructor stub
	}

	public void add(Method m) {
		m_fun = m;
	}
	public boolean isSet()
	{
		if(m_fun == null)
			return false;
		else 
			return true;
	}
}