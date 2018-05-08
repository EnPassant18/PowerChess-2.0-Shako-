package repl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test REPL methods.
 *
 */
public class ChessReplUtilsTest {

  /**
   * Test FEN method.
   */
  @Test
  public void isFenValidTest() {

    String fen1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    assertTrue(ChessReplUtils.isFenValid(fen1));
  }

}
