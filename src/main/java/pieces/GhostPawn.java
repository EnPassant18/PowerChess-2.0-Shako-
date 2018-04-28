package pieces;

import board.Board;
import game.Color;
import game.Move;

/**
 * A GhostPawn is associated with a normal pawn and is left behind when a pawn
 * moves 2 spaces on its first move.
 *
 * @author dwoods
 *
 */
public class GhostPawn extends Piece {

  /**
   * Construct GhostPawn of the specified color.
   *
   * @param color
   *          Piece color.
   */
  public GhostPawn(Color color) {
    super(color);
  }

  @Override
  public boolean move(Move move, Board board) {
    return false;
  }

  @Override
  public int getRank() {
    // TODO Auto-generated method stub
    return 0;
  }
}
