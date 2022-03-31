package pocketmon;

import java.util.ArrayList;
import pocketmon.item.PoketBall;

public class Human extends Animal
{
	String name;
	public static Animal pet;
	private ArrayList<PoketBall> ball;
	public Human(String name, Animal pet)
	{
		super();
		this.name = name;
		this.pet = pet;
		ball = new ArrayList<PoketBall>();
		ball.add(pet);
	}
	public void printInfo()
	{
		System.out.println(name+"'s pet:");
		pet.printInfo();
		
	}
	public boolean Fight(Human target)
	{
		float atkResult =  this.pet.attack(target);
		System.out.println("Atk : "+atkResult);
		float opResult = target.attack(this);
		System.out.println("Def : "+opResult);
		if(atkResult < opResult) return false;
		else return true;
	}
	public float def()
	{
		return this.pet.def();
	}
	public boolean trade(Human op , Animal target)
	{
		this.pet = target;
		return true;
	}
}