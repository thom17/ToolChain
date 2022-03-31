package gameSystem;

import gameSystem.Dice;
import gameSystem.Map;
import gameSystem.Player;

public class GameSystem
{
	Player[] playerList;
	Dice[] diceList;
	Map map;

	/**UC01_FL01
	플레이어는 자신의 이름을 입력하고 게임을 시작
	@return null
	*/
	public void playGame(String name)
	{
	}

	/**UC01_FL02
	시스템은 주사위 두개를 준비한다.
	@return 세팅된 주사위 두개를 반환/초기화
	*/
	public void diceSetting()
	{
	}

	/**UC01_FL03
	게임이 종료될때까지 세부사항을 반복한다.
	@return 게임의 종료
	*/
	public void main()
	{
		//플레이어가 주사위를 선택하고 던진다.		UC01_FL03_01
		GameSystem.diceRoll(dice);

		//플레이어가 주사위 값 만큼 이동한다.		UC01_FL03_03
		GameSystem.move(dice,player);

	}

	/**UC01_FL04
	시스템의 승자를 알려주고 플레이어의 스코어를 기록한다.
	@return 게임 스코어 기록 및 결과 출력
	*/
	public void gameEnd()
	{
	}

	/**UC01_FL03_01
	플레이어가 주사위를 선택하고 던진다.
	@return 주사위의 결과값을 반환한다.
	*/
	public void diceRoll(Dice dice)
	{
		//선택된 주사위를 굴린다.		UC01_FL03_01_01
		Dice.roll();

	}

	/**UC01_FL03_03
	플레이어가 주사위 값 만큼 이동한다.
	@return 해당 플레이어의 말이 해당 값 만큼 이동한다.
	*/
	public void move(Dice dice, Player player)
	{
	}

}

