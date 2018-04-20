package game;

import board.Location;

/**
 * GameDummy is a game for whose outputs are pre-programmed for testing
 * purposes.
 *
 * @author knorms
 *
 */
public class GameDummy extends Game {
  private Move moveToGet;
  private int illegalMovesAttempted = 0;

  /**
   * Set output of game getMove method.
   *
   * @param start
   *          Move starting location.
   * @param end
   *          Move ending location.
   */
  public void setMove(Location start, Location end) {
    this.moveToGet = new Move(start, end);
  }

  @Override
  public Move getMove(Color color, Location start) {
    return moveToGet;
  }

  @Override
  public boolean validMove(Move move) {
    if (!super.validMove(move)) {
      illegalMovesAttempted++;
    }

    // return true after 3 invalid moves so method can finish executing
    return illegalMovesAttempted >= 3;
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
