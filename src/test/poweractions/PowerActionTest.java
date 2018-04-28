package poweractions;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import board.Location;
import game.Game;
import powerups.PowerObject.Rarity;

/**
 * Test PowerAction interface static methods.
 *
 * @author knorms
 *
 */
public class PowerActionTest {

  /**
   * Test method of generating list of up to 3 power actions at specified
   * rarity.
   */
  @Test
  public void ofRarityTest() {
    Game game = new Game();
    Location whereCaptured = new Location(1, 1);

    // common
    List<PowerAction> actions =
        PowerAction.ofRarity(Rarity.COMMON, game, whereCaptured);

    for (PowerAction action : actions) {
      assertEquals(Rarity.COMMON, action.getRarity());
    }

    // rare
    actions = PowerAction.ofRarity(Rarity.RARE, game, whereCaptured);

    for (PowerAction action : actions) {
      assertEquals(Rarity.RARE, action.getRarity());
    }

    // legendary
    actions = PowerAction.ofRarity(Rarity.LEGENDARY, game, whereCaptured);

    for (PowerAction action : actions) {
      assertEquals(Rarity.LEGENDARY, action.getRarity());
    }

  }

}
