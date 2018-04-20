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
public class GhostPawn extends Piece {

	public GhostPawn(Color color) {
		super(color);
	}
	
	@Override
	public boolean move(Location start, Location end,
			Map<Location, BoardObject> spaces) {
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}

}
