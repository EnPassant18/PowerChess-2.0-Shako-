package projects;

import java.util.Set;
import java.util.StringJoiner;

import board.Board;
import board.BoardObject;
import board.Location;
import game.Color;
import game.Game;
import pieces.*;
import powerups.PowerObject;

public final class ChessProjectUtils {

	public static final int SIZE = 8;
	public static final Character EMPTY_SPACE_CHAR = 'x';
	public static final Character POWER_UP_CHAR = 'W';
	
	private ChessProjectUtils() {
		
	}
	
	/**
	 * Parses chess coordinates to a location. Example a1 -> (0,0) or e4 -> (4,3). 
	 * @param str
	 * 	String representation of a location in algebraic chess coordinates.
	 * @return
	 * 	Location object representing the location (or null if invalid input.)
	 */
	public static Location parseMove(final String str) {
	    String regex = "[a-h][1-8]";
	    if (!str.matches(regex)) {
	        return null;
	    }
	    
	    int row, col;
	    
	    col = str.charAt(0) - 'a';
	    row = SIZE - Character.getNumericValue(str.charAt(1));
	    
	    return new Location(row, col);
	}
	
	/**
	 * Gets a string representation of a board. (Note: Not meant to be toString()).
	 * @param board
	 * 	Board to find the string representation of.
	 * @return
	 * 	String representation of the board.
	 */
	public static String getBoardString(final Board board) {
		
		StringJoiner output = new StringJoiner("\n", "","");
		StringBuilder line;
		
		for(int i = SIZE - 1; i >= 0; i--) {
			line = new StringBuilder();
			for(int j = 0; j < SIZE; j++) {
				line.append(boardSpaceToChar(i,j, board));
			}
			output.add(line);
		}
		
		return output.toString();
		
		
	}
	
	public static Character boardSpaceToChar(final int i, final int j, final Board board) {
		Location location = new Location(i,j);
		if(board.isEmpty(location)) {
			return EMPTY_SPACE_CHAR;
		} else if (board.getPieceAt(location) == null) {
			return POWER_UP_CHAR;
		} 
		
		return pieceToChar(board.getPieceAt(location));
		
	}
	
	/**
	 * Returns a character representing the type of chess pieces.
	 * @param piece
	 * 	Chess piece to get the character representation of.
	 * @return
	 * 	Character representation of the chess piece.
	 */
	public static Character pieceToChar(final Piece piece) {
		
		Character c;
		
		if(piece instanceof Bishop) {
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
		
		if(piece.getColor() == Color.WHITE) {
			return Character.toLowerCase(c);
		} else {
			return c;
		}
		
	}

}
