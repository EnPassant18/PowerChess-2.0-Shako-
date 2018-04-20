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
public class Rook implements Piece {

	private Color color;
	private boolean moved;
	
	public Rook(Color color) {
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
		if(start.getRow() != end.getRow() && start.getCol() != end.getCol()) {
			return false;
		}
		if(start.getRow() == end.getRow() && start.getCol() == end.getCol()) {
			return false;
		}
		int rowDir = Math.abs(end.getCol() - start.getCol());
		int colDir = Math.abs(end.getRow() - start.getRow());
		return checkInLine(start, end, spaces, rowDir, colDir);
	}
	
	private boolean checkInLine(Location start, Location end, Map<Location, BoardObject> spaces, int rowDir, int colDir) {
		Location check = new Location(start.getRow() + rowDir, start.getCol() + colDir);
		while(check.getRow() != end.getRow() && check.getCol() != end.getCol()) {
			if(spaces.get(check).)
		}
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
