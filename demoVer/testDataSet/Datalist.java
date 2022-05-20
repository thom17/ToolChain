package testDataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Datalist 
{
	protected Map<String , ClassInfo> classList = new HashMap<String, ClassInfo >();
	protected Map<String , FunctionInfo> functionList= new HashMap<String, FunctionInfo>();
	protected Map<String , MemberInfo> memberList= new HashMap<String, MemberInfo>();
	
	public void addClass(ClassInfo cls)	{ classList.put(cls.getSrcName() , cls); }
	
	public void addFunction(FunctionInfo fn) { functionList.put(fn.getSrcSig() , fn);}
	public void addMember(MemberInfo var) { memberList.put(var.getSrcName() , var);	}
	
	//해당 객체 반환 없으면 null
	public ClassInfo getClass(String key) { return classList.get(key); };
	public FunctionInfo getFunction(String key) { return functionList.get(key); };
	public MemberInfo getMember(String key) { return memberList.get(key); }
	//해당 맵 반환
	public Map<String, ClassInfo> getClassMap() { return classList; };
	public Map<String, FunctionInfo> getFunctionMap() { return functionList; };
	public Map<String, MemberInfo> getMemberMap() { return memberList; };
}
