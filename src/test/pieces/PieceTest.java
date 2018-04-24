package pieces;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import board.Board;
import board.Location;
import game.Color;
import powerups.PowerObject;

/**
 * Test piece protected methods.
 *
 * @author knorms
 *
 */
public class PieceTest {

  /**
   * validEnd method test.
   */
  @Test
  public void validEndTest() {
    Board board = new Board();
    Location start = new Location(4, 1);
    board.addBoardObject(start, new Pawn(Color.WHITE));

    // start is empty; TRUE because isValidEnd doesn't care if there is actually
    // a piece to be moved
    assertTrue(Piece.isValidEnd(new Location(3, 1), start, board));

    // end is empty
    assertTrue(Piece.isValidEnd(start, new Location(5, 1), board));

    // end is empty and move pattern is invalid for pawn
    assertTrue(Piece.isValidEnd(start, new Location(4, 6), board));

    // end piece is same color as start
    assertFalse(Piece.isValidEnd(start, new Location(1, 1), board));

    // end piece is diff color from start
    assertTrue(Piece.isValidEnd(start, new Location(6, 1), board));

  }

  /**
   * checkInLine test.
   */
  @Test
  public void checkInLineTest() {
    Board board = new Board();

    Location start = new Location(2, 0);
    Location end = new Location(2, lastRank);
    updateRowAndColDirs(start, end);

    // check empty horizontal
    assertTrue(Piece.checkInLine(start, end, board, rowDir, colDir));

    // check empty vertical
    start = new Location(2, 0);
    end = new Location(5, 0);
    updateRowAndColDirs(start, end);
    assertTrue(Piece.checkInLine(start, end, board, rowDir, colDir));

    // check empty diagonal
    start = new Location(2, 0);
    end = new Location(5, 3);
    updateRowAndColDirs(start, end);
    assertTrue(Piece.checkInLine(start, end, board, rowDir, colDir));

    // check piece obstructing
    board.addBoardObject(new Location(3, 1), new Rook(Color.BLACK));
    assertFalse(Piece.checkInLine(start, end, board, rowDir, colDir));

    // check PowerObject
    board.addBoardObject(new Location(4, 0),
        PowerObject.createRandPowerObject());
    start = new Location(2, 0);
    end = new Location(5, 0);
    updateRowAndColDirs(start, end);
    assertFalse(Piece.checkInLine(start, end, board, rowDir, colDir));

  }

  private final int lastRank = 7;
  private int colDir;
  private int rowDir;

  private void updateRowAndColDirs(Location start, Location end) {
    colDir = (int) Math.signum(end.getCol() - start.getCol());
    rowDir = (int) Math.signum(end.getRow() - start.getRow());
  }

}
