package poweractions;

import board.Location;
import game.Game;
import powerups.PowerObject.Rarity;

/**
 * Black Hole allows the player to place a 'black hole' on a vacant square. For
 * the next 6 turns, no piece can move onto that square. (Pieces can move over
 * the square as usual). Rarity: Rare.
 *
 * @author knorms
 *
 */
public class BlackHole extends PowerAction {

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public BlackHole(Game game, Location whereCaptured) {
    super(Rarity.RARE, game, whereCaptured, 0);
  }

  @Override
  public String inputFormat() {
    return "[a-h][1-8] empty location to place black hole";
  }

  @Override
  public boolean validInput(Object input) {
    return input instanceof Location && getGame().isEmpty((Location) input);
  }

  @Override
  public void act(Object input) {
    getGame().addPowerUp((Location) input, new powerups.BlackHole(12));
    getGame().getBoard().removeEmpty((Location) input);

  }

  @Override
  public String toString() {
    return "Black Hole: You may place a 'black hole' on a vacant square. For"
        + " the next 6 turns, no piece can move onto that square "
        + "(pieces can move over the square as usual).";
  }

}
