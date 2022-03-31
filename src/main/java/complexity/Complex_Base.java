package complexity;

public abstract class Complex_Base 
{
	protected int absComplex = 0;
	protected int relativeComplex = 0;
	protected Coupling coupling;
	
	public int getCouplingLv(){return coupling.couplingLv;};
	public int getABS() {return absComplex;};
	public int getRES() { return relativeComplex;};
}

class Complexity 
{
	int absComplex;	//절대 복잡도
	int relativeComplex;	//상대 복잡도
	
	int memberScore;	//맴버 개수 당 점수?
	int classScore;		//클래스 개수 당 점수?
	int methodScore;	//함수당 개수 당 점수?
	
	int class_class_balance;	//클래스 간 규모 밸런스	
}

class ClassComplexitys
{
	int absComplex;	//절대 복잡도
	int relativeComplex;	//상대 복잡도
	
	int memberScore;	//맴버 개수 당 점수?
	
	int hasClassScore;		//has 관계 클래스에 대한 점수
	int extendsClassScore;	//is a 관계 클래스에 대한 점수
	int callByScore;		//call 관계에 대한 점수
	
	int methodScore;		//method 개수 당 점수?
}

class MethodComplexitys
{
	int absComplex;	//절대 복잡도
	int relativeComplex;	//상대 복잡도
	
	int parameterScore;	//파라미터 관련 점수
	
	int methodCallScore;	//메소드 호출 관련 점수		reculsive
	
	int memberUseScore;		//멤버 사용 관련 점수
	
	int	expScore;		//연산 복잡도 점수
	
	int repeatScore;	//반복 관련 점수
	
	int brenchScore;	//분기점 관련 점수
}

