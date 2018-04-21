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
public class Pawn extends Piece {

	public Pawn(Color color) {
		super(color);
	}
	
	@Override
	public boolean move(Move move, Board board) {
		Location start = move.getStart();
		Location end = move.getEnd();
		// TODO Auto-generated method stub
		return false;
	}
}
