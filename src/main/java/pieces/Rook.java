package pieces;

import board.Board;
import board.Location;
import game.Color;
import game.Move;

/**
 * Rook class that represents the rook piece.
 *
 * @author dwoods
 *
 */
public class Rook extends Piece {

  /**
   * Construct Rook of the specified color.
   *
   * @param color
   *          Piece color.
   */
  public Rook(Color color) {
    super(color, 5);
  }

  @Override
  public boolean canBePromotedTo() {
    return true;
  }

  @Override
  public boolean move(Move move, Board board) {
    Location start = move.getStart();
    Location end = move.getEnd();

    if (start.getRow() != end.getRow() && start.getCol() != end.getCol()) {
      return false;
    }
    if (start.equals(end)) {
      return false;
    }
    int colDir = (int) Math.signum(end.getCol() - start.getCol());
    int rowDir = (int) Math.signum(end.getRow() - start.getRow());
    if (rowDir != 0 && colDir != 0) {
      return false;
    }
    return checkInLine(start, end, board, rowDir, colDir);
  }
}
