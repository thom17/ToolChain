package dataSet;
/**
 * @brief 간략한 설명
 * @detail 자세한 설명
 * @author 작가
 * @date 날짜
 * @version 버전
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
//DataList의 add함수는 절대경로를 키로 호출함
public class DataList {
	protected Map<String , Class> classList = new LinkedHashMap<String, Class >();
	protected Map<String , Function> functionList= new LinkedHashMap<String, Function>();
	protected Map<String , Member> memberList= new LinkedHashMap<String, Member>();
		
	public void addData( Data_base target)
	{
		if(target instanceof Class)
			classList.put(target.getSrcName(), (Class)target);
		else if(target instanceof Function)
			functionList.put(target.getSrcName(), (Function)target);
		else 
			memberList.put(target.getSrcName(), (Member)target);
	}
	
	public Class findClass(String name)
	{	
		return classList.get(name);		
	}
	public Function findFunction(String name)
	{
		return functionList.get(name);		
	}
	public Member findMember(String name)
	{
		return memberList.get(name);		
	}
	
	public ArrayList<Class> getClassList()
	{
		return new ArrayList<Class>(classList.values());
	}
	public ArrayList<Function> getFunctionList()
	{
		return new ArrayList<Function>(functionList.values());
	}
	public ArrayList<Member> getMemberList()
	{
		return new ArrayList<Member>(memberList.values());
	}
	
	public HashMap<String, Class> getClassMap()
	{
		return (HashMap<String, Class>) this.classList;
	}
	public HashMap<String, Function> getFunctionMap()
	{
		return (HashMap<String, Function>) this.functionList;
	}
	/**
	 * 해당 datalist가 cls외에 다른 클래스를 가지는지 체크
	 * @param cls
	 * @return cls가 아닌 클래스가 있다면 true
	 */
	public boolean checkHasOtherClass(Class cls)
	{
		for(Class target : this.getClassList())
		{
			if( !cls.equals(target) ) return true;
		}
		
		return false;
	}
	
	public Map<String, ArrayList<Class>> separatePackage()
	{
		
		Map<String, ArrayList<Class>> map = new HashMap<String, ArrayList<Class>>();
		for(String key : this.classList.keySet())
		{
			Class cls = this.classList.get(key);
			String packageName = cls.getPackageName();
			ArrayList<Class> list = map.get(packageName);
			if(list == null)
			{
				list = new ArrayList<Class>();
				map.put(packageName, list);
			}
			list.add(cls);
		}
		
		return map;
	}
	
 	public static void  main(String test[])
 	{

 		
 	}

	
}
