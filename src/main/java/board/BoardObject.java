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
   * Check whether board object is an empty square or not.
   * 
   * @return true if is an empty square.
   */
  boolean isEmpty();
  
  /**
   * Check whether board object is a piece or not.
   * 
   * @return true if is a piece.
   */
  boolean isPiece();
  

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
