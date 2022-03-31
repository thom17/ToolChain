package gameSystem;

import java.util.Random;

public class Dice
{
	int[] shapeList;
	Random rand = new Random();
	
	/**
	 * 
	 * @return
	 */
	public Dice(int list[])
	{
		this.shapeList = list;
		
	}
	
	/**UC01_FL03_01_01
	선택된 주사위를 굴린다.
	@return null
	*/
	public int roll()
	{
		int size = shapeList.length;
		int r = rand.nextInt(size);
		return shapeList[r];
	}

}

