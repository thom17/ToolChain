package pocketmon;

abstract class Animal implements Poketmon
{
	int codeNum;
	float speed;
	float atk;
	float def;
	float size;
	String name="동물";
	
	
	public Animal()
	{
		speed = 1;
		atk = 1;
		def = 1;
		size = 1;
	}
	public float def()
	{
		return speed*def/size;
	}
	public float attack(Animal target)	
	{
		float damage = atk*speed*size;
		return damage-target.def();
	}
	public void printInfo()
	{
		String str = "Name : "+this.name +" ("+codeNum+")\n";	
		str += "sp"+speed+" at"+atk+"/def"+def+"("+size+ " size)";
		System.out.println(str);
	}
}