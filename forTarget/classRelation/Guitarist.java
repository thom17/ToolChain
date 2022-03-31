package classRelation;

public class Guitarist{
	Guitar guitar;
	public void buyGuitar()
	{
		this.guitar = new Guitar();
	}
	public Sound playSong(Guitar guitar)
	{
		return guitar.play();
	}
}
