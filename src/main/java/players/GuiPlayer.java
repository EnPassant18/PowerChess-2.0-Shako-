package players;

import game.Color;

/**
 * A Player that can play a game of chess via interaction with the command line
 * interface.
 *
 * @author knorms
 *
 */
public class GuiPlayer extends Player {

  /**
   * Constructs command line interface player of specified color.
   *
   * @param color
   *          Player color.
   */
  public GuiPlayer(Color color) {
    super(color);
  }

}
