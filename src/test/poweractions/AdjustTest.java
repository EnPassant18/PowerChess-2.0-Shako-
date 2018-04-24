package poweractions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import board.Location;
import game.Color;
import game.GameDummy;
import game.Move;
import pieces.Bishop;
import pieces.Piece;
import players.DummyPlayer;
import powerups.PowerObject.Rarity;
import repl.ChessReplUtils;

/**
 * Test Adjust PowerAction.
 *
 * @author knorms
 *
 */
public class AdjustTest {

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    Adjust a = new Adjust();
    assertNotNull(a);
    assertEquals(Rarity.COMMON, a.getRarity());
  }

  /**
   * Test execution of SecondEffort power action.
   */
  @Test
  public void actTest() {

    GameDummy game = new GameDummy();
    DummyPlayer whitePlayer = new DummyPlayer(Color.WHITE);
    DummyPlayer blackPlayer = new DummyPlayer(Color.BLACK);

    game.addPlayer(whitePlayer);
    game.addPlayer(blackPlayer);

    Adjust a = new Adjust();

    Location whereCaptured = new Location(3, 3);
    Location end = new Location(3, 4);

    Piece piece = new Bishop(Color.WHITE);
    game.getBoard().addBoardObject(whereCaptured, piece);

    whitePlayer.setMove(new Move(whereCaptured, end));

    // try normally illegal move: bishop to horzontally adjacent, empty square
    a.act(whereCaptured, game);
    assertEquals(0, game.getIllegalMovesAttempted());
    assertTrue(game.isEmpty(whereCaptured));
    assertEquals(piece, game.getPieceAt(end));

    System.out.println(ChessReplUtils.getBoardString(game.getBoard()));

    // try to move knight to adjacent, non-empty square
    Location trueEnd = new Location(1, 1); // clear pawn
    game.executeMove(new Move(trueEnd, new Location(2, 1)));
    whereCaptured = new Location(0, 1);
    end = new Location(0, 2);
    piece = game.getPieceAt(whereCaptured);
    whitePlayer.setMove(new Move(whereCaptured, end));
    whitePlayer.setNextLegalMove(new Move(whereCaptured, trueEnd));
    a.act(whereCaptured, game);
    assertEquals(piece, game.getPieceAt(trueEnd));
    assertTrue(game.isEmpty(whereCaptured));
    assertEquals(0, game.getIllegalMovesAttempted());
    assertTrue(whitePlayer.isTriedIllegalMove());

  }

}
