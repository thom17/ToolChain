class Player
{
  private int chip;
  private int cash;
  private String name;
  private Record record;

  public Player(String name, String cash)
  {
    this.name = name;
    this.cash = cash;

    init(500);

  }

  public init(int chip)
  {
    this.chip = chip;
    record = new Record();
  }

  public boolean deal(int deal)
  {
    if( chip < deal) return false;
    else return true;

  }

  public void getChip(int chip)
  {
    this.chip += chip;

  }
}
