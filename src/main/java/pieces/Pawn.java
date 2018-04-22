package pieces;

import java.util.Collection;

import board.Board;
import board.BoardObject;
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

	private int direction;
	private boolean ghost;
	
	public Pawn(Color color) {
		super(color);
	}
	
	@Override
	public boolean move(Move move, Board board) {
		Location start = move.getStart();
		Location end = move.getEnd();
		if(isSame(start, end)) {
			return false;
		}
		
		if(getColor() == Color.WHITE) {
			direction = 1;
		}
		else {
			direction = -1;
		}
		
		int rowDif = end.getRow() - start.getRow();
		int colDif = end.getCol() - start.getCol();
		
		boolean moveBool = checkMove(start, end, board, rowDif, colDif);
		boolean attackBool = checkAttack(start, end, board, rowDif, colDif);
		return moveBool || attackBool;
	}
	
	private boolean checkMove(Location start, Location end, Board board, int rowDif, int colDif) {
		if(colDif != 0) {
			return false;
		}
		if(rowDif == direction) {
			Piece p = board.getPieceAt(end);
			return p == null;
		}
		if(rowDif == 2 * direction) {
			Location check = new Location(start.getRow() + direction, start.getCol());
			if(!board.isEmpty(check)) {
				return false;
			}
			Piece p = board.getPieceAt(end);
			if(p == null) {
				Location ghostPos = new Location(start.getRow() + direction, start.getCol());
				board.setGhost(ghostPos, getColor());
				this.ghost = true;
				return true;
			}
			return false;
		}
		return false;
	}
	
	private boolean checkAttack(Location start, Location end, Board board, int rowDif, int colDif) {
		if(Math.abs(colDif) != 1 || rowDif != direction) {
			return false;
		}
		Location check = new Location(start.getRow() + direction, start.getCol() + colDif);
		Piece p = board.getPieceAt(check);
		if(p == null || p.getColor() == getColor()) {
			return false;
		}
		if(p instanceof GhostPawn) {
			this.ghost = true;
		}
		return true;
	}
	
	public boolean getGhost() {
		return this.ghost;
	}
	
	public void resetGhost() {
		this.ghost = false;
	}
}
