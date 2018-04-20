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
public class Knight implements Piece {

	private Color color;
	private boolean moved;
	
	public Knight(Color color) {
		this.color = color;
		moved = false;
	}
	
	@Override
	public boolean canBeJumped() {
		return true;
	}
	
	@Override
	public boolean move(Location start, Location end,
			Map<Location, BoardObject> spaces) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
		
	}

	@Override
	public void hasMoved() {
		moved = true;
	}
	

}
