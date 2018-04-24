package board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test Location class.
 *
 * @author knorms
 *
 */
public class LocationTest {
  private static final int BEYOND_BOARD = 8;

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    Location loc = new Location(0, 0);
    assertEquals(0, loc.getRow());
    assertEquals(0, loc.getCol());

    try { // row min
      loc = new Location(-1, 0);
      fail(loc.toString());
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("ERROR: illegal board location"));
    }

    try { // row max
      loc = new Location(BEYOND_BOARD, 0);
      fail(loc.toString());
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("ERROR: illegal board location"));
    }

    try { // col min
      loc = new Location(0, -1);
      fail(loc.toString());
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("ERROR: illegal board location"));
    }

    try { // col max
      loc = new Location(0, BEYOND_BOARD);
      fail(loc.toString());
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("ERROR: illegal board location"));
    }

  }

  /**
   * Test equals method.
   */
  @Test
  public void equalsTest() {
    Location loc = new Location(2, 3);
    Location sameLoc = new Location(2, 3);
    assertEquals(loc, sameLoc);

    Location diffLoc = new Location(3, 2);
    assertNotEquals(loc, diffLoc);

  }

  /**
   * Test toString method.
   */
  @Test
  public void toStringTest() {
    Location loc = new Location(2, 3);
    assertEquals("D3", loc.toString());

    // test extremes
    loc = new Location(0, 0);
    assertEquals("A1", loc.toString());

    loc = new Location(BEYOND_BOARD - 1, BEYOND_BOARD - 1);
    assertEquals("H8", loc.toString());

  }

}
