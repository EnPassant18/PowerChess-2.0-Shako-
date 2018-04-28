package players;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.Move;

/**
 * DummyPlayer for testing; can pre-set a back-up, valid move in case first move
 * is invalid.
 *
 * @author knorms
 *
 */
public class DummyPlayer extends Player {
  private Move nextLegalMove;
  private boolean triedIllegalMove = false;

  /**
   * Construct dummy player of specified color.
   *
   * @param color
   *          Player color.
   */
  public DummyPlayer(Color color) {
    super(color);
  }

  /**
   * Set next legal move that will be gotten if player attempts to give illegal
   * move.
   *
   * @param move
   *          Next legal move.
   */
  public void setNextLegalMove(Move move) {
    this.nextLegalMove = move;
  }

  @Override
  public Move getMove() {
    try {
      return super.getMove();
    } catch (IllegalStateException e) {
      triedIllegalMove = true;
      return nextLegalMove;
    }
  }

  @Override
  public Move getMove(Location start) {
    try {
      return super.getMove(start);
    } catch (IllegalStateException | IllegalMoveException e) {
      triedIllegalMove = true;
      return nextLegalMove;
    }
  }

  /**
   * @return the triedIllegalMove
   */
  public boolean isTriedIllegalMove() {
    return triedIllegalMove;
  }

  /**
   * @param triedIllegalMove
   *          the triedIllegalMove to set
   */
  public void setTriedIllegalMove(boolean triedIllegalMove) {
    this.triedIllegalMove = triedIllegalMove;
  }

}
