package simpleTest;

import java.io.File;
import java.io.IOException;

import dataSet.Class;


import main.Parser;


public class ToString 
{
	//makeInitFile();	
	public static void main(String args[])
	{
		String packageName = "test";
		String test[] = {"test", "tsesf", "asdasd"};
		String test2[] = null;
		Class cl1 = new Class(packageName, "Cl1");
		Class cl2 = new Class(packageName, "Cl2");
		Class cl3 = new Class(packageName, "Cl3");
		Class[] clsList = {cl1, cl2, cl3};
		System.out.println(clsList.toString());
		System.out.println(test.toString());
		
		for(String s : test2)
			System.out.println("sd");
	}
	
	

}
