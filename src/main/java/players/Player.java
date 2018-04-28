package players;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.Move;
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
  private PowerAction action;

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
    return color;
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
          "ERROR: called getMove() before setMove() in Player.");
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
   */
  public void setPromotion(Piece promotion) {
    newPiece = promotion;
  }

  /**
   * Ask player how they would like to promote their pawn.
   *
   * @return Piece that player would two which player would like to promote.
   */
  public Piece getPromotion() {
    Piece rtn = newPiece;
    newPiece = null;
    return rtn;
  }

  /**
   * Set player PowerAction selection.
   *
   * @param action
   *          PowerActions player selected.
   */
  public void setAction(PowerAction action) {
    this.action = action;
  }

  /**
   * Get player PowerAction selection.
   *
   * @return Player selected PowerAction.
   * @throws IllegalStateException
   *           If getAction was called before player PowerAction selection was
   *           set using setAction.
   */
  public PowerAction getAction() throws IllegalStateException {
    if (action == null) {
      throw new IllegalStateException(
          "ERROR: called getAction() before setAction() in Player.");
    }
    return action;
  }

  /**
   * Check whether input is valid for player's power action.
   *
   * @param input
   *          Input for PowerAction.
   * @return true if valid input, otherwise false.
   */
  public boolean validActionInput(Object input) {
    if (action == null) {
      return false;
    }

    return action.validInput(input);
  }

  /**
   * Execute player's power action.
   *
   * @param input
   *          Input to execute PowerAction.
   */
  public void executeAction(Object input) {
    action.act(input);
    action = null;
  }

  /**
   * Get the desired input format to execute player's selected PowerAction.
   *
   * @return the desired input format to execute player's selected PowerAction.
   */
  public String getActionInputFormat() {
    if (action != null) {
      return action.inputFormat();
    }
    return null;
  }

}
