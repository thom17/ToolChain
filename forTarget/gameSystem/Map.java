package gameSystem;


public class Map
{
	int[] eventList;

	/**
	 * 기본 생성자
	 * 5의 배수칸 +1
	 * 나머지 0
	 */
	public Map()
	{
		eventList = new int[30];
		for(int i=0; i<30; i++)
		{
			if(i%5 == 0)
				eventList[i] = 1;
			else
				eventList[i] = 0;
		}
	}
	
	public int getSize()
	{
		return eventList.length;
	}
	public int getEvent(int index)
	{
		if( index < eventList.length)
			return eventList[index];
		else
			return Integer.MAX_VALUE;
	}
}

