package game;

import java.util.List;

import board.Location;
import powerups.PowerAction;
import utility.Pair;

/**
 * Player can participate in a game of Chess.
 *
 * @author knorms
 *
 */
public interface Player {

  /**
   * Get player piece color.
   *
   * @return player piece color.
   */
  Color getColor();

  /**
   * Get player's next move.
   *
   * @return pair of locations representing the starting and ending locations of
   *         a move.
   */
  Pair<Location, Location> getMove();

  /**
   * Get player PowerAction selection.
   *
   * @param actions
   *          Set of PowerActions player may choose from.
   * @return selected PowerAction.
   */
  PowerAction selectPowerAction(List<PowerAction> actions);

}
