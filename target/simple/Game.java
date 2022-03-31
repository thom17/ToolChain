import stub.FileMakerSystem;
class Game
{
  private static Player playerList[];
  private Dealer dealer;
}

  public static void main(String fName[])
  {
    FileMakerSystem maker = new FileMakerSystem(fName);
    playerList = maker.getPlayerList();
    dealer = maker.getDealer();

    PlayerList[] nextPlayerList = playerList;
    while(checkEnd(nextPlayerList))
    {
      nextPlayerList = dealer.gainBetting(playerList);
    }
  }

  public boolean checkEnd(playerList current[])
  {
      /*,,,,*/
      return true;

  }

}
