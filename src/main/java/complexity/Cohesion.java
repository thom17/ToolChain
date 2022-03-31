package complexity;

import dataSet.Data_base;
import dataSet.Function;

import java.util.ArrayList;

import dataSet.Class;

public class Cohesion 
{
	int cohesionLv;
	int selfNum;
	
	
	public int getLv() {return this.cohesionLv;}
	public void setLevel(Class target)
	{
		ArrayList<Function> functionList = target.getHasList().getFunctionList();
		
		
		
		
	}
}
