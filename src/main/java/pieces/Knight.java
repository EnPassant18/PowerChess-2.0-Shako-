package pieces;

import board.Board;
import board.Location;
import game.Color;
import game.Move;

/**
 * Piece represents a chess piece.
 *
 * @author knorms
 *
 */
public class Knight extends Piece {

	public Knight(Color color) {
		super(color);
	}
	
	@Override
	public boolean move(Move move, Board board) {
		Location start = move.getStart();
		Location end = move.getEnd();
		
		if(start.getRow() == end.getRow() || start.getCol() == end.getCol()) {
			return false;
		}
		int rowDif = end.getRow() - start.getRow();
		int colDif = end.getCol() - start.getCol();
		
		//If the knight cannot move from start to end in an L shape, return false.
		int absCol = Math.abs(colDif);
		int absRow = Math.abs(rowDif);
		if(!((absCol == 1 && absRow == 2) || (absCol == 2 && absRow == 1))) {
			return false;
		}
		
		checkMovement(start, end, board, rowDif, colDif);
		
		if(checkRowFirst(start, end, board, rowDif, colDif) || checkColFirst(start, end, board, rowDif, colDif)) {
			return isValidEnd(start, end, board);
		}
		return false;
	}
	
	private boolean checkRowFirst(Location start, Location end, Board board, int rowDif, int colDif) {
		int rowDir = (int) Math.signum(rowDif);
		int colDir = (int) Math.signum(colDif);
		
		Location check = new Location(start.getRow() + rowDir, start.getCol());
		
		
		return false;
	}
	
	private boolean checkColFirst(Location start, Location end, Board board, int rowDif, int colDif) {
		return false;
	}
	
	private boolean checkMovement(Location start, Location end, Board board, int rowDif, int colDif) {
		//Move by row first
		int rowDir = (int) Math.signum(rowDif);
		int colDir = (int) Math.signum(colDif);
		
		Location check = new Location(start.getRow() + rowDir, start.getCol());
		if(!board.isJumpable(check)) {
			return false;
		}
		if(check.getRow() != end.getRow()) {
			check = new Location(start.getRow() + rowDir, start.getCol());
			if(!board.isJumpable(check)) {
				return false;
			}
		}
		
		check = new Location(start.getRow(), start.getCol() + colDir);
		if(!board.isJumpable(check)) {
			return false;
		}
		while()
		for(int r = 0; r < rowDif; r++) {
			if(!board.isJumpable(check)) {
				return false;
			}
			check = new Location(check.getRow() + rowDir, check.getCol() + colDir);
		}
		
		return false;
	}
}
