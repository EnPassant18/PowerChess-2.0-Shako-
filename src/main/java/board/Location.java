package board;

import java.util.Objects;

/**
 * Location represents a location on the board.
 *
 * @author knorms
 *
 */
public class Location implements BoardLoc {
  private final int row;
  private final int col;

  private static final int ALPHA_START = 65;

  /**
   * Constructs a board location with the specified row and column.
   *
   * @param row
   *          Row of location on board.
   * @param col
   *          Column of location on board.
   * @throws IllegalArgumentException
   *           if passed illegal row and/or column.
   */
  public Location(int row, int col) throws IllegalArgumentException {
    try {
      assert row >= 0 && row < Board.SIZE;
      assert col >= 0 && col < Board.SIZE;
      this.row = row;
      this.col = col;
    } catch (AssertionError e) {
      throw new IllegalArgumentException(
          String.format("ERROR: illegal board location (%d, %d).", row, col));
    }
  }

  @Override
  public int getRow() {
    return row;
  }

  @Override
  public int getCol() {
    return col;
  }

  @Override
  public boolean equals(Object obj) {
    try {
      return ((Location) obj).getRow() == row
          && ((Location) obj).getCol() == col;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  @Override
  public String toString() {
    return String.format("%c%d", (char) (col + ALPHA_START), row);
  }

}
