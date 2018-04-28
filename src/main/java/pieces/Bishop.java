package pieces;

import board.Board;
import board.Location;
import game.Color;
import game.Move;

/**
 * Bishop class that represents the bishop piece.
 *
 * @author dwoods
 *
 */
public class Bishop extends Piece {

  /**
   * Construct Bishop of the specified color.
   *
   * @param color
   *          Piece color.
   */
  public Bishop(Color color) {
    super(color);
  }

  @Override
  public boolean canBePromotedTo() {
    return true;
  }

  @Override
  public boolean move(Move move, Board board) {
    Location start = move.getStart();
    Location end = move.getEnd();

    int colDif = end.getCol() - start.getCol();
    int rowDif = end.getRow() - start.getRow();

    if (Math.abs(rowDif) != Math.abs(colDif)) {
      return false;
    }

    int colDir = (int) Math.signum(colDif);
    int rowDir = (int) Math.signum(rowDif);
    if (rowDir == 0 || colDir == 0) {
      return false;
    }

    return checkInLine(start, end, board, rowDir, colDir);
  }

  @Override
  public int getRank() {
    // TODO Auto-generated method stub
    return 0;
  }
}
