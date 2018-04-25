package poweractions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import board.Location;
import game.Color;
import game.GameDummy;
import game.Move;
import pieces.Piece;
import players.DummyPlayer;
import powerups.PowerObject.Rarity;
import repl.ChessReplUtils;

/**
 * Test SecondEffort PowerAction.
 *
 * @author knorms
 *
 */
public class SecondEffortTest {

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    SecondEffort s = new SecondEffort();
    assertNotNull(s);
    assertEquals(Rarity.COMMON, s.getRarity());
  }

  /**
   * Test execution of power action.
   */
  @Test
  public void actTest() {
    GameDummy game = new GameDummy();
    DummyPlayer whitePlayer = new DummyPlayer(Color.WHITE);
    DummyPlayer blackPlayer = new DummyPlayer(Color.BLACK);

    game.addPlayer(whitePlayer);
    game.addPlayer(blackPlayer);

    SecondEffort s = new SecondEffort();

    // knight "captured" SecondEffort at starting location
    Location whereCaptured = new Location(0, 1);
    Location end = new Location(2, 2);
    Piece piece = game.getPieceAt(whereCaptured);
    whitePlayer.setMove(new Move(whereCaptured, end));

    // Move knight "again"
    s.act(whereCaptured, game);
    assertTrue(game.isEmpty(whereCaptured));
    assertEquals(piece, game.getPieceAt(end));

    // Make rook try illegal move on Second Effort
    whereCaptured = new Location(0, 0);
    end = new Location(4, 0);
    piece = game.getPieceAt(whereCaptured);
    whitePlayer.setMove(new Move(whereCaptured, end));
    Location trueEnd = new Location(0, 1);

    // then execute legal move, right to empty knight space
    game.setNextLegalMove(new Move(whereCaptured, trueEnd));

    // Move rook "again" (try illegal move, should fail then execute legal move)
    s.act(whereCaptured, game);
    System.out.println(ChessReplUtils.getBoardString(game.getBoard()));

    assertEquals(1, game.getIllegalMovesAttempted());
    assertTrue(game.isEmpty(end));
    assertTrue(game.isEmpty(whereCaptured));
    assertEquals(piece, game.getPieceAt(trueEnd));

  }

}
