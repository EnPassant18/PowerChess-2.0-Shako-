package repl;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.Game;
import game.Move;
import players.CliPlayer;

/**
 * Handler for chess project.
 *
 * @author Brad
 *
 */
public class ChessProjectHandler extends CommandMap {

  private boolean whiteToMove;
  private Game game;
  private CliPlayer whitePlayer, blackPlayer;
  private static final int SIZE = 8;

  /**
   * Default constructor for ChessProjectHandler.
   */
  public ChessProjectHandler() {

    // Handles the "new game" command
    add("new", "new game", s -> startNewGame());

    // Handles the "move" command
    add("move", "move %s %s", s -> move(s.get(1), s.get(2)));
    add("move", "move %s -> %s", s -> move(s.get(1), s.get(3)));
  }

  private String startNewGame() {
    game = new Game();

    whitePlayer = new CliPlayer(Color.WHITE);
    blackPlayer = new CliPlayer(Color.BLACK);

    game.addPlayer(whitePlayer);
    game.addPlayer(blackPlayer);

    return printBoardState();
  }

  private String move(final String startPosition, final String endPosition) {
    String errorString = "ERROR: Invalid start or end positions.";

    Location startLocation = ChessReplUtils.parseMove(startPosition);
    Location endLocation = ChessReplUtils.parseMove(endPosition);

    if (startLocation == null || endLocation == null) {
      return errorString;
    }

    Move move = new Move(startLocation, endLocation);
    CliPlayer player = whiteToMove() ? whitePlayer : blackPlayer;

    System.out.println(String.format("(%d, %d) -> (%d, %d)",
        startLocation.getRow(), startLocation.getCol(), endLocation.getRow(),
        endLocation.getCol()));

    player.setMove(move);

    try {
      game.turn(); // checks if valid before executing move
    } catch (IllegalMoveException e) {
      return e.getMessage();
    }

    return printBoardState();
  }

  private Boolean whiteToMove() {
    return game.getActivePlayerIndex() == 0;
  }

  private String printBoardState() {
    String header = whiteToMove() ? "White to move.\n" : "Black to move.\n";
    String boardString = ChessReplUtils.getBoardString(game.getBoard());
    return "\n" + header + boardString + "\n";
  }

}
