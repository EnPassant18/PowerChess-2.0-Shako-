package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import board.BoardObject;
import board.Location;
import powerups.PowerObject;
import repl.ChessReplUtils;

/**
 * Test game class.
 *
 * @author knorms
 *
 */
public class GameTest {

  /**
   * Test power object spawn.
   */
  @Test
  public void spawnPowerObjTest() {
    Game g = new Game();
    Location objLoc = new Location(4, 4);
    PowerObject powerObj = PowerObject.createRandPowerObject();
    g.spawnPowerObject(objLoc, powerObj);
    Collection<BoardObject> objs = g.getObjsAt(objLoc);
    assertTrue(objs.contains(powerObj));

    assertEquals(
        "RNBQKBNR\n" + "PPPPPPPP\n" + "xxxxxxxx\n" + "xxxxWxxx\n" + "xxxxxxxx\n"
            + "xxxxxxxx\n" + "pppppppp\n" + "rnbqkbnr",
        ChessReplUtils.getBoardString(g.getBoard()));

  }

}
