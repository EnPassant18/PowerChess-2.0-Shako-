package pieces;

import java.util.Map;

import board.Board;
import board.BoardObject;
import board.Location;
import game.Color;
import game.Move;

/**
 * Queen class that represents the Queen piece.
 *
 * @author knorms
 *
 */
public class Queen extends Piece {

	public Queen(Color color) {
		super(color);
	}
	
	@Override
	public boolean move(Move move, Board board) {
		Location start = move.getStart();
		Location end = move.getEnd();
		
		if(isSame(start, end)) {
			return false;
		}
		int colDir = (int) Math.signum(end.getCol() - start.getCol());
		int rowDir = (int) Math.signum(end.getRow() - start.getRow());
		return checkInLine(start, end, board, rowDir, colDir);
	}
}
