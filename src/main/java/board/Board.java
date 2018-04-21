package board;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import game.Move;
import pieces.GhostPawn;
import pieces.Piece;

/**
 * Board represents the chess board.
 *
 * @author knorms
 *
 */
public class Board {
  private Multimap<Location, BoardObject> spaces;
  public static final int SIZE = 8;
  private static final EmptySpace EMPTY_SPACE = new EmptySpace();

  /**
   * Constructs a board of empty spaces.
   */
  public Board() {
    spaces = HashMultimap.create();
    for (int i = 0; i < Board.SIZE; i++) {
      for (int j = 0; j < Board.SIZE; j++) {
        spaces.put(new Location(i, j), EMPTY_SPACE);
      }
    }
    // TODO change to construct a board with all pieces at starting locations
  }

  // TODO implement constructor that takes boardString to use with db

  // DEPRICATED DUE TO MULTIMAP
  // public BoardObject getObjectAt(Location loc) {
  // return spaces.get(loc);
  // }

  /**
   * Get piece at specified location.
   *
   * @param loc
   *          Board location.
   * @return Piece at specified location or null if location no piece at
   *         location.
   */
  public Piece getPieceAt(Location loc) {
    for (BoardObject obj : spaces.get(loc)) {
      if (obj instanceof Piece) {
        return (Piece) obj;
      }
    }
    return null;
  }
  
  public boolean isJumpable(Location loc) {
	  for(BoardObject obj : spaces.get(loc)) {
		  return obj.canBeJumped();
	  }
	  return false;
  }

  /**
   * Check whether given board location is empty.
   *
   * @param loc
   *          Location to check.
   * @return true if location is empty or a ghost pawn, otherwise false.
   */
  public boolean isEmpty(Location loc) {
    Collection<BoardObject> objs = spaces.get(loc);
    return objs.size() == 1 && (objs.contains(EMPTY_SPACE) || spaces.get(loc) instanceof GhostPawn);
  }

  /**
   * Move a BoardObject from one location to another; does not check move
   * validity.
   *
   * @param move
   *          Pair of locations representing the start and ending locations of a
   *          move.
   * @return captured board objects if any, Collection containing only an
   *         EMPTY_SPACE otherwise.
   */
  public Collection<BoardObject> move(Move move) {
    Location start = move.getStart();
    Location end = move.getEnd();
    Collection<BoardObject> captured;
    Collection<BoardObject> obj = spaces.get(start);
    captured = spaces.removeAll(end);
    spaces.putAll(end, obj);
    spaces.put(start, EMPTY_SPACE);
    return captured;
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
    public boolean move(Move move, Board board) {
      return false;
    }
  }

}
