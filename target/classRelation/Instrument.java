package classRelation;

public abstract class Instrument 
{
	Sound sound;
	public Sound play()
	{
		sound = new Sound(10);
		return sound;
	}

}
