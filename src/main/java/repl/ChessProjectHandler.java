package repl;

import board.IllegalMoveException;
import board.Location;
import pieces.*;
import game.Color;
import game.Game;
import game.Move;
import powerups.PowerObject;
import players.Player;
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
  private boolean printBoard = true;
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
    
    // Handles printing
    add("print", "print board", s -> ChessReplUtils.getBoardString(game.getBoard()));
    add("print", "print on", s -> printOn());
    add("print", "print off", s -> printOff());
    
    // Handles pawn promotion
    add("promote", "promote %s", s -> promote(s.get(1)));
    
    // Handles spawning powerups
    add("spawn", "spawn %s %s", s -> spawn(s.get(1), s.get(2)));
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
    
    if(game.getGameState() != Game.GameState.WAITING_FOR_MOVE) {
      return "ERROR: Not able to move.";
    }
    
    String errorString = "ERROR: Invalid start or end positions.";

    Location startLocation = ChessReplUtils.parseMove(startPosition);
    Location endLocation = ChessReplUtils.parseMove(endPosition);

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
    
    Location location = ChessReplUtils.parseMove(locationString);
    PowerObject.Rarity rarity = ChessReplUtils.rarityFromString(rarityString);
    
    if(location == null) {
      return "ERROR: Invalid location.";
    }
    
    if(rarity == null) {
      return "ERROR: Invalid rarity.";
    }
    

    PowerObject powerObject = PowerObject.ofRarity(rarity);
    game.spawnPowerObject(location, powerObject);
    
    return print();
  }
  
  private String promote(final String piece) {
      if(game.getGameState() != Game.GameState.WAITING_FOR_PROMOTE) {
        return "ERROR: Not able to promote.";
      }

	  Player player = game.getActivePlayer();
	  Color color = player.getColor();
	  Piece p;
	  
	  if(piece.equalsIgnoreCase("Queen") || piece.equalsIgnoreCase("Q")) {
		  p = new Queen(color);
	  } else if (piece.equalsIgnoreCase("Bishop") || piece.equalsIgnoreCase("B")) {
		  p = new Bishop(color);
	  } else if (piece.equalsIgnoreCase("Rook") || piece.equalsIgnoreCase("R")) {
		  p = new Rook(color);
	  } else if (piece.equalsIgnoreCase("Knight") || piece.equalsIgnoreCase("N")) {
		  p = new Knight(color);
	  } else {
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
	  
	  switch(gameState) {
    	  case WAITING_FOR_MOVE:
    	    header = game.whiteToMove() ? "White to move.\n" : "Black to move.\n";
    	    break;
    	  case WAITING_FOR_PROMOTE:
    	    header = game.whiteToMove() ? "White to promote.\n" : "Black to promote.\n";
    	    break;
    	  default:
    	    header = gameState.toString() + "\n";
    	    break;
	  }
	  
   	return printBoardState(header);
  }
  
  private String printBoardState(final String header) {
	String boardString = ChessReplUtils.getBoardString(game.getBoard());
	if(header.equals("")) {
	  return boardString;
	}
	return "\n" + header + boardString + "\n";
  }

}
