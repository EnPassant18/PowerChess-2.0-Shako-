package board;

import game.Move;

/**
 * BoardObject represents an object on the board.
 *
 * @author knorms
 *
 */
public interface BoardObject {

  /**
   * Check whether board object can be jumped.
   *
   * @return true if can be jumped, false otherwise.
   */
  boolean canBeJumped();
  

  /**
   * Check whether move is legal.
   *
   * @param move
   *          Move to check for validity.
   * @param board
   *          Board on which to check for validity.
   * @return true if move is legal, false otherwise.
   */
  boolean move(Move move, Board board);

}
