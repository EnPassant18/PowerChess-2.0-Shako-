package pieces;

import board.Board;
import game.Color;
import game.Move;

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
	public boolean move(Move move, Board board) {
		return false;
	}
}
