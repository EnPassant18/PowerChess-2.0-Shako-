package poweractions;

import board.Location;
import game.Game;
import pieces.Piece;
import powerups.PowerObject.Rarity;

/**
 * Eye for an Eye allows a player to destroy the piece that captured the
 * PowerObject and an opposing piece of equal or lesser point value.
 *
 * @author knorms
 *
 */
public class EyeForEye extends PowerAction {
  private Piece capturingPiece;

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public EyeForEye(Game game, Location whereCaptured) {
    super(Rarity.RARE, game, whereCaptured);
    capturingPiece = game.getPieceAt(whereCaptured);
  }

  @Override
  public String inputFormat() {
    return "[a-h][1-8] location of opposing piece to destroy";
  }

  @Override
  public boolean validInput(Object input) {
    try {
      Piece p = getGame().getPieceAt((Location) input);
      return capturingPiece.getColor() != p.getColor()
          && capturingPiece.getRank() >= p.getRank();
    } catch (ClassCastException | NullPointerException e) {
      return false;
    }
  }

  @Override
  public void act(Object input) {
    getGame().removePieceAt((Location) input);
    getGame().removePieceAt(getWhereCaptured());
  }

  @Override
  public String toString() {
    return "Eye for an Eye: destroy capturing piece and an opposing "
        + "piece of equal or lesser point value.";
  }

}
