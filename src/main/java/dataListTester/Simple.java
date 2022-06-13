package dataListTester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dataSet.OMS;
import dataSet.Member;

public class Simple 
{
	public static ArrayList<Member> findTypeName(String typeName, OMS datalist)
	{
		ArrayList<Member> list = new ArrayList<Member>();
		for(Member m : datalist.getMemberList())
		{
			if(m.getTypeName().equals(typeName))
				list.add(m);
		}
		return list;
	}
	public static Map<String, ArrayList<Member>> seperateTypeMap(OMS datalist)
	{
		Map<String, ArrayList<Member>> map = new HashMap<String, ArrayList<Member>>();
		for(Member m : datalist.getMemberList())
		{
			String typeName = m.getTypeName();
			ArrayList<Member> list = map.get(typeName);
			if(list == null)
			{
				list = new ArrayList<Member>();
				list.add(m);
				map.put(typeName, list);
			}
			list.add(m);
		}
		return map;
		
	}
}
