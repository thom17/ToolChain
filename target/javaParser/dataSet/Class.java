package dataSet;

import java.util.Arrays;

import complexity.Complex;

public class Class extends Data_base{
	boolean isimplement = false;	//인터페이스 여부 (모두 추상화)
	
	Class extendClass[] = null;		//해당 클래스의 부모
	Class implementClass[] = null;
 	
	String extendSrcName[] = null;	//
 	String implementSrcName[] = null;	//
	String packageSrc = "default";	//해당 클래스의 패키지 경로
	
	boolean isLib = false;
	boolean isInterface=false;
	
	Complex complex;
	
	public Class(String srcName)
	{
		String list[] = srcName.split("\\.");
		this.name = list[list.length-1];
		this.packageSrc=list[0];
		for(int i = 1; i<list.length-1; i++)
		{
			this.packageSrc += "."+list[i];
		}

		this.modifier="public";
		
	}
	public Class(String packageSrc, String name)
	{
		if(name.equals("script.DataListDiagram"))
			System.out.println();
		this.name = name;
		this.packageSrc = packageSrc;
	}
	
	public String getSrcName()
	{
		return packageSrc+"."+name;
	}
	public void setImplementSrcName(String arg[])
	{
		this.implementSrcName = arg;
	}
	public void setExtendSrcName(String arg[])
	{
		this.extendSrcName = arg;
	}
	public void setExtendClass(Class extendClass[])
	{
		this.extendClass = extendClass;
	}
	public void setImplementClass(Class implementClass[])
	{
		this.implementClass = implementClass;
	}
	
	public void setInterface(boolean isInterface) {this.isInterface = isInterface;}
	
	public boolean isimplement() { return isimplement;}
	public boolean isInterface() { return this.isInterface; }
	public Class[] getSuperClass()
	{
		if(implementClass == null)
			return extendClass;
		else if(extendClass == null)
			return implementClass;
		
	
		int extendSize = extendClass.length;
		Class superList[] = new Class[implementClass.length + extendSize];		
	
		for(int i=0; i<extendSize; i++)
		{
			superList[i] = extendClass[i];
		}
		for(int i=0; i<implementClass.length; i++)
		{
			superList[i+extendSize] = implementClass[i];
		}
		return superList;
	}
	
	public String getPackageName() { return this.packageSrc; }
	public void setIsInterface(boolean isInterface) { this.isInterface = isInterface;	}

	public Complex getComplex() { return this.complex;}
	
	@Override
	public void addCallBy(Data_base target) 
	{
		callByList.addData(target);	//		X Call By B	 only callBy callFun()	
	}
	@Override
	public void addCall(Data_base target) 
	{
		callList.addData(target);
		target.callByList.addData(this);	//X Call Y => Y Call By X
	}
	@Override
	public void addHas(Data_base target) 
	{
		hasList.addData(target);
		
	}
	public void setComplex() {
		this.complex = new Complex(this);
		
	};
	
}
