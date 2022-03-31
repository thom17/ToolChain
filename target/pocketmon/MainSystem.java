package pocketmon;

public class MainSystem {
	public static void main(String args[])
	{
		Dog dg = new Dog();
		Cat ct = new Cat(); 
		Human h1 = new Human("h1", dg);
		Human h2 = new Human("h2", ct);
		h1.printInfo();
		h2.printInfo();
		h1.Fight(h2);
		h2.Fight(h1);
	}
}
