package complexity;

public class TypeLevel {

	
	public static boolean isDataType(String typeName)
	{
		String list[] = typeName.split("\\.");
		
		if(list.length == 1 || typeName.equals("java.lang.String"))
			return true;
		else
			return false;
	};
	public static int getTypeScore(String type)
	{
		int score = 0;
		int arrayDepth = getArrayDepth(type);
		
		if(0 < arrayDepth)
		{
			type = type.substring(0, type.indexOf("["));
		}
		
		
		if(isDataType(type)) score += 1; 
		else score += 2;
		
		for(int d=0; d<arrayDepth; d++)
			score *=2;
		return score;
	}
	private static int getArrayDepth(String type)
	{
		int depth = 0;
		int index = 0;
		
		while(index != -1)
		{
			index = type.indexOf("[", index+1);
			if(index != -1 ) depth++;
		}
		return depth;
	}

	public static void main(String test[])
	{
		String t = "dataSet.DataList";
		String list[] = t.split("\\.");
		int i = 10;
	}
}
