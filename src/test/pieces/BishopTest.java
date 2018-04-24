package pieces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import board.Board;
import board.Location;
import game.Color;
import game.Move;
import powerups.Invulnerability;
import powerups.PowerObject;
import repl.ChessReplUtils;

/**
 * Test Bishop piece.
 *
 * @author knorms
 *
 */
public class BishopTest {

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    Bishop b = new Bishop(Color.BLACK);
    assertNotNull(b);
    assertTrue(b.canBeJumped());
    assertFalse(b.getMoved());
    assertEquals(Color.BLACK, b.getColor());

    b = new Bishop(Color.WHITE);
    assertNotNull(b);
    assertTrue(b.canBeJumped());
    assertFalse(b.getMoved());
    assertEquals(Color.WHITE, b.getColor());
  }

  /**
   * Check bishop move.
   */
  @Test
  public void moveTest() {
    Board board = new Board();

    Location start = new Location(0, 2);
    Bishop b = (Bishop) board.getPieceAt(start);

    // can't capture own piece
    Move move = new Move(start, new Location(1, 3));

    // can't move through pawns
    move = new Move(start, new Location(2, 0));
    assertFalse(b.move(move, board));

    start = new Location(4, 3);
    b = new Bishop(Color.WHITE);
    board.addBoardObject(start, b);
    System.out.println(ChessReplUtils.getBoardString(board));

    // right-diag through empty
    move = new Move(start, new Location(5, 4));
    assertTrue(b.move(move, board));

    move = new Move(start, new Location(2, 1));
    assertTrue(b.move(move, board));

    // left-diag through empty
    move = new Move(start, new Location(5, 2));
    assertTrue(b.move(move, board));

    move = new Move(start, new Location(2, 5));
    assertTrue(b.move(move, board));

    // capture
    move = new Move(start, new Location(6, 1));
    assertTrue(b.move(move, board));

    Location objLoc = new Location(3, 4);
    board.addBoardObject(objLoc, PowerObject.createRandPowerObject());

    // can't move through powerobject
    move = new Move(start, new Location(2, 5));
    assertFalse(b.move(move, board));

    // land on powerobject
    move = new Move(start, objLoc);
    assertTrue(b.move(move, board));

    // TODO can't move onto invulnerable space/piece
    board.addBoardObject(objLoc, new Invulnerability());
    // assertFalse(b.move(move, board));

  }

}
