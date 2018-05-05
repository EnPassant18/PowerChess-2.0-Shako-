package poweractions;

import board.Location;
import game.Game;
import game.Move;
import powerups.PowerObject.Rarity;

/**
 * SecondEffort allows capturing piece to move again. Rarity: Common.
 *
 * @author knorms
 *
 */
public class SecondEffort extends PowerAction {

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public SecondEffort(Game game, Location whereCaptured) {
    super(Rarity.COMMON, game, whereCaptured, 2);
  }

  @Override
  public String inputFormat() {
    return "[a-h][1-8] ending location of legal move";
  }

  @Override
  public boolean validInput(Object input) {
    return input instanceof Location
        && getGame().validMove(new Move(getWhereCaptured(), (Location) input));
  }

  @Override
  public void act(Object input) {
    getGame().executeMove(new Move(getWhereCaptured(), (Location) input));
  }

  @Override
  public String toString() {
    return "Second Effort: You may move the capturing "
        + "piece capturing piece to move again. Rarity: Common";
  }

}
