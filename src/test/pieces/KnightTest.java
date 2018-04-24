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

/**
 * Test Knight piece.
 *
 * @author knorms
 *
 */
public class KnightTest {

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    Knight k = new Knight(Color.BLACK);
    assertNotNull(k);
    assertTrue(k.canBeJumped());
    assertFalse(k.getMoved());
    assertEquals(Color.BLACK, k.getColor());

    k = new Knight(Color.WHITE);
    assertNotNull(k);
    assertTrue(k.canBeJumped());
    assertFalse(k.getMoved());
    assertEquals(Color.WHITE, k.getColor());
  }

  /**
   * Check knight move.
   */
  @Test
  public void moveTest() {
    Board board = new Board();

    Location start = new Location(0, 1);
    Knight k = (Knight) board.getPieceAt(start);

    // valid move jumping over pawns going up-left
    Move move = new Move(start, new Location(2, 0));
    assertTrue(k.move(move, board));

    // valid move jumping over pawns going up-right
    move = new Move(start, new Location(2, 2));
    assertTrue(k.move(move, board));

    // move ending on own piece
    move = new Move(start, new Location(1, 3));
    assertFalse(k.move(move, board));

    start = new Location(4, 3);
    k = new Knight(Color.WHITE);
    board.addBoardObject(start, k);

    // valid move jumping over empty spaces going down-left
    move = new Move(start, new Location(2, 4));
    assertTrue(k.move(move, board));

    move = new Move(start, new Location(3, 5));
    assertTrue(k.move(move, board));

    // valid move jumping over empty spaces going down-right
    move = new Move(start, new Location(2, 2));
    assertTrue(k.move(move, board));

    move = new Move(start, new Location(3, 1));
    assertTrue(k.move(move, board));

    // capturing
    move = new Move(start, new Location(6, 4));
    assertTrue(k.move(move, board));

    Location objLoc = new Location(2, 4);
    board.addBoardObject(objLoc, PowerObject.createRandPowerObject());

    // land on powerobject
    move = new Move(start, objLoc);
    assertTrue(k.move(move, board));

    // jump over powerobject
    objLoc = new Location(3, 3);
    board.addBoardObject(objLoc, PowerObject.createRandPowerObject());
    move = new Move(start, new Location(3, 5));
    assertTrue(k.move(move, board));

    // TODO can't move onto invulnerable space/piece
    objLoc = new Location(2, 4);
    board.addBoardObject(objLoc, new Invulnerability());
    move = new Move(start, objLoc);
    // assertFalse(k.move(move, board));

  }

}
