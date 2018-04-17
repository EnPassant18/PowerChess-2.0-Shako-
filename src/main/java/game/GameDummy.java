package game;

import board.Location;
import utility.Pair;

/**
 * GameDummy is a game for whose outputs are pre-programmed for testing
 * purposes.
 *
 * @author knorms
 *
 */
public class GameDummy extends Game {
  private Pair<Location, Location> moveToGet;
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
    this.moveToGet = new Pair<Location, Location>(start, end);
  }

  @Override
  public Pair<Location, Location> getMove(Color color, Location start) {
    return moveToGet;
  }

  @Override
  public boolean makeMove(Pair<Location, Location> move) {
    if (!super.makeMove(move)) {
      illegalMovesAttempted++;
    }
    /*
     * return true after attempting 3 illegal moves so method can finish
     * executing
     */
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
