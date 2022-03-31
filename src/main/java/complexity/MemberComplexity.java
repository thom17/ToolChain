package complexity;

import dataSet.Member;

public class MemberComplexity extends Complex_Base
{
	int typeScore=0;
	int typeLv = 0;

	public MemberComplexity(Member member)
	{
		simpleMake(member);
	}
	private void simpleMake(Member member) {
		setTypeLevel(member.getTypeName());
		this.coupling = new Coupling();
		this.coupling.setLevel(member);
		this.absComplex = typeScore;
		this.relativeComplex = typeScore*coupling.couplingLv;
	}
	private void setTypeLevel(String typeName)
	{
		if( TypeLevel.isDataType(typeName) )
			typeLv = 1;
		else
			typeLv = 2;
		
		typeScore = TypeLevel.getTypeScore(typeName);
	}
	public int getTypeLevel() {
		// TODO Auto-generated method stub
		return typeLv;
	}
}
