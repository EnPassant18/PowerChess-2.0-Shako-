package repl;

import java.util.List;
import java.util.Locale;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.Game;
import game.Game.GameState;
import game.Move;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import players.CliPlayer;
import players.Player;
import poweractions.PowerAction;
import powerups.PowerObject;
import powerups.PowerObject.Rarity;

/**
 * Handler for chess project.
 *
 * @author Brad
 *
 */
public class ChessProjectHandler extends CommandMap {

  private Game game;
  private CliPlayer whitePlayer, blackPlayer;
  private boolean printBoard = true;

  /**
   * Default constructor for ChessProjectHandler.
   */
  public ChessProjectHandler() {

    // Handles the "new game" command
    add("new", "new game", s -> startNewGame());

    // Handles the "move" command
    add("move", "move %s %s", s -> move(s.get(1), s.get(2)));
    add("move", "move %s -> %s", s -> move(s.get(1), s.get(3)));

    // Handles printing
    add("print", "print board",
        s -> ChessReplUtils.getBoardString(game.getBoard()));
    add("print", "print on", s -> printOn());
    add("print", "print off", s -> printOff());

    // Handles pawn promotion
    add("promote", "promote %s", s -> promote(s.get(1)));

    // Handles spawning powerups
    add("spawn", "spawn %s %s", s -> spawn(s.get(1), s.get(2)));

    // Handles selecting powerup
    add("power", "power %s", s -> power(s.get(1)));

    // Handles executing poweraction
    add("action", "action %s", s -> action(s.get(1)));

  }

  private String power(String indexStr) {
    List<PowerAction> actionOptions = game.getActionOptions();
    PowerAction selected;
    try {
      int index = Integer.parseInt(indexStr);
      selected = actionOptions.get(index - 1);
      game.getActivePlayer().setAction(selected);
    } catch (NumberFormatException | IndexOutOfBoundsException e) {
      return String.format(
          "ERROR: Expected PowerAction "
              + "index selection integer between [1, %d] (inclusive).",
          actionOptions.size());
    }

    game.setGameState(GameState.WAITING_FOR_POWERUP_EXEC);
    return String.format("selected %s%n%s", selected.getClass().getSimpleName(),
        print());
  }

  private String action(final String str) {
    if (game.getGameState() != Game.GameState.WAITING_FOR_POWERUP_EXEC) {
      return "ERROR: No powerups available to execute.";
    }

    Location location = ChessReplUtils.parseLocation(str);
    if (location != null && game.validActionInput(location)) {
      game.executePowerAction(location);
      return print();
    }

    Piece piece =
        ChessReplUtils.parsePiece(str, game.getActivePlayer().getColor());
    if (piece != null && game.validActionInput(piece)) {
      game.executePowerAction(piece);
      return print();
    }

    return String.format("ERROR: Invalid input, expected: 'action %s'%n",
        game.getActionInputFormat());
  }

  private String startNewGame() {
    game = new Game();

    whitePlayer = new CliPlayer(Color.WHITE);
    blackPlayer = new CliPlayer(Color.BLACK);

    game.addPlayer(whitePlayer);
    game.addPlayer(blackPlayer);

    return print();
  }

  private String move(final String startPosition, final String endPosition) {

    if (game.getGameState() != Game.GameState.WAITING_FOR_MOVE) {
      return "ERROR: Not able to move.";
    }

    String errorString = "ERROR: Invalid start or end positions.";

    Location startLocation = ChessReplUtils.parseLocation(startPosition);
    Location endLocation = ChessReplUtils.parseLocation(endPosition);

    if (startLocation == null || endLocation == null) {
      return errorString;
    }

    Move move = new Move(startLocation, endLocation);
    Player player = game.getActivePlayer();
    player.setMove(move);

    try {
      game.turn(); // checks if valid before executing move
    } catch (IllegalMoveException e) {
      return e.getMessage();
    }

    return print();
  }

  private String printOn() {
    printBoard = true;
    return "";
  }

  private String printOff() {
    printBoard = false;
    return "";
  }

  private String spawn(final String locationString, final String rarityString) {

    Location location = ChessReplUtils.parseLocation(locationString);
    Rarity rarity;
    try {
      rarity = Rarity.valueOf(rarityString.toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException e) {
      return "ERROR: Invalid rarity.";
    }

    if (location == null) {
      return "ERROR: Invalid location.";
    }

    PowerObject powerObject = PowerObject.ofRarity(rarity);
    game.spawnPowerObject(location, powerObject);

    return print();
  }

  private String promote(final String pieceStr) {
    if (game.getGameState() != Game.GameState.WAITING_FOR_PROMOTE) {
      return "ERROR: Not able to promote.";
    }

    Player player = game.getActivePlayer();
    Color color = player.getColor();
    Piece p = ChessReplUtils.parsePiece(pieceStr, color);

    if (p == null || p instanceof King || p instanceof Pawn) {
      return "ERROR: Invalid promotion choice.";
    }

    player.setPromotion(p);
    game.executePromotion();

    return print();
  }

  private String print() {
    return printBoard ? printBoardState() : "";
  }

  private String printBoardState() {
    String header;

    Game.GameState gameState = game.getGameState();

    switch (gameState) { // todo: reformat
      case WAITING_FOR_MOVE:
        header = game.whiteToMove() ? "White to move.\n" : "Black to move.\n";
        break;
      case WAITING_FOR_PROMOTE:
        header =
            game.whiteToMove() ? "White to promote.\n" : "Black to promote.\n";
        break;
      case WAITING_FOR_POWERUP_CHOICE:
        StringBuilder powerupText = new StringBuilder();
        powerupText.append(game.whiteToMove() ? "White to choose powerup.\n"
            : "Black to choose powerup.\n");
        powerupText.append("command format: 'power [index]'\n");
        List<PowerAction> actionOptions = game.getActionOptions();
        for (int i = 0; i < actionOptions.size(); i++) {
          powerupText.append(String
              .format("%d. %s%n", i + 1, actionOptions.get(i)).toString());
        }
        header = powerupText.toString();
        break;
      case WAITING_FOR_POWERUP_EXEC:
        StringBuilder actionFormat = new StringBuilder();
        actionFormat.append(
            game.whiteToMove() ? "White to execute " : "Black to execute ");
        String action =
            game.getActivePlayer().getAction().getClass().getSimpleName();
        actionFormat
            .append(String.format("%s%n%s", action, "command format: "));
        actionFormat.append(
            String.format("'action %s'%n", game.getActionInputFormat()));
        header = actionFormat.toString();
        break;
      default:
        header = gameState.toString() + "\n";
        break;
    }

    return printBoardState(header);
  }

  private String printBoardState(final String header) {
    String boardString = ChessReplUtils.getBoardString(game.getBoard());
    if (header.equals("")) {
      return boardString;
    }
    return "\n" + header + boardString + "\n";
  }

}
