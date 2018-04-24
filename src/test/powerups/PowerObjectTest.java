package powerups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import board.Board;
import board.Location;
import game.Move;
import powerups.PowerObject.Rarity;

/**
 * Test PowerObject.
 *
 * @author knorms
 *
 */
public class PowerObjectTest {

  /**
   * Test constructor.
   */
  public void constructorTest() {
    PowerObject obj = new PowerObject(Rarity.COMMON);
    assertNotNull(obj);
    assertEquals(Rarity.COMMON, obj.getRarity());

    // can be jumped
    assertTrue(obj.canBeJumped());

    // can never be moved
    Board board = new Board();
    Location objLoc = new Location(4, 4);
    board.addBoardObject(objLoc, obj);
    assertFalse(obj.move(new Move(objLoc, new Location(4, 5)), board));
  }

  /**
   * Test create random power object method.
   */
  @Test
  public void createRandTest() {
    PowerObject pObj = PowerObject.createRandPowerObject();
    assertNotNull(pObj);
    while (pObj.getRarity() != Rarity.COMMON) {
      pObj = PowerObject.createRandPowerObject();
    }

    assertEquals(Rarity.COMMON, pObj.getRarity());

    // TODO how to test randomly generated obj?
  }

}
