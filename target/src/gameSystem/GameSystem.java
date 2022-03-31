package gameSystem;

import java.util.Random;
import java.util.Scanner;

import gameSystem.Dice;
import gameSystem.Map;
import gameSystem.Player;

public class GameSystem
{
	int diceShapeList[][];
	int turnIndex;
	
	Scanner scan = new Scanner(System.in);
	Random rand = new Random();
	
	Player[] playerList = new Player[2];
	Dice[] diceList;
	Map map;

	public GameSystem(int diceShapeList[][])
	{
		this.diceShapeList = diceShapeList;
		map = new Map();
		playerList[1] = new Player("AI");
		
	}
	/**UC01_FL01
	플레이어는 자신의 이름을 입력하고 게임을 시작
	@return void 게임 종료
	*/
	public void playGame(String name)
	{
		playerList[0] = new Player(name);
		diceSetting();
		main();
	}
	
	/**UC01_FL02
	시스템은 주사위 두개를 준비한다.
	@return 세팅된 주사위 두개를 반환/초기화
	*/
	public void diceSetting()
	{
		diceList = new Dice[2];
		diceList[0] = new Dice(diceShapeList[0]);
		diceList[1] = new Dice(diceShapeList[1]);
	}
	
	/**UC01_FL03
	게임이 종료될때까지 세부사항을 반복한다.
	@return 게임의 종료
	*/
	public void main()
	{
		turnIndex = 1;
		while( !gameEnd())
		{
			turnIndex = (turnIndex+1)%2;
			Dice dice = choiceDice(playerList[turnIndex]);
			//플레이어가 주사위를 선택하고 던진다.
			int value = diceRoll(dice);
	
			//플레이어가 주사위 값 만큼 이동한다.
			move(value, playerList[turnIndex]);
			
		}
	}
	
	private void printPos(Player player)
	{
		Player opponent = playerList[(turnIndex+1)%2];
		int pos = player.getPos();
		int opponentPos = opponent.getPos();
		int endPos = map.getSize();
		
		System.out.println("Opponent : "+(opponentPos - pos) +", end Left : " + (endPos - pos));
		System.out.print("[Player]");
		String tile;
		for(int i=pos+1; i<pos+10 && i<=endPos; i++)
		{		
			if(i < endPos)
				tile = ""+map.getEvent(i);
			else
				tile = "End";
			
			if(i == opponentPos)
				tile +="(AI)";
			
			System.out.print("["+tile+"]");
		}
		System.out.println();
	}
	
	private Dice choiceDice(Player player) 
	{
		if(player.getName().equals("AI"))
		{
			System.out.println("AI turn :");
			return diceList[rand.nextInt(diceList.length)];
		}
		System.out.println("Your turn :");
		printPos(player);
		for(int i=0; i<diceShapeList.length; i++)
		{
			System.out.print((i+1)+". dice : ");
			for(int d : diceShapeList[i])
			{
				System.out.print(d+" ");
			}
			System.out.println();
		}
		
		int diceIndex = scan.nextInt()-1;
		return diceList[diceIndex];
		
	}
	/**UC01_FL03_01
	플레이어가 주사위를 선택하고 던진다.
	@return 주사위의 결과값을 반환한다.
	*/
	public int diceRoll(Dice dice)
	{
		//선택된 주사위를 굴린다.
		int r = dice.roll();
		return r;
	}
	/**UC01_FL03_03
	플레이어가 주사위 값 만큼 이동한다.
	@return 해당 플레이어의 말이 해당 값 만큼 이동한다.
	*/
	public void move(int dice, Player player)
	{
		int before = player.getPos();
		int after = before+dice;
		String str = "event ("+map.getEvent(after)+")";
		int result;
		if(map.getSize() < after)
		{
			result = map.getSize();
			str = " End";
		}else
		{
			int event = map.getEvent(after);
			str = " event ("+event+")";
			result = after+event;
		}
		System.out.println("dice "+dice+" : "+before+"->"+after+str);
		System.out.println("result "+result);
		player.setPos(result);
	}

	private void printResult()
	{
		int score = playerList[0].getPos()-playerList[1].getPos();
		System.out.println("격차 점수 :" +score);
		if(0 < score)
			System.out.println("플레이어 승");
		else
			System.out.println("플레이어 패배");
	}
	/**UC01_FL04
	시스템의 승자를 알려주고 플레이어의 스코어를 기록한다.
	@return 게임 스코어 기록 및 결과 출력
	*/
	public boolean gameEnd()
	{ 
		for(Player player : playerList)
		{
			if(  player.getPos() == map.getSize())
			{
				printResult();
				return true;
			}
		}
		return false;
	}



}

