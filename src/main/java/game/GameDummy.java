package game;

import board.IllegalMoveException;

/**
 * GameDummy is a game for whose outputs are pre-programmed for testing
 * purposes.
 *
 * @author knorms
 *
 */
public class GameDummy extends Game {
  private Move nextMove;
  private int illegalMovesAttempted = 0;

  /**
   * Set the active player's next move.
   *
   * @param move
   *          Move to set for active player.
   */
  private void setMove(Move move) {
    getActivePlayer().setMove(move);
  }

  /**
   * Set next move legal to execute if an illegal move is attempted.
   *
   * @param move
   *          Next legal move.
   */
  public void setNextLegalMove(Move move) {
    nextMove = move;
  }

  @Override
  public boolean validMove(Move move) throws IllegalMoveException {
    if (!super.validMove(move) && nextMove != null) {
      illegalMovesAttempted++;
      setMove(nextMove);
      nextMove = null;
      return false;
    }

    return true;
  }

  /**
   * @return the number of illegalMovesAttempted
   */
  public int getIllegalMovesAttempted() {
    return illegalMovesAttempted;
  }

  /**
   * Reset illegalMovesAttempted to 0.
   */
  public void resetIllegalMovesAttempted() {
    this.illegalMovesAttempted = 0;
  }

}
