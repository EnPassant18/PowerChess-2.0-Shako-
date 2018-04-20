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
public abstract class Piece implements BoardObject {
	
	private Color color;
	private boolean moved;
	
	public Piece(Color color) {
		this.color = color;
		moved = false;
	}
	
	@Override
	public boolean canBeJumped() {
		return true;
	}
	
	@Override
	public boolean isPiece() {
		return true;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	/**
	 * Get piece color.
	 *
	 * @return color of piece (WHITE or BLACK).
	 */
	public Color getColor() {
		return this.color;
	}
	  
	/**
	 * Set the color of the piece.
	 * 
	 * @param color
	 * 	color of piece to set to
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	  
	/**
	 * Is called after a piece makes a move. Sets the moved boolean to true.
	 */
	public void setMoved() {
		this.moved = true;
	}
	  
	/**
	 * Checks if the piece has moved since the start of the game.
	 * 
	 * @return 
	 * 	True if piece has moved and false if it hasn't.
	 */
	boolean getMoved() {
		return this.moved;
	}
	  
	protected boolean isValidEnd(Location start, Location end, Map<Location, BoardObject> spaces) {
		if(spaces.get(end).isPiece() && !(spaces.get(end) instanceof GhostPawn)) {
			if(((Piece) spaces.get(end)).getColor() == ((Piece) spaces.get(start)).getColor()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks all spaces in a straight line from start to end location. If the BoardObject at each location between
	 * start and end is not empty and not a piece, will return false as player is attempting to make an invalid move.
	 * Finally, checks that the end location is not a piece of the same color as the start location.
	 * 
	 * @param start
	 * 		Location of the piece being moved
	 * @param end
	 * 		Location the piece wants to move to
	 * @param spaces
	 * 		Map of the all the pieces on the board
	 * @param rowDir
	 * 		Direction to move in rows. Either -1, 0, or 1.
	 * @param colDir
	 * 		Direction to move in columns. Either -1, 0, or 1.
	 * @return
	 * 		True if piece can move from start to end. False if it can't.
	 */
	protected boolean checkInLine(Location start, Location end, Map<Location, BoardObject> spaces, int rowDir, int colDir) {
		Location check = new Location(start.getRow() + rowDir, start.getCol() + colDir);
		while(check.getRow() != end.getRow() && check.getCol() != end.getCol()) {
			if(!spaces.get(check).isEmpty()) {
				return false;
			}
			check = new Location(check.getRow() + rowDir, check.getCol() + colDir);
		}
		return isValidEnd(start, end, spaces);
	}
	
	protected boolean isSame(Location start, Location end) {
		if(start.getRow() == end.getRow() && start.getCol() == end.getCol()) {
			return true;
		}
		return false;
	}
}
