package game;

import board.Location;

/**
 * Pair of objects.
 *
 * @author knorms
 *
 */
public class Move {
  private Location start;
  private Location end;

  /**
   * Constructs a pair of objects.
   *
   * @param start
   *          Start of move.
   * @param end
   *          End of move.
   */
  public Move(Location start, Location end) {
    this.start = start;
    this.end = end;
  }

  /**
   * @return the start
   */
  public Location getStart() {
    return start;
  }

  /**
   * @param start
   *          the start to set
   */
  public void setStart(Location start) {
    this.start = start;
  }

  /**
   * @return the end
   */
  public Location getEnd() {
    return end;
  }

  /**
   * @param end
   *          the end to set
   */
  public void setEnd(Location end) {
    this.end = end;
  }

}
