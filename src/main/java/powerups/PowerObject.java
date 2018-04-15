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
public interface PowerObject extends BoardObject {

  /**
   * Rarity represents the frequency with which a given PowerObject may spawn on
   * the board.
   *
   * @author knorms
   *
   */
  enum Rarity {
    COMMON, RARE, LEGENDARY
  }

  /**
   * Get rarity.
   *
   * @return rarity of power object.
   */
  Rarity getRarity();

  /**
   * Get a list of PowerActions allowed upon capturing this PowerObject.
   *
   * @return randomly generated list of 3 PowerActions.
   */
  List<PowerAction> getPowerActions();

  @Override
  default boolean move(Location start, Location end,
      Map<Location, BoardObject> spaces) {
    return false; // powerobjects cannot be moved
  }

}
