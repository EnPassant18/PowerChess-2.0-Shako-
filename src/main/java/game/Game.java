package game;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.Board;
import board.BoardObject;
import board.IllegalMoveException;
import board.Location;
import pieces.Piece;
import utility.Pair;

/**
 * Game represents a game of chess.
 *
 * @author knorms
 *
 */
public class Game {
  private Map<Color, Pair<Player, Clock>> players;
  private Board board;
  private List<Board> history; // TODO should this be list of strings?

  /**
   * Constructs new default game.
   */
  public Game() {
    board = new Board();
    history = new ArrayList<>();
    players = new HashMap<>();
  }
  // TODO add constructor(s) that start special game or continue game

  /**
   * Execute next player's turn.
   */
  public void turn() {
    // TODO implement
  }

  /**
   * Get next move by player of specified color.
   *
   * @param color
   *          Color of player.
   * @return pair of locations representing start and ending locations of
   *         selected player move.
   */
  public Pair<Location, Location> getMove(Color color) {
    return players.get(color).getLeft().getMove();
  }

  /**
   * Get next move by player of specified color of whatever piece sits at
   * specified starting location.
   *
   * @param color
   *          Color of player.
   * @param start
   *          Starting location of piece to be moved.
   * @return pair of locations representing start and ending locations of
   *         selected player move.
   */
  public Pair<Location, Location> getMove(Color color, Location start) {
    return players.get(color).getLeft().getMove(start);
  }

  /**
   * Have board execute move.
   *
   * @param move
   *          Move to execute.
   * @return true if move was valid and executed, otherwise false.
   */
  public boolean makeMove(Pair<Location, Location> move) {
    try {
      BoardObject captured = board.move(move);
      // TODO something with captured piece/powerobject
      return true;
    } catch (IllegalMoveException e) {
      return false;
    }
  }

  /**
   * Force board to execute move, even if move would typically be illegal.
   *
   * @param move
   *          Move to execute.
   */
  public void forceMove(Pair<Location, Location> move) {
    BoardObject captured = board.forceMove(move);
    // TODO something with captured piece/power object
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
   *          pair of locations representing start and ending locations of a
   *          move.
   * @return true if the move is a castle.
   */
  public static boolean isCastle(Pair<Location, Location> move) {
    Location start = move.getLeft();
    Location end = move.getRight();
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
