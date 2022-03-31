class Test
{
  public static void main(String args[])
  {
    for(int index = 0; index < args.length; index++)//ast.stmt.ForStmt
    { //ast.stmt.BlockStmt

      int mode = Tester.getMode(args[index]); //ast.expr.VariableDeclarationExpr->ast.stmt.ExpressionStmt
      switch(mode)//stmt.SwitchStmt
      {
        case 1: 
          Tester.funA(Tester.getOk(args[index]));//ast.stmt.ExpressionStmt
          break;

        case 2:
          Tester.funB();
        case 3:
          Tester.funC();
        default:
          Tester.funD();
      };
    }
  }


}
class Tester
{
  public static int x = 12;
  public static String str = "hellow World";
  public static int getMode(String str) {return str.length();};
  public static boolean getOk(String str) {return (str.length() % 2 == 1);};
  public static void setup() { x = 12;};
  public static int getChunk( ) {return str.length();}
  public static boolean main()
  {
	  str += --x;
	  return (x%2 == 1);
  }
  public static int funA(boolean check)
  {
    if(check)
      return main();
    else
      return getChunk();
  }
  public static int funB()
  {
    return funA(main());
  }
  public static void funC()
   {
     setup();
   }
   public static void funD()
   {
     if(10 < funB())  funC();
     funB();
   }
}
