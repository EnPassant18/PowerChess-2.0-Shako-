package players;

import java.util.List;

import board.Location;
import game.Color;
import game.Move;
import pieces.Piece;
import powerups.PowerAction;

/**
 * A Player can participate in a game of Chess.
 *
 * @author knorms
 *
 */
public abstract class Player {
  private final Color color;

  /**
   * Constructs player of the specified color.
   *
   * @param color
   *          Color of player's pieces.
   */
  public Player(Color color) {
    this.color = color;
  }

  /**
   * Get player color.
   *
   * @return player piece color.
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * Get player's next move.
   *
   * @return pair of locations representing the starting and ending locations of
   *         a move.
   */
  public abstract Move getMove();

  /**
   * Get move by player of piece at a specified location.
   *
   * @param start
   *          Starting location of piece to be moved.
   * @return pair of locations representing the start and ending locations of
   *         chosen move.
   */
  public abstract Move getMove(Location start);

  /**
   * Ask player how they would like to promote their pawn.
   *
   * @return Piece that player would two which player would like to promote.
   */
  public abstract Piece getPromotion();

  /**
   * Check whether player can castle long.
   *
   * @return true if player may castle long, otherwise false.
   */
  public abstract boolean getCanCastleLong();

  /**
   * Check whether player can castle short.
   *
   * @return true if player may castle short, otherwise false.
   */
  public abstract boolean getCanCastleShort();

  /**
   * Set indicator for whether player can castle long.
   *
   * @param canCastleLong
   *          Boolean indicating whether player can execute castle long.
   */
  public abstract void setCanCastleLong(boolean canCastleLong);

  /**
   * Set indicator for whether player can castle short.
   *
   * @param canCastleShort
   *          Boolean indicating whether player can execute castle short.
   */
  public abstract void setCanCastleShort(boolean canCastleShort);

  /**
   * Get player PowerAction selection.
   *
   * @param actions
   *          Set of PowerActions player may choose from.
   * @return selected PowerAction.
   */
  public abstract PowerAction selectPowerAction(List<PowerAction> actions);

}
