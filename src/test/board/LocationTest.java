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
      loc = new Location(8, 0);
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
      loc = new Location(0, 8);
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

}
