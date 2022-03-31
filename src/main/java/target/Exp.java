package target;

public class Exp 
{
	int field;
	public static void main(String args[])
	{
		Exp exp = new Exp();
		exp.methodCall();
	}

	private int methodCall() 
	{
		int i =0;
		int a, b, c = 5;
		int x=0, y=0, z=0;
		field =  x;
		field = c+x;
		x++;
		y+=field;
		y%=this.field;
		b = methodCall() * this.methodCall();
		z = (i*(b+x))/x;
		return y++;
	}

}
