package testDataSet;

public class ClassInfo 
{
	String name;
	String packageName;
	
	Datalist hasList = new Datalist();
	Datalist callList = new Datalist();
	
	public ClassInfo(String name, String packageName)
	{
		this.name = name;
		this.packageName = packageName;
	}
	public String getSrcName() {return packageName+"."+name; } 
	public String getPackageName() { return this.packageName; };
	
	public Datalist getHasList() { return this.hasList; };
	public Datalist getCallList() { return this.callList; }

}
