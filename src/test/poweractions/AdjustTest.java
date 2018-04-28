package poweractions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import board.Location;
import game.Game;
import powerups.PowerObject.Rarity;

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
    Game game = new Game();
    Location whereCaptured = new Location(1, 1);

    Adjust a = new Adjust(game, whereCaptured);
    assertNotNull(a);
    assertEquals(Rarity.COMMON, a.getRarity());
    assertEquals(game, a.getGame());
    assertEquals(whereCaptured, a.getWhereCaptured());
  }

  /**
   * Test inputFormat method.
   */
  @Test
  public void inputFormatTest() {

  }

  /**
   * Test validInput method.
   */
  @Test
  public void validInputTest() {
    // move onto adjacent empty (normally illegal move)

    // move onto adjace empty (normally legal move)

    // try to move onto adjacent non-empty
  }

  /**
   * Test execution.
   */
  @Test
  public void actTest() {

  }

}
