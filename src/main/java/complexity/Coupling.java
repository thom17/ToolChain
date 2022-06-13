package complexity;

import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;

import java.util.ArrayList;

import dataSet.Class;
public class Coupling 
{
	int couplingLv;
	
	public int getLv() {return this.couplingLv;}
	
	public void setLevel(Data_base target)
	{
		if(checkNotUse(target))
			couplingLv = -1;
 
		if(target instanceof Member)
		{
			if(checkNotCallBy( (Member) target))
				couplingLv = 0;
			else if( checkDataCoupling((Member) target) )
				couplingLv = 1;
			else if(checkStampCoupling((Member) target))
				couplingLv = 2;
			else if(checkControlCoupling(target))
				couplingLv = 3;
			else if(checkCommonCoupling((Member) target))
				couplingLv = 4;
		}
		else
			couplingLv = 5;
	}

	private boolean checkNotCallBy(Member member) 
	{
		OMS list = member.getCallByList();
		Data_base owner = member.getOwner();
		ArrayList<Function> useMethodList = list.getFunctionList();
		if(owner instanceof Class)
		{
			for(Function fun :  useMethodList)
			{
				Data_base callOwner = fun.getOwner();
				if(callOwner != owner)
					return false;
				else if(!checkNotUse(fun))
					return false;
			}
		}
		return true;
	}

	private boolean checkNotUse(Data_base target) {
		OMS list = target.getCallByList();
		int size = list.getClassList().size() + list.getFunctionList().size() + list.getMemberList().size();
		if(size == 0)
			return true;
		else
			return false;
	}

	private boolean checkCommonCoupling(Member target) {
		OMS callByList = target.getCallByList();
		Data_base d_owner = target.getOwner();
		if(d_owner instanceof dataSet.Class)
		{
			Class owner = (Class) d_owner; 
			for(Class caller : callByList.getClassList())
			{
				if(caller != owner)
					return true;
			}
		}
		return false;
	}

	private boolean checkControlCoupling(Data_base target) 
	{
		
		return false;
	}

	private boolean checkStampCoupling(Member target) {
		OMS callByList = target.getCallByList();
		Data_base owner = target.getOwner();
		if(owner instanceof dataSet.Class)
		{
			for(Function fun : callByList.getFunctionList())
			{
				Data_base caller = fun.getOwner();
				if(caller != owner)
					return false;
			}
		}
		return true;
	}


	private boolean checkDataCoupling(Member target) {
		OMS callByList = target.getCallByList();
		Data_base owner = target.getOwner();
		String typeName = target.getTypeName();
		if(TypeLevel.isDataType(typeName))
		{
			if(owner instanceof dataSet.Class)
			{
				for(Function fun : callByList.getFunctionList())
				{
					Data_base caller = fun.getOwner();
					if(caller != owner)
						return false;
				}
			}
			return true;
		}
		else
			return false;
	}

}
