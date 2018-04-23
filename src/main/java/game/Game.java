package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import board.Board;
import board.BoardObject;
import board.Location;
import pieces.GhostPawn;
import pieces.Piece;
import players.Player;
import powerups.PowerAction;
import powerups.PowerObject;
import projects.ChessProjectUtils;

/**
 * Game represents a game of chess.
 *
 * @author knorms
 *
 */
public class Game {
  private List<Player> players;
  private int activePlayerIndex;
  private Board board;
  private boolean gameOver;
  private List<Move> history; // list past moves

  /**
   * Constructs new default game.
   */
  public Game() {
    board = new Board();
    history = new ArrayList<>();
    players = new ArrayList<>();
    gameOver = false;
  }

  /**
   * Start the game.
   */
  public void start() {
    while (!gameOver) {
      turn();
    }
  }
  
  /**
   * Adds a player to the game.
   * @param player
   * 	Player to be added.
   */
  public void addPlayer(final Player player) {
	  players.add(player);
  }

  /**
   * Execute active player's turn.
   */
  public void turn() {
    Player player = players.get(activePlayerIndex);
    Move move = player.getMove();
    
    Piece p = board.getPieceAt(move.getStart());

    while (!validMove(move)) {
      move = player.getMove();
    }
    executeMove(move);

    activePlayerIndex = (activePlayerIndex + 1) % 2;
    board.resetGhost(activePlayerIndex);
    
  }
  

  /**
   * Check whether specified move is valid.
   *
   * @param move
   *          Move to check.
   * @return true if valid, false otherwise.
   */
  public boolean validMove(Move move) {
    Location start = move.getStart();
    Piece p = board.getPieceAt(start);
    if (!(p instanceof Piece) || p instanceof GhostPawn) {
      return false;
    }
    Piece piece = board.getPieceAt(start);
    // check if is castle, if yes, check if castle is valid
    // check isCastleValid(neitherHasMove && emptyBetween())
    return piece.move(move, board);
  }

  /**
   * Get next move by player of specified color.
   *
   * @param color
   *          Color of player.
   * @return a move (a pair of locations representing start and ending locations
   *         of selected player move).
   */
  public Move getMove(Color color) {
    return players.get(activePlayerIndex).getMove();
  }

  /**
   * Get next move by player of specified color of whatever piece sits at
   * specified starting location.
   *
   * @param color
   *          Color of player.
   * @param start
   *          Starting location of piece to be moved.
   * @return a move (a pair of locations representing start and ending locations
   *         of selected player move).
   */
  public Move getMove(Color color, Location start) {
    return players.get(activePlayerIndex).getMove(start);
  }

  /**
   * Execute a move; does not check for validity. Updates the board and manages
   * casling and captured pieces/power-ups.
   *
   * @param move
   *          Move to execute.
   */
  public void executeMove(Move move) {
    Collection<BoardObject> captured = board.move(move);

    // add move to history
    history.add(move);

    Location end = move.getEnd();

    // manage captured power-ups or king
    manageCaptured(captured, end);

    // TODO check for promotion, call player.getPromotion until legal piece is
    // chosen, then call board.executePromotion(Location end, Piece piece)
  }

  private void manageCaptured(Collection<BoardObject> captured,
      Location whereCaptured) {

    for (BoardObject obj : captured) {
      if (obj instanceof PowerObject) {
        List<PowerAction> actions = ((PowerObject) captured).getPowerActions();
        Player player = players.get(activePlayerIndex);
        PowerAction powerup = player.selectPowerAction(actions);
        powerup.act(whereCaptured, this);

        // TODO change to obj instanceof King
      } else if (obj.getClass().getName().equals("King")) {
        gameOver = true;
      }
    }

  }

  /**
   * Check whether given board location is empty.
   *
   * @param loc
   *          Location to check.
   * @return true if location is empty, otherwise false.
   */
  public boolean isEmpty(Location loc) {
    return board.isEmpty(loc);
  }

  /**
   * Get the color of the piece at specified location.
   *
   * @param loc
   *          Location of piece.
   * @return color of piece at location or null if no piece at specified
   *         location.
   */
  public Color getColorAt(Location loc) {
    try {
      return getPieceAt(loc).getColor();
    } catch (NullPointerException e) {
      return null;
    }
  }

  /**
   * Get the piece at specified location.
   *
   * @param loc
   *          Location of piece.
   * @return Piece at location or null if no piece at specified location.
   */
  public Piece getPieceAt(Location loc) {
    try {
      return board.getPieceAt(loc);
    } catch (ClassCastException e) {
      return null;
    }
  }

  private static final Location CASTLE_START1 = new Location(0, 4);
  private static final Location CASTLE_END_SHORT1 = new Location(0, 6);
  private static final Location CASTLE_END_LONG1 = new Location(0, 2);

  private static final Location CASTLE_START2 = new Location(7, 4);
  private static final Location CASTLE_END_SHORT2 = new Location(7, 6);
  private static final Location CASTLE_END_LONG2 = new Location(7, 2);

  /**
   * Check whether a move is a castle.
   *
   * @param move
   *          A move (a pair of locations representing start and ending
   *          locations of a move).
   * @return true if the move is a castle.
   */
  public static boolean isCastle(Move move) {
    Location start = move.getStart();
    Location end = move.getEnd();
    if (start.equals(CASTLE_START1)
        && (end.equals(CASTLE_END_SHORT1) || end.equals(CASTLE_END_LONG1))) {
      return true;
    } else if (start.equals(CASTLE_START2)
        && (end.equals(CASTLE_END_SHORT2) || end.equals(CASTLE_END_LONG2))) {
      return true;
    }
    return false;
  }
  
  /**
   * Getter method for board.
   * @return
   * 	Current board of the game.
   */
  public Board getBoard() {
	  return board;
  }
  
  
  /**
   * Getter method for active player index.
   * @return
   * 	Current active player.
   */
  public int getActivePlayerIndex() {
	  return activePlayerIndex;
  }
  
  /**
   * Checks whether or not the game is over.
   * @return
   * 	True if the game is over and false otherwise.
   */
  public boolean getGameOverStatus() {
	  return gameOver;
  }
  
}
