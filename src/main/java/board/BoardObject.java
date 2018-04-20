package board;

import java.util.Map;

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
   * @param start
   *          Piece's starting location on the board.
   * @param end
   *          Board location to try moving to.
   * @param spaces
   *          List of board spaces that represent the current board state.
   * @return true if move is legal, false otherwise.
   */
  boolean move(Location start, Location end, Map<Location, BoardObject> spaces);

}
