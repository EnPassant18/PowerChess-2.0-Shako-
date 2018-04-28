package pieces;

import board.Board;
import board.Location;
import game.Color;
import game.Move;

/**
 * King class that represents the King piece.
 *
 * @author dwoods
 *
 */
public class King extends Piece {
  private static final int CASTLE_DIST = -2;

  private boolean castling;

  /**
   * Construct King of the specified color.
   *
   * @param color
   *          Piece color.
   */
  public King(Color color) {
    super(color);
    castling = false;
  }

  @Override
  public boolean move(Move move, Board board) {
    Location start = move.getStart();
    Location end = move.getEnd();

    if (start.equals(end)) {
      return false;
    }
    int colDif = end.getCol() - start.getCol();
    int rowDif = end.getRow() - start.getRow();
    if (colDif == 2 && rowDif == 0) {
      return checkCastleShort(start, end, board);
    }
    if (colDif == CASTLE_DIST && rowDif == 0) {
      System.out.println("check long");
      return checkCastleLong(start, end, board);
    }
    if (Math.abs(rowDif) > 1 || Math.abs(colDif) > 1) {
      return false;
    }
    return isValidEnd(start, end, board);

  }

  private boolean checkCastleLong(Location start, Location end, Board board) {
    if (getMoved()) {
      return false;
    }
    Piece p = board.getPieceAt(new Location(end.getRow(), end.getCol() - 2));
    if (p == null || p.getMoved() || !(p instanceof Rook)) {
      return false;
    }
    Location check1 = new Location(start.getRow(), start.getCol() - 1);
    Location check2 = new Location(check1.getRow(), check1.getCol() - 1);
    Location check3 = new Location(check2.getRow(), check2.getCol() - 1);
    if (board.isEmpty(check1) && board.isEmpty(check2)
        && board.isEmpty(check3)) {
      castling = true;
      return true;
    }
    return false;
  }

  private boolean checkCastleShort(Location start, Location end, Board board) {
    if (getMoved()) {
      return false;
    }
    Piece p = board.getPieceAt(new Location(end.getRow(), end.getCol() + 1));
    if (p == null || p.getMoved() || !(p instanceof Rook)) {
      return false;
    }
    Location check1 = new Location(start.getRow(), start.getCol() + 1);
    Location check2 = new Location(check1.getRow(), check1.getCol() + 1);
    if (board.isEmpty(check1) && board.isEmpty(check2)) {
      castling = true;
      return true;
    }
    return false;
  }

  /**
   * Check whether castle (long or short) that was most recently inspected is
   * currently allowed.
   *
   * @return true if latest castle move that was checked is allowed, false
   *         otherwise.
   */
  public boolean getCastling() {
    return this.castling;
  }

  /**
   * Reset indicator for whether can castle.
   */
  public void resetCastling() {
    this.castling = false;
  }

  @Override
  public int getRank() {
    // TODO Auto-generated method stub
    return 0;
  }
}
