package pieces;

import java.util.Map;

import board.BoardObject;
import board.Location;
import game.Color;

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
	public boolean move(Location start, Location end,
			Map<Location, BoardObject> spaces) {
		if(isSame(start, end)) {
			return false;
		}
		int rowDir = Math.abs(end.getCol() - start.getCol());
		int colDir = Math.abs(end.getRow() - start.getRow());
		if(rowDir > 1 || colDir > 1) {
			return false;
		}
		return isValidEnd(start, end, spaces);
		//TODO: Add ability to castle
	}
}
