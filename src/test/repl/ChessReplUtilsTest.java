
public class ChessReplUtilsTest {
  
  @Test
  public void isFenValidTest() {
    
    String fen1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    assertTrue(ChessReplUtils.isFenValid(fen1));
  }
  

}
