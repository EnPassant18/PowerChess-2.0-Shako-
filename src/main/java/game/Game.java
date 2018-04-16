package game;

import java.time.Clock;
import java.util.List;
import java.util.Map;

import board.Board;
import board.Location;
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

  // TODO add constructor that starts a game

  /**
   * Execute next player's turn.
   */
  public void turn() {
    // TODO implement
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
