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
   * Constructs player of the specified color with the given name.
   *
   * @param color
   *          Color of player's pieces.
   * @param name
   *          Player name.
   * @param id
   *          Player Id.
   */
  public GuiPlayer(Color color, int id, String name) {
    super(color, id, name);
  }

}
