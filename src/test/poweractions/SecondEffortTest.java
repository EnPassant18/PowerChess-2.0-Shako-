package poweractions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import board.Location;
import game.Game;
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
    Game game = new Game();
    Location whereCaptured = new Location(1, 1);

    SecondEffort s = new SecondEffort(game, whereCaptured);
    assertNotNull(s);
    assertEquals(Rarity.COMMON, s.getRarity());
    assertEquals(game, s.getGame());
    assertEquals(whereCaptured, s.getWhereCaptured());
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
    // check second legal move

    // check second illegal move onto own piece

    // check second illegal move onto empty space
  }

  /**
   * Test execution.
   */
  @Test
  public void actTest() {
    // execute second legal move

    // try to execute second illegal move

  }

}
