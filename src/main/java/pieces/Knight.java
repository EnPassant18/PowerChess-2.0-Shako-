package pieces;

import java.util.Map;

import board.BoardObject;
import board.Location;
import game.Color;

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
	public boolean move(Location start, Location end,
			Map<Location, BoardObject> spaces) {
		if(isSame(start, end)) {
			return false;
		}
		int colDif = end.getRow() - start.getRow();
		int rowDif = end.getCol() - start.getCol();
		//If the knight cannot move from start to end in an L shape, return false.
		if(!((colDif == 1 && rowDif == 2) || (colDif == 2 && rowDif == 1))) {
			return false;
		}
		return false;
	}
}
