package pocketmon.item;

import pocketmon.Animal;
public class PoketBall
{
	Animal poketmon;
	float power;
	
	public boolean throw_to_catch(Animal target)
	{
		if(power < target.def() ) return true;
		else return false;
	}
}