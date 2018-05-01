package pieces;

import board.Board;
import board.Location;
import game.Color;
import game.Move;

/**
 * Knight represents a knight chess piece.
 *
 * @author dwoods
 *
 */
public class Knight extends Piece {

  /**
   * Construct Knight of the specified color.
   *
   * @param color
   *          Piece color.
   */
  public Knight(Color color) {
    super(color, 3);
  }

  @Override
  public boolean canBePromotedTo() {
    return true;
  }

  @Override
  public boolean move(Move move, Board board) {
    Location start = move.getStart();
    Location end = move.getEnd();

    if (start.getRow() == end.getRow() || start.getCol() == end.getCol()) {
      return false;
    }
    int rowDif = end.getRow() - start.getRow();
    int colDif = end.getCol() - start.getCol();

    // If the knight cannot move from start to end in an L shape, return false.
    int absCol = Math.abs(colDif);
    int absRow = Math.abs(rowDif);
    if (!((absCol == 1 && absRow == 2) || (absCol == 2 && absRow == 1))) {
      return false;
    }

    if (checkRowFirst(start, end, board, rowDif, colDif)
        || checkColFirst(start, end, board, rowDif, colDif)) {
      return isValidEnd(start, end, board);
    }
    return false;
  }

  private boolean checkRowFirst(Location start, Location end, Board board,
      int rowDif, int colDif) {
    int rowDir = (int) Math.signum(rowDif);
    int colDir = (int) Math.signum(colDif);

    Location check = new Location(start.getRow() + rowDir, start.getCol());
    if (!board.isJumpable(check)) {
      return false;
    }
    if (rowDif == 2) {
      check = new Location(check.getRow() + rowDir, check.getCol());
    } else {
      check = new Location(check.getRow(), check.getCol() + colDir);
    }
    return board.isJumpable(check);
  }

  private boolean checkColFirst(Location start, Location end, Board board,
      int rowDif, int colDif) {
    int rowDir = (int) Math.signum(rowDif);
    int colDir = (int) Math.signum(colDif);

    Location check = new Location(start.getRow() + rowDir, start.getCol());
    if (!board.isJumpable(check)) {
      return false;
    }
    if (colDif == 2) {
      check = new Location(check.getRow(), check.getCol() + colDir);
    } else {
      check = new Location(check.getRow() + rowDir, check.getCol());
    }
    return board.isJumpable(check);
  }
}
