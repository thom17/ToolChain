package simpleStructure;

public class OneMethodClass 
{
	public String oneMethod(int a, int b)
	{
		if( a == b)
			return "same";
		else if( a < b)
			return a+"<"+b;
		else
			return b+"<"+a;
	}
}
