package poweractions;

import board.Location;

/**
 * Represents a power action that moves a piece.
 *
 * @author Brad
 *
 */
public interface PieceMover {

  /**
   * Get end location of moved piece.
   *
   * @return end location.
   */
  Location getEndLocation();

}
