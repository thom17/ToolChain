package gameSystem;


public class Player
{
	String name;
	int pos;

	public Player(String name) 
	{
		this.name = name;
		pos = 0;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	public int getPos()
	{
		return pos;
	}

	public void setPos(int pos) 
	{
		// TODO Auto-generated method stub
		this.pos = pos;
	}
}

