package board;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import game.Color;
import game.Move;
import pieces.Bishop;
import pieces.GhostPawn;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

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
  private static final int LAST_COL = 7;

  /**
   * Constructs a board of empty spaces.
   */
  public Board() {
    spaces = HashMultimap.create();
    fillRow(0, Color.WHITE);
    fillPawns(1, Color.WHITE);
    for (int i = 2; i < Board.SIZE - 2; i++) {
      for (int j = 0; j < Board.SIZE; j++) {
        spaces.put(new Location(i, j), EMPTY_SPACE);
      }
    }
    fillRow(Board.SIZE - 1, Color.BLACK);
    fillPawns(Board.SIZE - 2, Color.BLACK);
  }

  // TODO implement constructor that takes boardString to use with db

  /**
   * Fills the non-pawn row with the appropriate pieces according to the color
   * given.
   *
   * @param row
   *          The row to fill. Either the first or last row.
   * @param color
   *          The color of the pieces. Either black or white.
   */
  private void fillRow(int row, Color color) {
    spaces.put(new Location(row, 0), new Rook(color));
    spaces.put(new Location(row, 1), new Knight(color));
    spaces.put(new Location(row, 2), new Bishop(color));
    spaces.put(new Location(row, 3), new Queen(color));
    spaces.put(new Location(row, 4), new King(color));
    spaces.put(new Location(row, 5), new Bishop(color));
    spaces.put(new Location(row, 6), new Knight(color));
    spaces.put(new Location(row, LAST_COL), new Rook(color));
  }

  /**
   * Fills the given row with pawns of the given color.
   *
   * @param row
   *          The row to fill. Either the second or second to last row.
   * @param color
   *          The color of the pawns. Either black or white.
   */
  private void fillPawns(int row, Color color) {
    for (int c = 0; c < Board.SIZE; c++) {
      spaces.put(new Location(row, c), new Pawn(color));
    }
  }

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

  /**
   * Check if a location on the board is jumpable.
   *
   * @param loc
   *          Board location.
   * @return true if can be jumped, false otherwise.
   */
  public boolean isJumpable(Location loc) {
    for (BoardObject obj : spaces.get(loc)) {
      return obj.canBeJumped();
    }
    return false;
  }

  /**
   * Remove ghost pawn from board for active player.
   *
   * @param playerIndex
   *          Index of player whose ghost pawns should be removed (0 = White, 1
   *          = Black).
   * @exception IllegalArgumentException
   *              if method is passed playerIndex other than 0 (white) or 1
   *              (black).
   */
  public void resetGhost(int playerIndex) throws IllegalArgumentException {
    Color color;
    switch (playerIndex) {
      case 0:
        color = Color.WHITE;
        break;
      case 1:
        color = Color.BLACK;
        break;
      default:
        throw new IllegalArgumentException(
            "ERROR: Illegal playerIndex; expected 0 (white) or 1 (black).");
    }

    for (Location loc : spaces.keys()) {
      Piece p = getPieceAt(loc);
      if (p instanceof GhostPawn && p.getColor() == color) {
        spaces.remove(loc, p);
      }
    }
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
    return objs.size() == 1
        && (objs.contains(EMPTY_SPACE) || spaces.get(loc) instanceof GhostPawn);
  }

  /**
   * Set a Ghost Pawn on the board when player moves pawn 2 spaces for first
   * pawn move.
   *
   * @param loc
   *          Board location skipped space where ghost pawn will be set.
   * @param color
   *          Color of ghost pawn.
   */
  public void setGhost(Location loc, Color color) {
    spaces.put(loc, new GhostPawn(color));
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

    Piece startPiece = getPieceAt(start);
    Collection<BoardObject> captured;
    if (startPiece instanceof King && ((King) startPiece).getCastling()) {
      Piece rook;
      Location rookLocStart;
      Location rookLocEnd;
      if (end.getCol() == 1) {
        rookLocStart = new Location(end.getRow(), end.getCol() - 1);
        rook = getPieceAt(rookLocStart);
        rookLocEnd = new Location(end.getRow(), end.getCol() + 1);
        spaces.put(end, startPiece);

      } else {
        rookLocStart = new Location(end.getRow(), end.getCol() + 1);
        rook = getPieceAt(rookLocStart);
        rookLocEnd = new Location(end.getRow(), end.getCol() - 1);
      }
      Collection<BoardObject> obj1 = spaces.get(start);
      captured = spaces.removeAll(end);
      spaces.putAll(end, obj1);
      spaces.put(start, EMPTY_SPACE);
      Collection<BoardObject> obj2 = spaces.get(rookLocStart);
      spaces.removeAll(rookLocEnd);
      spaces.putAll(rookLocEnd, obj2);
      spaces.put(rookLocStart, EMPTY_SPACE);
      ((King) startPiece).resetCastling();
    } else {
      if (startPiece instanceof Pawn && ((Pawn) startPiece).getGhost()) {
        Piece p = getPieceAt(end);
        int direction;
        if (p.getColor() == Color.WHITE) {
          direction = 1;
        } else {
          direction = -1;
        }
        spaces.removeAll(new Location(end.getRow(), end.getCol() + direction));
      }
      Collection<BoardObject> obj = spaces.get(start);
      captured = spaces.removeAll(end);
      spaces.putAll(end, obj);
      spaces.put(start, EMPTY_SPACE);
    }
    startPiece.setMoved();
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
