package classRelation;

public class GuitarAcademy 
{
	Guitarist teacher;
	Guitar academyGuitar;
	public GuitarAcademy()
	{
		this.academyGuitar = new Guitar();
		this.teacher = new Guitarist();
	}
	public void lesson(Guitarist student)
	{
		teacher.playSong(academyGuitar);
		student.playSong(academyGuitar);
	}

}
