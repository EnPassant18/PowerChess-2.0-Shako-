package board;

/**
 * An object that has a location on the chess board.
 *
 * @author knorms
 *
 */
public interface BoardLoc {
  /**
   * Get row on board.
   *
   * @return Integer representing row location on board.
   */
  int getRow();

  /**
   * Get column on board.
   *
   * @return Integer representing column location on board.
   */
  int getCol();
}
