package players;

import java.util.List;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.IllegalPromotionException;
import game.Move;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import poweractions.PowerAction;

/**
 * A Player can participate in a game of Chess.
 *
 * @author knorms
 *
 */
public abstract class Player {
  private final Color color;
  private Move move;
  private Piece newPiece; // for promotion

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
   * @throws IllegalStateException
   *           If getMove was called before player move was set using setMove.
   */
  public Move getMove() throws IllegalStateException {
    if (move == null) {
      throw new IllegalStateException(
          "ERROR: called getMove() before setMove() in Player.");
    }
    Move output = move;
    move = null;
    return output;
  }

  /**
   * Set the player's next move.
   *
   * @param move
   *          Next move to be executed by player.
   */
  public void setMove(Move move) {
    this.move = move;
  }

  /**
   * Get move by player of piece at a specified location.
   *
   * @param start
   *          Starting location of piece to be moved.
   * @return pair of locations representing the start and ending locations of
   *         chosen move.
   * @throws IllegalMoveException
   *           If player attempts to move a different piece than the one at teh
   *           starting location.
   */
  public Move getMove(Location start) throws IllegalMoveException {
    if (move == null) {
      throw new IllegalStateException(
          "ERROR: called getMove() before setMove() in CliPlayer.");
    } else if (!move.getStart().equals(start)) {
      throw new IllegalMoveException(
          String.format("ERROR: Player must move %s next.", start.toString()));
    }
    Move rtn = move;
    move = null;
    return rtn;
  }

  /**
   * Set the player's next choice for promotion.
   *
   * @param promotion
   *          Piece to promote to.
   * @throws IllegalPromotionException
   *           If player chooses illegal piece (i.e. Pawn or King).
   */
  public void setPromotion(Piece promotion) throws IllegalPromotionException {
    if (promotion instanceof Pawn || promotion instanceof King) {
      throw new IllegalPromotionException(
          String.format("ERROR: Cannot promote to %s.",
              promotion.getClass().getSimpleName()));
    }
    newPiece = promotion;
  }

  /**
   * Ask player how they would like to promote their pawn.
   *
   * @return Piece that player would two which player would like to promote.
   * @throws IllegalPromotionException
   *           If player chooses illegal promotion piece type (Pawn or King).
   */
  public Piece getPromotion() throws IllegalPromotionException {
    if (newPiece == null) {
      throw new IllegalPromotionException(
          "ERROR: called getPromotion() before setPromotion in CliPlayer.");
    }
    Piece rtn = newPiece;
    newPiece = null;
    return rtn;
  }

  /**
   * Get player PowerAction selection.
   *
   * @param actions
   *          Set of PowerActions player may choose from.
   * @return selected PowerAction.
   */
  public abstract PowerAction selectPowerAction(List<PowerAction> actions);

}
