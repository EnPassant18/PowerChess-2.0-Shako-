package powerups;

import board.Location;
import game.Color;
import game.Game;
import powerups.PowerObject.Rarity;
import utility.Pair;

/**
 * SecondEffort allows capturing piece to move again. Rarity: Common.
 *
 * @author knorms
 *
 */
public class SecondEffort extends PowerAction {

  /**
   * Default constructor.
   */
  public SecondEffort() {
    super(Rarity.COMMON);
  }

  @Override
  public void act(Location whereCaptured, Game game) {
    Color color = game.getColorAt(whereCaptured);

    Pair<Location, Location> move;

    // loop until valid move is executed
    while (true) {
      // make player move the capturing piece
      move = game.getMove(color, whereCaptured);

      // must execute valid move
      if (game.makeMove(move)) {
        break;
      }
    }

  }

}
