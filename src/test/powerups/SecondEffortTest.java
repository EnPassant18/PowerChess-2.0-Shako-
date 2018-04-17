package powerups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import board.Location;
import game.GameDummy;
import pieces.Piece;
import powerups.PowerObject.Rarity;

/**
 * Test SecondEffort PowerAction.
 *
 * @author knorms
 *
 */
public class SecondEffortTest {

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    SecondEffort s = new SecondEffort();
    assertNotNull(s);
    assertEquals(Rarity.COMMON, s.getRarity());
  }

  /**
   * Test execution of SecondEffort power action.
   */
  @Test
  public void actTest() {
    GameDummy game = new GameDummy();
    SecondEffort s = new SecondEffort();
    Location whereCaptured = new Location(0, 1);
    Location end = new Location(2, 2);
    Piece piece = game.getPieceAt(whereCaptured);
    game.setMove(whereCaptured, end);

    // try to move knight from starting position
    s.act(whereCaptured, game);
    assertTrue(game.isEmpty(whereCaptured));
    assertEquals(piece, game.getPieceAt(end));

    // try to move rook to illegal loc through pawn
    whereCaptured = new Location(0, 0);
    end = new Location(4, 0);
    piece = game.getPieceAt(whereCaptured);
    game.setMove(whereCaptured, end);
    game.resetIllegalMovesAttempted();
    s.act(whereCaptured, game);
    assertEquals(3, game.getIllegalMovesAttempted());
    assertTrue(game.isEmpty(end));
    assertEquals(piece, game.getPieceAt(whereCaptured));

  }

}
