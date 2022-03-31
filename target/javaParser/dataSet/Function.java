package dataSet;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import complexity.Complex;
import dataSet.stateNode.StateNode;

public class Function extends Data_base{
	boolean isStatic;
	Data_base owner;
	String returnType;
	String sig;
	String parameterType[] = null;
	String parameterName[] = null;
	String code;
	//int line;
	StateNode headNode;
	
	
	Complex complex;
	

	
	public void setCode(String code)
	{
		this.code = code; 
	}
	public String getCode() {return this.code;}
	
	/*
	public Function(MethodDeclaration n)
	{
		Optional<BlockStmt> block = n.getBody();
		returnType = n.getType().toString();
		name = n.getName().asString();
		parameterType = (String[]) n.getParameters().toArray();
		if(block.isPresent())
			this.node = block.get();
	//	System.out.println(name + "/"+n.getName().getIdentifier());		
	}
	*/
	public Function(String sig, Class owner)	//임시 선언
	{
		this.owner= owner;
		this.sig = sig;
		String list[] = sig.split("\\(");
		this.name = list[0];
		
	}
	public Function(String returnType, String sig, Data_base owner) 
	{
		this.returnType = returnType;
		this.sig = sig;
		this.owner = owner;
		owner.addHas(this);
		
	}
	public void setHeadNode(StateNode headNode) {this.headNode = headNode; }
	public void setInit(String returnType , String name)
	{
		this.returnType = returnType;
		this.name = name;
	}

	public String getSrcName() 
	{
		return  owner.getSrcName() + "." + sig;	
	}

	public String getParameterName()
	{
		if(parameterType == null) return "( )";
		
		String str = "(";
		for(int i=0; i<parameterType.length; i++)
		{
			str += parameterType[i]+" ";
		}
		str+=")";
		return str;
	}
	public void setParameter(String type[], String name[])
	{
		parameterType = type;
		parameterName = name;
	}
	
	public StateNode getHeadNode() {return this.headNode; }
	public Data_base getOwner() {return this.owner; }
	public String getSig() {return this.sig; }
	public String getTypeName() { return this.returnType; }
	public String[] getParameterTypeList() { return this.parameterType; }
	public String[] getParameterNameList() { return this.parameterName; }
	
	public Complex getComplex() { return this.complex = new Complex(this); }
	@Override
	public String getPackageName() {
		return owner.getPackageName();
	}
	@Override	//local Variable, local Function
	public void addHas(Data_base target) 
	{
		// TODO Auto-generated method stub
		hasList.addData(target);
	}
	@Override
	public void addCallBy(Data_base target) 
	{
		// TODO Auto-generated method stub
		callByList.addData(target);
		this.owner.addCallBy(target);
	}
	@Override
	public void addCall(Data_base target) {
		// TODO Auto-generated method stub
		callList.addData(target);
		this.owner.addCall(target);
		target.addCallBy(this);
		target.addCallBy(this.owner);
	}

	public void setName(String name) {
		this.name = name;
		
	}

	public void setComplex() {
		this.complex = new Complex(this);
		
	}
}
