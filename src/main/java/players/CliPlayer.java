package players;

import java.util.List;

import game.Color;
import poweractions.PowerAction;

/**
 * A Player that can play a game of chess via interaction with the command line
 * interface.
 *
 * @author knorms
 *
 */
public class CliPlayer extends Player {

  /**
   * Constructs command line interface player of specified color.
   *
   * @param color
   *          Player color.
   */
  public CliPlayer(Color color) {
    super(color);
  }

  @Override
  public PowerAction selectPowerAction(List<PowerAction> actions) {
    // TODO Auto-generated method stub
    return null;
  }

}
