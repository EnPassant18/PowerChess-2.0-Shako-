package pieces;

import board.Board;
import board.BoardObject;
import board.Location;
import game.Color;
import powerups.Invulnerability;
import powerups.PowerUp;

/**
 * Piece represents a chess piece.
 *
 * @author knorms
 *
 */
public abstract class Piece implements BoardObject {

  private Color color;
  private boolean moved;
  private int rank;

  /**
   * Construct a piece of the specified color.
   *
   * @param color
   *          Piece color.
   * @param rank
   *          Piece rank.
   */
  public Piece(Color color, int rank) {
    this.color = color;
    moved = false;
    this.rank = rank;
  }

  @Override
  public boolean canBeJumped() {
    return true;
  }

  /**
   * Returns true if a pawn can promote to the desired piece and false
   * otherwise. Defaults to false.
   *
   * @return True if the pawn can promote to the desired piece and false
   *         otherwise.
   */
  public boolean canBePromotedTo() {
    return false;
  }

  /**
   * Get piece color.
   *
   * @return color of piece (WHITE or BLACK).
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * Set the color of the piece.
   *
   * @param color
   *          color of piece to set to
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Get piece rank.
   *
   * @return Piece rank.
   */
  public int getRank() {
    return this.rank;
  }

  /**
   * Is called after a piece makes a move. Sets the moved boolean to true.
   */
  public void setMoved() {
    this.moved = true;
  }

  /**
   * Checks if the piece has moved since the start of the game.
   *
   * @return True if piece has moved and false if it hasn't.
   */
  boolean getMoved() {
    return this.moved;
  }

  /**
   * Check whether end location is valid for given piece (i.e. must be empty or
   * contain piece of opposite color without invulnerability).
   *
   * @param start
   *          Start location.
   * @param end
   *          End location.
   * @param board
   *          Board.
   * @return true if end location is valid for piece at starting location,
   *         otherwise false.
   */
  public static boolean isValidEnd(Location start, Location end, Board board) {
    Piece endP = board.getPieceAt(end);
    Piece startP = board.getPieceAt(start);

    PowerUp power = board.getPowerUpAt(end);
    if (power instanceof Invulnerability) {
      return false;
    }

    if (endP != null && startP != null && !(endP instanceof GhostPawn)) {
      if (endP.getColor() == startP.getColor()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks all spaces in a straight line from start to end location. If the
   * BoardObject at each location between start and end is not empty and not a
   * piece, will return false as player is attempting to make an invalid move.
   * Finally, checks that the end location is not a piece of the same color as
   * the start location.
   *
   * @param start
   *          Location of the piece being moved.
   * @param end
   *          Location the piece wants to move to.
   * @param board
   *          Game board.
   * @param rowDir
   *          Direction to move in rows. Either -1, 0, or 1.
   * @param colDir
   *          Direction to move in columns. Either -1, 0, or 1.
   * @return True if piece can move from start to end. False if it can't.
   */
  public static boolean checkInLine(Location start, Location end, Board board,
      int rowDir, int colDir) {
    try {
      Location check =
          new Location(start.getRow() + rowDir, start.getCol() + colDir);
      while (!check.equals(end)) {
        if (!board.isEmpty(check)) {
          return false;
        }
        check = new Location(check.getRow() + rowDir, check.getCol() + colDir);
      }
      return isValidEnd(start, end, board);

    } catch (IllegalArgumentException e) {
      return false; // if end not in line, return false
    }
  }

}
