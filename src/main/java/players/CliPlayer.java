package players;

import java.util.List;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.IllegalPromotionException;
import game.Move;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import poweractions.PowerAction;

/**
 * A Player that can play a game of chess via interaction with the command line
 * interface.
 *
 * @author knorms
 *
 */
public class CliPlayer extends Player {

  private Move nextMove;
  private Piece newPiece; // for promotion

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
    if (nextMove == null) {
      throw new IllegalStateException(
          "ERROR: called getMove() before setMove() in CliPlayer.");
    } else if (!nextMove.getStart().equals(start)) {
      throw new IllegalMoveException(
          String.format("ERROR: Player must move %s next.", start.toString()));
    }
    Move rtn = nextMove;
    nextMove = null;
    return rtn;
  }

  /**
   * Set the player's next choice for promotion.
   *
   * @param promotion
   *          Piece to promote to.
   * @throws IllegalPromotionException
   *           If player chooses illegal piece (i.e. Pawn or King).
   */
  public void setPromotion(Piece promotion) throws IllegalPromotionException {
    if (promotion instanceof Pawn || promotion instanceof King) {
      throw new IllegalPromotionException(
          String.format("ERROR: Cannot promote to %s.",
              promotion.getClass().getSimpleName()));
    }
    newPiece = promotion;
  }

  @Override
  public Piece getPromotion() throws IllegalPromotionException {
    if (newPiece == null) {
      throw new IllegalPromotionException(
          "ERROR: called getPromotion() before setPromotion in CliPlayer.");
    }
    Piece rtn = newPiece;
    newPiece = null;
    return rtn;
  }

  @Override
  public PowerAction selectPowerAction(List<PowerAction> actions) {
    // TODO Auto-generated method stub
    return null;
  }

}
