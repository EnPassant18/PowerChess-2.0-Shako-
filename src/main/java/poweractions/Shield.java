package poweractions;

import board.Location;
import game.Game;
import powerups.Invulnerability;
import powerups.PowerObject.Rarity;

/**
 * Shield makes the capturing piece invulnerable for the next 3 turns, or until
 * it captures, whichever is less. Rarity: Common.
 *
 * @author knorms
 *
 */
public class Shield extends PowerAction {

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public Shield(Game game, Location whereCaptured) {
    super(Rarity.COMMON, game, whereCaptured, 3, 0);
  }

  @Override
  public String inputFormat() {
    return null;
  }

  @Override
  public boolean validInput(Object input) {
    return true;
  }

  @Override
  public void act(Object input) {
    getGame().addPowerUp(getWhereCaptured(), new Invulnerability(6));
  }

  @Override
  public String toString() {
    return "Shield: you may make the capturing piece invulnerable "
        + "for the next 3 turns, or until it captures, whichever "
        + "is less. Rarity: Common.";
  }

}
