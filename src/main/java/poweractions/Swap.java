package poweractions;

import board.*;
import game.Game;
import pieces.Piece;
import powerups.PowerObject.Rarity;

/**
 * Allows the capturing player to swap the position of the
 * capturing piece with another of their pieces.
 * @author Brad
 *
 */
public class Swap extends PowerAction {
  private Piece capturingPiece;
  private Location whereCaptured;

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public Swap(Game game, Location whereCaptured) {
    super(Rarity.COMMON, game, whereCaptured, 4, 4);
    capturingPiece = game.getPieceAt(whereCaptured);
    this.whereCaptured = whereCaptured;
  }

  @Override
  public String inputFormat() {
    return "[a-h][1-8] location of piece to swap";
  }

  @Override
  public boolean validInput(Object input) {
    try {
      Piece p = getGame().getPieceAt((Location) input);
      return capturingPiece.getColor() == p.getColor();
    } catch (ClassCastException | NullPointerException e) {
      return false;
    }
  }

  @Override
  public void act(Object input) {
    getGame().getBoard().swap(whereCaptured, (Location) input);
  }

  @Override
  public String toString() {
    return "Swap: Swap the capturing piece with another of your pieces.";
  }

}
