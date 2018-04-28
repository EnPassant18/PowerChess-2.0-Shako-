package repl;

import java.util.StringJoiner;

import board.Board;
import board.Location;
import game.Color;
import pieces.Bishop;
import pieces.GhostPawn;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/**
 * Utility class for REPL; includes methods to parse a move, piece to char,
 * board space to char.
 *
 * @author bbentz
 *
 */
public final class ChessReplUtils {

  public static final int SIZE = 8;
  public static final Character EMPTY_SPACE_CHAR = 'x';
  public static final Character POWER_UP_CHAR = 'W';
  public static final Character BLACKHOLE_CHAR = 'O';

  private ChessReplUtils() {

  }

  /**
   * Parses chess coordinates to a location. Example a1 -> (0,0) or e4 -> (4,3).
   *
   * @param locString
   *          String representation of a location in algebraic chess
   *          coordinates.
   * @return Location object representing the location (or null if invalid
   *         input.)
   */
  public static Location parseLocation(final String locString) {
    String regex = "[a-h][1-8]";
    if (!locString.matches(regex)) {
      return null;
    }

    int row, col;

    col = locString.charAt(0) - 'a';
    row = Character.getNumericValue(locString.charAt(1)) - 1;

    return new Location(row, col);
  }

  /**
   * Parse a string to a piece of the specified color.
   *
   * @param pieceStr
   *          Piece string to parse (could be full name like 'queen' or single
   *          character like 'q').
   * @param color
   *          Color of piece to return.
   * @return return Piece that parsed string indicated or return null if could
   *         not parse piece.
   */
  public static Piece parsePiece(final String pieceStr, Color color) {
    if (pieceStr.equalsIgnoreCase("Queen") || pieceStr.equalsIgnoreCase("Q")) {
      return new Queen(color);
    } else if (pieceStr.equalsIgnoreCase("Bishop")
        || pieceStr.equalsIgnoreCase("B")) {
      return new Bishop(color);
    } else if (pieceStr.equalsIgnoreCase("Rook")
        || pieceStr.equalsIgnoreCase("R")) {
      return new Rook(color);
    } else if (pieceStr.equalsIgnoreCase("Knight")
        || pieceStr.equalsIgnoreCase("N")) {
      return new Knight(color);
    } else if (pieceStr.equalsIgnoreCase("Pawn")
        || pieceStr.equalsIgnoreCase("P")) {
      return new Pawn(color);
    } else if (pieceStr.equalsIgnoreCase("King")
        || pieceStr.equalsIgnoreCase("K")) {
      return new King(color);
    } else {
      return null;
    }
  }

  /**
   * Gets a string representation of a board. (Note: Not meant to be
   * toString()).
   *
   * @param board
   *          Board to find the string representation of.
   * @return String representation of the board.
   */
  public static String getBoardString(final Board board) {

    StringJoiner output = new StringJoiner("\n", "", "");
    StringBuilder line;

    for (int i = SIZE - 1; i >= 0; i--) {
      line = new StringBuilder();
      line.append(i + 1);
      line.append(" |");
      for (int j = 0; j < SIZE; j++) {
        line.append(boardSpaceToChar(i, j, board));
      }
      output.add(line);
    }

    output.add("   ________");
    output.add("   abcdefgh");

    return output.toString();

  }

  /**
   * Convert a board space to a character representation.
   *
   * @param row
   *          Row location.
   * @param col
   *          Column location.
   * @param board
   *          Board to represent.
   * @return Character representing the object at a specified row and column.
   */
  public static Character boardSpaceToChar(final int row, final int col,
      final Board board) {
    Location location = new Location(row, col);
    if (board.isEmpty(location)) {
      return EMPTY_SPACE_CHAR;
    } else if (board.getPieceAt(location) == null) {
      if (board.getPowerUpAt(location) instanceof powerups.BlackHole) {
        return BLACKHOLE_CHAR;
      }
      return POWER_UP_CHAR;
    }

    return pieceToChar(board.getPieceAt(location));

  }

  /**
   * Returns a character representing the type of chess pieces.
   *
   * @param piece
   *          Chess piece to get the character representation of.
   * @return Character representation of the chess piece.
   */
  public static Character pieceToChar(final Piece piece) {

    Character c;

    if (piece instanceof Bishop) {
      c = 'B';
    } else if (piece instanceof King) {
      c = 'K';
    } else if (piece instanceof Knight) {
      c = 'N';
    } else if (piece instanceof Pawn) {
      c = 'P';
    } else if (piece instanceof Queen) {
      c = 'Q';
    } else if (piece instanceof Rook) {
      c = 'R';
    } else if (piece instanceof GhostPawn) {
      c = 'G';
    } else {
      c = 'Q';
    }

    if (piece.getColor() == Color.WHITE) {
      return Character.toLowerCase(c);
    } else {
      return c;
    }

  }

}
