package dataSet;

import java.util.ArrayList;

import complexity.Coupling;

//Data기반의 add함수는 상대경로를 키로 추가함 (데이터 리스트의 해쉬맵을 직접 호출하여 추가하기때문)
public abstract class Data_base {
	protected String cmd;
	protected String name;
	protected String modifier = "private";	//public private protected
	protected OMS hasList = new OMS();
	protected OMS callList = new OMS();
	protected OMS callByList = new OMS();
	
	//Coupling couplingLv;
	
	boolean isAbstract = false;	//abstract 여부 (선택적 추상화)
	boolean isStatic = false;
	
	
	public void setCmd(String cmd) {this.cmd = cmd;}
	public void setStatic(boolean isStatic) {this.isStatic = isStatic;}
	public void setAbstract(boolean isAbstract) {this.isAbstract = isAbstract;}
	public void setModfi(String mod) {this.modifier = mod; }
	
	public boolean isAbstract() {return isAbstract; }
	public boolean isStatic() {return isStatic; }
	//public String getId() {};
	public String getModifier() {return modifier;}
	public String getName() {return name;}
	public String getCmd() {return cmd;}	
	
	public OMS getCallList() {return this.callList; }
	public OMS getHasList() {return this.hasList; }
	public OMS getCallByList() {return this.callByList; }	
	/**
	 * this.getCallOtherList
	 * this는 Data_base
	 * @param list 탐색할 리스트
	 * @return list에서 this가 아닌 data_base들 반환
	 */
	public ArrayList<Data_base> getOtherList(ArrayList<Data_base> list)
	{
		ArrayList<Data_base> result = new ArrayList<Data_base>();
		for(Data_base target : list)
		{
			if(target != this)
				result.add(target);
		}
		return result;
	}
	
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
}
