package game;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.BoardObject;
import board.IllegalMoveException;
import board.Location;
import pieces.Piece;
import powerups.PowerAction;
import powerups.PowerObject;

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
      activePlayerIndex = (activePlayerIndex + 1) % 2;
    }
  }

  /**
   * Execute active player's turn.
   */
  public void turn() {
    Player player = players.get(activePlayerIndex);
    Move move = player.getMove();

    // ask player for move until they choose a valid move
    while (!makeMove(move)) {
      move = player.getMove();
    }

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
   * Ask board execute move only if move is legal.
   *
   * @param move
   *          Move to execute.
   * @return true if move was valid and executed, otherwise false.
   */
  public boolean makeMove(Move move) {
    return executeMove(move, false);
  }

  /**
   * Force board to execute move, even if move would typically be illegal.
   *
   * @param move
   *          Move to execute.
   */
  public void forceMove(Move move) {
    executeMove(move, true);
  }

  private boolean executeMove(Move move, boolean force) {
    try {
      BoardObject captured;

      if (force) {
        // force move, even if normally illegal
        captured = board.forceMove(move);
      } else {
        // try to make move (throws error if illegal)
        captured = board.move(move);
      }

      // add move to history
      history.add(move);

      Location end = move.getEnd();

      // manage captured power-ups or king
      manageCaptured(captured, end);

      // TODO check for promotion, call player.getPromotion until legal piece is
      // chosen, then call board.executePromotion(Location end, Piece piece)

      return true;
    } catch (IllegalMoveException e) {
      return false;
    }
  }

  private void manageCaptured(BoardObject captured, Location whereCaptured) {
    if (captured instanceof PowerObject) {
      List<PowerAction> actions = ((PowerObject) captured).getPowerActions();
      Player player = players.get(activePlayerIndex);
      PowerAction powerup = player.selectPowerAction(actions);
      powerup.act(whereCaptured, this);
    } else if (captured.getClass().getName().equals("King")) {
      gameOver = true;
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
      return ((Piece) board.getObjectAt(loc));
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

}
