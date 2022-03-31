package target;

public class IfTree 
{
	static int sum=0;
	public static void main(String args[])
	{
		int l = args.length;
		if( l %2 == 1)
		{
			if(1 < args[0].length())
			{
				int ll = args[0].length();
				sum += ll;
			}else if (args[0].length() == 1)
			{
				int lr = 1;
				sum += lr;
			}else
			{
				int lrr = 6;
				sum+= lrr;
			}
			
			sum+= 1;
		}else
		{
			int r=5;
			if( sum < -3)
			{
				sum++;
				if( sum < -3)
				{
					sum++;
					if( sum < -3)
						sum += 3;
					
						
				}else if(sum < -2)
				{
					sum +=2;
					if(0 == sum)
						sum = 10;
					
				}else
					sum *=-1;
			}else if (sum <-6)
			{
				sum+=6;
			}
			sum +=r;
		}
		sum *= sum;
	}
}
