package pieces;

import board.Board;
import board.Location;
import game.Color;
import game.Move;

/**
 * Queen class that represents the Queen piece.
 *
 * @author dwoods
 *
 */
public class Queen extends Piece {
  private static final int RANK = 9;

  /**
   * Construct Queen of the specified color.
   *
   * @param color
   *          Piece color.
   */
  public Queen(Color color) {
    super(color, RANK);
  }

  @Override
  public boolean canBePromotedTo() {
    return true;
  }

  @Override
  public boolean move(Move move, Board board) {
    Location start = move.getStart();
    Location end = move.getEnd();

    if (start.equals(end)) {
      return false;
    }
    int colDir = (int) Math.signum(end.getCol() - start.getCol());
    int rowDir = (int) Math.signum(end.getRow() - start.getRow());
    return checkInLine(start, end, board, rowDir, colDir);
  }
}
