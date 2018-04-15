package pieces;

import board.BoardObject;
import game.Color;

/**
 * Piece represents a chess piece.
 *
 * @author knorms
 *
 */
public interface Piece extends BoardObject {

  /**
   * Get piece color.
   *
   * @return color of piece (WHITE or BLACK).
   */
  Color getColor();

}
