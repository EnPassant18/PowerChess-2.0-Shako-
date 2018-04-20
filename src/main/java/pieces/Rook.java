package pieces;

import java.util.Map;

import board.BoardObject;
import board.Location;
import game.Color;

/**
 * Rook class that represents the rook piece.
 *
 * @author knorms
 *
 */
public class Rook extends Piece {
	
	public Rook(Color color) {
		super(color);
	}
	
	@Override
	public boolean move(Location start, Location end,
			Map<Location, BoardObject> spaces) {
		if(start.getRow() != end.getRow() && start.getCol() != end.getCol()) {
			return false;
		}
		if(isSame(start, end)) {
			return false;
		}
		int rowDir = (int) Math.signum(end.getCol() - start.getCol());
		int colDir = (int) Math.signum(end.getRow() - start.getRow());
		if(rowDir != 0 && colDir != 0) {
			return false;
		}
		return checkInLine(start, end, spaces, rowDir, colDir);
	}
}
