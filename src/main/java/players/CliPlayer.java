package players;

import java.util.List;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.Move;
import pieces.Piece;
import powerups.PowerAction;

/**
 * A Player that can play a game of chess via interaction with the command line
 * interface.
 *
 * @author knorms
 *
 */
public class CliPlayer extends Player {

  private Move nextMove;

  /**
   * Constructs command line interface player of specified color.
   *
   * @param color
   *          Player color.
   */
  public CliPlayer(Color color) {
    super(color);
  }

  /**
   * Set the player's next move.
   *
   * @param move
   *          Next move to be executed by player.
   */
  public void setMove(final Move move) {
    nextMove = move;
  }

  @Override
  public Move getMove() {
    if (nextMove == null) {
      throw new IllegalStateException(
          "ERROR: called getMove() before setMove() in CliPlayer.");
    }
    Move output = nextMove;
    nextMove = null;
    return output;
  }

  @Override
  public Move getMove(Location start) throws IllegalMoveException {
    if (!nextMove.getStart().equals(start)) {
      throw new IllegalMoveException(
          String.format("ERROR: Player must move %s next.", start.toString()));
    }
    nextMove = null;
    return nextMove;
  }

  @Override
  public Piece getPromotion() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PowerAction selectPowerAction(List<PowerAction> actions) {
    // TODO Auto-generated method stub
    return null;
  }

}
