package board;

import java.util.HashMap;
import java.util.Map;

import game.Move;

/**
 * Board represents the chess board.
 *
 * @author knorms
 *
 */
public class Board {
  private Map<Location, BoardObject> spaces;
  public static final int SIZE = 8;
  private static final EmptySpace EMPTY_SPACE = new EmptySpace();

  /**
   * Constructs a board of empty spaces.
   */
  public Board() {
    spaces = new HashMap<>();
    for (int i = 0; i < Board.SIZE; i++) {
      for (int j = 0; j < Board.SIZE; j++) {
        spaces.put(new Location(i, j), EMPTY_SPACE);
      }
    }
    // TODO change to construct a board with all pieces at starting locations
  }

  // TODO implement constructor that takes boardString to use with db

  /**
   * Get object at specified location.
   *
   * @param loc
   *          Board location.
   * @return Board object at specified location or EMPTY_SPACE if location is
   *         empty.
   */
  public BoardObject getObjectAt(Location loc) {
    return spaces.get(loc);
  }

  /**
   * Check whether given board location is empty.
   *
   * @param loc
   *          Location to check.
   * @return true if location is empty, otherwise false.
   */
  public boolean isEmpty(Location loc) {
    return spaces.get(loc).equals(EMPTY_SPACE);
  }

  /**
   * Move a BoardObject from one location.
   *
   * @param move
   *          Pair of locations representing the start and ending locations of a
   *          move.
   * @return captured board object if any, EMPTY_SPACE otherwise.
   * @throws IllegalMoveException
   *           if attempted move was not legal.
   */
  public BoardObject move(Move move) throws IllegalMoveException {
    Location start = move.getStart();
    Location end = move.getEnd();
    if (isEmpty(start)) {
      throw new IllegalMoveException(
          String.format("ERROR: starting location %s is empty.", start));
    }
    BoardObject obj = spaces.get(start);
    if (obj.move(start, end, spaces)) { // if allowable move
      // TODO if castle, call move again on rook
      // TODO if promotion, call promotion method
      // TODO if En Passant, are we leaving behind a "ghost pawn?"
      return forceMove(move);
    } else {
      throw new IllegalMoveException(
          String.format("ERROR: cannot move %s from %s to %s.",
              obj.getClass().getSimpleName(), start, end));
    }
  }

  /**
   * Force move a BoardObject from one location, even if piece would not
   * normally move in this way.
   *
   * @param move
   *          Pair of locations representing the start and ending locations of a
   *          move.
   * @return captured board object if any, EMPTY_SPACE otherwise.
   */
  public BoardObject forceMove(Move move) {
    Location start = move.getStart();
    Location end = move.getEnd();
    if (!isEmpty(start)) {
      BoardObject obj = spaces.get(start);
      BoardObject captured = spaces.remove(end);
      spaces.put(end, obj);
      spaces.put(start, EMPTY_SPACE);
      return captured;
    }
    return EMPTY_SPACE;
  }

  /**
   * Represents an empty space on a board.
   *
   * @author knorms
   *
   */
  private static final class EmptySpace implements BoardObject {
    private EmptySpace() {
    }

    @Override
    public boolean canBeJumped() {
      return true;
    }

    @Override
    public boolean move(Location start, Location end,
        Map<Location, BoardObject> spaces) {
      return false;
    }
  }

}
