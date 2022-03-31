package simple;

class Simple
{
	int i;
	public Simple(int x)
	{
		int r = x;
		while( 1 < r)
		{
			i = r * i;
			r--;
		}	
			
	}
	public int get(int b) 
	{
		return b*b;
	}
	public void test()
	{
		int d = get(2);
		
	}
	public static void main(String args[])
	{
		Simple s = new Simple(5);
		int d = s.get(4);
		
	}	
}