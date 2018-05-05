package board;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

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
import powerups.PowerObject;
import powerups.PowerUp;
import repl.ChessReplUtils;

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

  /**
   * Constructs a board corresponding to a given FEN string.
   *
   * @param fen
   *          FEN String.
   */
  public Board(String fen) {
    if (!ChessReplUtils.isFenValid(fen)) {
      throw new IllegalArgumentException(
          "ERROR: Invalid FEN given to Board constructor.");
    }

    String[] fenArray = fen.split("\\s+");

    String piecePlacement = fenArray[0];
    String activeColor = fenArray[1];
    String castling = fenArray[2];
    String enPassant = fenArray[3];

    Character c;
    Character seperator = '/';

    spaces = HashMultimap.create();

    int i = 0;
    int j = 0;

    for (int k = 0; k < piecePlacement.length(); k++) {

      c = piecePlacement.charAt(k);

      if (c.equals(seperator)) {
        i++;
        j = 0;
      } else if (Character.isLetter(c)) {
        spaces.put(new Location(i, j), ChessReplUtils.charToPiece(c));
        j++;
      } else if (Character.isDigit(c)) {
        for (int h = 0; h < Character.getNumericValue(c); h++) {
          spaces.put(new Location(i, j), EMPTY_SPACE);
          j++;
        }
      }
    }

    if (!enPassant.equals("-")) {
      Location loc = ChessReplUtils.parseLocation(enPassant);
      Color color = loc.getRow() == 3 ? Color.WHITE : Color.BLACK;
      spaces.put(loc, new GhostPawn(color));
    }

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
   * Get PowerObject at specified location.
   *
   * @param loc
   *          Board location.
   * @return PowerObject at specified location or null if location no
   *         PowerObject at location.
   */
  public PowerObject getPowerObjectAt(Location loc) {
    for (BoardObject obj : spaces.get(loc)) {
      if (obj instanceof PowerObject) {
        return (PowerObject) obj;
      }
    }
    return null;
  }

  /**
   * Get PowerUp at specified location.
   *
   * @param loc
   *          Board location.
   * @return PowerUp at specified location or null if location no PowerUp at
   *         location.
   */
  public PowerUp getPowerUpAt(Location loc) {
    for (BoardObject obj : spaces.get(loc)) {
      if (obj instanceof PowerUp) {
        return (PowerUp) obj;
      }
    }
    return null;
  }

  /**
   * Add an object to the board (usually a PowerObject or a PowerUp); note this
   * method does check whether a space is empty.
   *
   * @param loc
   *          Location to put object.
   * @param obj
   *          BoardObject to place on the board.
   */
  public void addBoardObject(Location loc, BoardObject obj) {
    spaces.put(loc, obj);
  }

  /**
   * Replace the the piece at a specified board location with given new piece.
   *
   * @param loc
   *          Location of piece to be replaced.
   * @param newPiece
   *          New piece to place.
   * @throws IllegalMoveException
   *           If location does not already house a piece of the same color.
   */
  public void replacePiece(Location loc, Piece newPiece)
      throws IllegalMoveException {
    for (BoardObject obj : spaces.get(loc)) {
      if (obj instanceof Piece
          && ((Piece) obj).getColor() == newPiece.getColor()) {
        spaces.remove(loc, obj);
        spaces.put(loc, newPiece);
        return;
      }
    }
    throw new IllegalMoveException(
        String.format("ERROR: %s does not have a piece at %s.",
            newPiece.getColor(), loc.toString()));
  }

  /**
   * Removes a piece at a given location (if one exists).
   *
   * @param loc
   *          Location to remove the piece from.
   */
  public void removePieceAt(final Location loc) {
    for (BoardObject obj : spaces.get(loc)) {
      if (obj instanceof Piece) {
        spaces.remove(loc, obj);
      }
    }
    if (spaces.get(loc).isEmpty()) {
      spaces.put(loc, EMPTY_SPACE);
    }
  }

  /**
   * Places a piece at a given location, replacing any existing piece.
   *
   * @param loc
   *          Location to place the piece.
   * @param piece
   *          Piece to be placed.
   */
  public void placePiece(final Location loc, final Piece piece) {
    removePieceAt(loc);
    spaces.put(loc, piece);
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
      return obj.canBeJumped(); // what
    }
    return false;
  }

  /**
   * Remove ghost pawn from board for active player.
   *
   * @param color
   *          Color of ghost pawn to remove ghost.
   */
  public void resetGhost(final Color color) {
    for (Location loc : spaces.keySet()) {
      Piece p = getPieceAt(loc);
      if (p instanceof GhostPawn && p.getColor() == color) {
        spaces.remove(loc, p);
      }
    }
  }

  /**
   * Remove the PowerUp at a specified board location.
   *
   * @param loc
   *          Location of PowerUp.
   * @param power
   *          PowerUp to remove.
   */
  public void removePowerUp(Location loc, PowerUp power) {
    spaces.remove(loc, power);
    System.out.println(spaces.get(loc));
    if (!spaces.get(loc).isEmpty()) {
      spaces.put(loc, EMPTY_SPACE);
    }
  }

  /**
   * Swap the contents of two board locations.
   *
   * @param loc1
   *          First board location.
   * @param loc2
   *          Second board location.
   */
  public void swap(final Location loc1, final Location loc2) {
    Collection<BoardObject> coll1 = spaces.removeAll(loc1);
    Collection<BoardObject> coll2 = spaces.removeAll(loc2);

    spaces.putAll(loc1, coll2);
    spaces.putAll(loc2, coll1);
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
    if (startPiece == null) {
      return Collections.emptyList();
    }
    startPiece.setMoved();
    Collection<BoardObject> captured;
    if (startPiece instanceof King && ((King) startPiece).getCastling()) {
      Location rookLocStart;
      Location rookLocEnd;
      if (end.getCol() == 2) {
        rookLocStart = new Location(end.getRow(), end.getCol() - 2);
        rookLocEnd = new Location(end.getRow(), end.getCol() + 1);

      } else {
        rookLocStart = new Location(end.getRow(), end.getCol() + 1);
        rookLocEnd = new Location(end.getRow(), end.getCol() - 1);
      }
      Collection<BoardObject> obj1 = spaces.removeAll(start);
      captured = spaces.removeAll(end);
      spaces.putAll(end, obj1);
      spaces.put(start, EMPTY_SPACE);
      Collection<BoardObject> obj2 = spaces.get(rookLocStart);
      spaces.removeAll(rookLocEnd);
      spaces.putAll(rookLocEnd, obj2);
      spaces.removeAll(rookLocStart);
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
        ((Pawn) startPiece).resetGhost();
        Location enemyPawn =
            new Location(end.getRow() + direction, end.getCol());
        spaces.removeAll(enemyPawn);
        spaces.put(enemyPawn, EMPTY_SPACE);
      }
      Collection<BoardObject> startObjs = spaces.removeAll(start);
      captured = spaces.removeAll(end);
      spaces.putAll(end, startObjs);
      spaces.put(start, EMPTY_SPACE);
    }
    return captured;
  }

  /**
   * Represents an empty space on a board.
   *
   * @author knorms
   *
   */
  public static final class EmptySpace implements BoardObject {
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

  /**
   * Get all objects at a specified board location.
   *
   * @param loc
   *          Board location.
   * @return collection of objects at board location.
   */
  public Collection<BoardObject> getObjsAt(Location loc) {
    return spaces.get(loc);
  }

  /**
   * Getter for set of all board locations.
   *
   * @return Set of all board locations.
   */
  public Set<Location> getLocationSet() {
    return spaces.keySet();
  }

}
