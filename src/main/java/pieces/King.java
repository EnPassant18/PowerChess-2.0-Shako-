package pieces;

import board.Board;
import board.Location;
import game.Color;
import game.Move;

/**
 * King class that represents the King piece.
 *
 * @author knorms
 *
 */
public class King extends Piece {
	
	public King(Color color) {
		super(color);
	}
	
	@Override
	public boolean move(Move move, Board board) {
		Location start = move.getStart();
		Location end = move.getEnd();
		
		if(isSame(start, end)) {
			return false;
		}
		int colDir = Math.abs(end.getCol() - start.getCol());
		int rowDir = Math.abs(end.getRow() - start.getRow());
		if(rowDir > 1 || colDir > 1) {
			return false;
		}
		return isValidEnd(start, end, board);
		//TODO: Add ability to castle
	}
}
