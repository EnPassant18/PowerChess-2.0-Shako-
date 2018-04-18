package game;

import java.util.List;

import board.Location;
import pieces.Piece;
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
   * Get move by player of piece at a specified location.
   *
   * @param start
   *          Starting location of piece to be moved.
   * @return pair of locations representing the start and ending locations of
   *         chosen move.
   */
  Pair<Location, Location> getMove(Location start);

  /**
   * Ask player how they would like to promote their pawn.
   *
   * @return Piece that player would two which player would like to promote.
   */
  Piece getPromotion();

  /**
   * Check whether player can castle long.
   *
   * @return true if player may castle long, otherwise false.
   */
  boolean getCanCastleLong();

  /**
   * Check whether player can castle short.
   *
   * @return true if player may castle short, otherwise false.
   */
  boolean getCanCastleShort();

  /**
   * Set indicator for whether player can castle long.
   *
   * @param canCastleLong
   *          Boolean indicating whether player can execute castle long.
   */
  void setCanCastleLong(boolean canCastleLong);

  /**
   * Set indicator for whether player can castle short.
   *
   * @param canCastleShort
   *          Boolean indicating whether player can execute castle short.
   */
  void setCanCastleShort(boolean canCastleShort);

  /**
   * Get player PowerAction selection.
   *
   * @param actions
   *          Set of PowerActions player may choose from.
   * @return selected PowerAction.
   */
  PowerAction selectPowerAction(List<PowerAction> actions);

}
