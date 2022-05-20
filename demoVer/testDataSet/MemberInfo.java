package testDataSet;

public class MemberInfo 
{
	ClassInfo owner;
	String name;
	
	Datalist callList = new Datalist();

	public MemberInfo(ClassInfo owner, String name)
	{
		this.owner = owner;
		this.name = name;
		owner.getHasList().addMember(this);	//해당 맴버를 소유하므로.
	}
	public String getName() { return this.name; };
	public String getSrcName() { return owner.getSrcName() +"."+name; }
	public Datalist getCallList() { return this.callList; }
}
