package dataSet;

import java.util.ArrayList;

import complexity.Coupling;
import complexity.MemberComplexity;
import complexity.TypeLevel;

import java.util.ArrayList;

public class Member extends Data_base {
	Data_base owner;
	ArrayList<String> imageList = new ArrayList<String>();
	Data_base typedef;
	String typeName;
	
	boolean superMember = false;
	MemberComplexity complexity;
	public Member(String name)
	{
		this.name = name;
		superMember = true;
	}
	public Member(String typeName, String name)
	{
		this.typeName = typeName;
		this.name = name;
	}
	public Member(String name, Class owner)
	{
		System.out.println(owner.getSrcName()+" call "+name);
		this.name = name;
		this.owner = owner;
	}
	public Member(String type ,String name, Data_base owner)
	{
		//System.out.println("check ! "+owner.getSrcName()+" call "+name);
		this.typeName = type;
		this.name = name;
		this.owner = owner;
	}
	public void setTypeDef(Data_base typedef)
	{
		this.typedef = typedef;
	}
	
	/*
	public Member(String name, String init, Class owner)
	{
		this.owner = owner;
		this.modifier = "private";
		System.out.println(" init : "+init+"("+name+")");
		
		init = init.substring(0, init.length()-1);
		String list[] = init.split("\\s");
		this.name = name;
		int i=0;
		while(i < list.length && !list[i].equals(name))
		{
			if(list[i].equals("abstract")) this.isAbstract = true;
			else this.modifier = list[i];
			i++;
		}
		typeName = list[i-1];
		String img="";
		while( i < list.length)
		{
			if( !list[i].equals("=") ) img+=list[i];
			i++;
		}
		if(!img.equals("")) imageList.add(img);
	}
	*/
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
	

	
	public void setSuperMember()	{	this.superMember = true;	}
	public boolean isSuperMember() {return this.superMember; }
	public ArrayList<String> getImageList(){ return this.imageList; }
	public void addImage(String img) { imageList.add(img); }
	public String getTypeName() { return this.typeName; }
	public String getSrcName() { return owner.getSrcName()+"."+name; }
	public Data_base getOwner() { return this.owner; }
	@Override
	public String getPackageName() {
		return owner.getPackageName();
	}

	public int getScore() {
		// TODO Auto-generated method stub
		return TypeLevel.getTypeScore(typeName);
	}
	public MemberComplexity getComplex() {
		// TODO Auto-generated method stub
		if(complexity == null)
			complexity = new MemberComplexity(this);
		return complexity;
	}
}
