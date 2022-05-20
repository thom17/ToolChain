package sqliteManage;

import dataSet.Class;

public class DataListDAO 
{
	
	boolean makeTables() 
	{
		return false;
		
	}
	/**
	 * 
	 * @param cls
	 * @return
	 */
	boolean addClass(Class cls)
	{
		String srcName = cls.getSrcName();
		String name = cls.getName();
		String packageSrc = cls.getPackageName();
		String type = this.getType(cls);
		String superList = getSuperList(cls.getSuperClass());

		
		//for()
		
		return true;
		
	}
	private String getSuperList(Class[] superClass) 
	{
		String li = null;
		if(superClass == null)
			return "";
		else
			for(int i = 0; i<superClass.length; i++)
			{
				li += superClass[i].getSrcName() + " ,";
			}
		
		return li.substring(0, li.length()-2);
			
	}
	private String getType(Class cls) 
	{
		if( cls.isAbstract() )
			return "abstract";
		else if( cls.isInterface() )
			return "interface";
		else
			return "Class";
	}
	/**
	 * 
	 * @param fun
	 * @return
	 */
	boolean addFunction(dataSet.Function fun)
	{
		return true;
	}
	/**
	 * 
	 * @param var
	 * @return
	 */
	boolean addMember(dataSet.Member var)
	{
		return true;		
	}
	
	boolean makeClassTable() {
		return true;
	}
	boolean makeFunctionTable(){
		return true;
	}
	boolean makeMemberTable(){
		return true;
	}
	
}
