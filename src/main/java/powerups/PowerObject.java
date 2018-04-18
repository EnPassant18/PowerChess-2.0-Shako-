package powerups;

import java.util.List;
import java.util.Map;

import board.BoardObject;
import board.Location;

/**
 * PowerObject represents a powerup that can appear on the game board and be
 * captured.
 *
 * @author knorms
 *
 */
public class PowerObject implements BoardObject {
  private Rarity rarity;

  /**
   * Rarity represents the frequency with which a given PowerAction will be
   * offered.
   *
   * @author knorms
   *
   */
  public enum Rarity {
    COMMON, RARE, LEGENDARY
  }

  /**
   * Construct PowerObject of specified rarity.
   *
   * @param rarity
   *          Rarity of PowerObject (Common, Rare, or Legendary).
   */
  public PowerObject(String rarity) {
    this.rarity = Rarity.valueOf(rarity);
  }

  /**
   * Get a list of PowerActions allowed upon capturing this PowerObject.
   *
   * @return randomly generated list of 3 PowerActions.
   */
  public List<PowerAction> getPowerActions() {
    return PowerAction.ofRarity(rarity);
  }

  @Override
  public boolean move(Location start, Location end,
      Map<Location, BoardObject> spaces) {
    return false; // powerobjects cannot be moved
  }

  @Override
  public boolean canBeJumped() {
    return false;
  }

}
