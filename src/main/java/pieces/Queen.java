package pieces;

import java.util.Map;

import board.BoardObject;
import board.Location;
import game.Color;

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
	public boolean move(Location start, Location end,
			Map<Location, BoardObject> spaces) {
		if(isSame(start, end)) {
			return false;
		}
		int rowDir = (int) Math.signum(end.getCol() - start.getCol());
		int colDir = (int) Math.signum(end.getRow() - start.getRow());
		return checkInLine(start, end, spaces, rowDir, colDir);
	}
}
