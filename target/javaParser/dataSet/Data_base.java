package dataSet;

import complexity.Complex;

//Data기반의 add함수는 상대경로를 키로 추가함 (데이터 리스트의 해쉬맵을 직접 호출하여 추가하기때문)
public abstract class Data_base {
	protected String name;
	protected String modifier = "private";	//public private protected
	protected DataList hasList = new DataList();
	protected DataList callList = new DataList();
	protected DataList callByList = new DataList();
	boolean isAbstract = false;	//abstract 여부 (선택적 추상화)
	boolean isStatic = false;
	
	public void setStatic(boolean isStatic) {this.isStatic = isStatic;}
	public void setAbstract(boolean isAbstract) {this.isAbstract = isAbstract;}
	public void setModfi(String mod) {this.modifier = mod; }
	
	public boolean isAbstract() {return isAbstract; }
	
	public String getModifier() {return modifier;}
	public String getName() {return name;};
		
	public DataList getCallList() {return this.callList; }
	public DataList getHasList() {return this.hasList; }
	public DataList getCallByList() {return this.callByList; }	
	
	public static void main(String args[])
	{
		//Class cls = new Class("A");
		//boolean b = false;
	
		
	}
	public abstract String getPackageName();
	public abstract String getSrcName();
	protected abstract void addCallBy(Data_base target);
	protected abstract void addCall(Data_base target);
	protected abstract void addHas(Data_base target);
	public abstract Complex getComplex();
}
