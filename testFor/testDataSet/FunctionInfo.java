package testDataSet;

public class FunctionInfo 
{

	ClassInfo owner;	
	String sig;
	
	Datalist hasList = new Datalist();
	Datalist callList = new Datalist();
	
	public FunctionInfo(ClassInfo cls, String sig) 
	{
		this.sig = sig;
		this.owner = cls;
		owner.getHasList().addFunction(this);	//해당 함수를 소유하므로.
	}
	public String getSrcSig() { return owner.getSrcName()+"."+sig; }
	public Datalist getHasList() { return this.hasList; };
	public Datalist getCallList() { return this.callList; }
	public String getSig() { return this.sig; };
}
