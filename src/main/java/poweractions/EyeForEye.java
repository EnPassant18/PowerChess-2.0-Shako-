package poweractions;

import java.util.Map;

import board.Location;
import game.Game;
import pieces.Piece;
import powerups.Invulnerability;
import powerups.PowerObject.Rarity;
import powerups.PowerUp;

/**
 * Eye for an Eye allows a player to destroy the piece that captured the
 * PowerObject and an opposing piece of equal or lesser point value. Rarity:
 * Rare.
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
    super(Rarity.RARE, game, whereCaptured, 2);
    capturingPiece = game.getPieceAt(whereCaptured);
  }

  @Override
  public String inputFormat() {
    return "[a-h][1-8] location of opposing piece of lesser or equal rank"
        + " to destroy";
  }

  @Override
  public boolean validInput(Object input) {
    try {
      Location inputLoc = (Location) input;
      Piece p = getGame().getPieceAt(inputLoc);
      Map<Location, PowerUp> onBoardPowers = getGame().getOnBoardPowers();
      for (Location powerLoc : onBoardPowers.keySet()) {
        if (powerLoc.equals(inputLoc)
            && onBoardPowers.get(powerLoc) instanceof Invulnerability) {
          return false;
        }
      }

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
