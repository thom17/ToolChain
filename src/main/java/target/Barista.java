package target;

public class Barista {
	//Scanner scan = new Scanner(System.in);
	private Coffee makeCoffee(Coffee target, boolean shot)
	{
		Coffee result = new Americano();
		result.addShot();
		return result;
	}
	
	public static boolean getOrder(Barista staff)
	{
		staff.makeCoffee(null, true);
		return true;
	}

}
