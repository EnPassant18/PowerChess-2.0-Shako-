package powerups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import board.Location;
import game.GameDummy;
import pieces.Piece;
import powerups.PowerObject.Rarity;
import utility.Pair;

/**
 * Test Adjust PowerAction.
 *
 * @author knorms
 *
 */
public class AdjustTest {

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    Adjust a = new Adjust();
    assertNotNull(a);
    assertEquals(Rarity.COMMON, a.getRarity());
  }

  /**
   * Test execution of SecondEffort power action.
   */
  @Test
  public void actTest() {
    GameDummy game = new GameDummy();
    Adjust a = new Adjust();
    Location whereCaptured = new Location(3, 3);
    Location end = new Location(3, 4);

    // move bishop out of grid-lock
    game.forceMove(
        new Pair<Location, Location>(new Location(0, 2), whereCaptured));
    Piece piece = game.getPieceAt(whereCaptured);
    game.setMove(whereCaptured, end);

    // try normally illegal move: bishop to horzontally adjacent, empty square
    a.act(whereCaptured, game);
    assertEquals(0, game.getIllegalMovesAttempted());
    assertTrue(game.isEmpty(whereCaptured));
    assertEquals(piece, game.getPieceAt(end));

    // try to move knight to adjacent, non-empty square
    whereCaptured = new Location(0, 1);
    end = new Location(0, 2);
    piece = game.getPieceAt(whereCaptured);
    game.setMove(whereCaptured, end);
    a.act(whereCaptured, game);
    assertEquals(piece, game.getPieceAt(whereCaptured));
    assertEquals(0, game.getIllegalMovesAttempted());

  }

}
