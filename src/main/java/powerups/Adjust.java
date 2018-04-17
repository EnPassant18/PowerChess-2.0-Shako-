package powerups;

import java.util.ArrayList;
import java.util.List;

import board.Location;
import game.Color;
import game.Game;
import powerups.PowerObject.Rarity;
import utility.Pair;

/**
 * Adjust allows capturing piece to move to an adjacent vacant square. Rarity:
 * Common.
 *
 * @author knorms
 *
 */
public class Adjust extends PowerAction {

  /**
   * Default constructor.
   */
  public Adjust() {
    super(Rarity.COMMON);
  }

  @Override
  void act(Location whereCaptured, Game game) {
    Color color = game.getColorAt(whereCaptured);

    int row = whereCaptured.getRow();
    int col = whereCaptured.getCol();

    List<Location> adjacentSquares = new ArrayList<>();

    // collect empty adjacent squares
    Location square;
    for (int i = row - 1; i <= row + 1; i++) {
      for (int j = col - 1; j <= col + 1; j++) {
        try {
          square = new Location(i, j);
          if (game.isEmpty(square)) {
            adjacentSquares.add(square);
          }
        } catch (IllegalArgumentException e) {
          continue; // ignore invalid off-board location
        }
      }
    }

    // if no empty adjacent squares, return
    if (adjacentSquares.size() == 0) {
      return;
    }

    Pair<Location, Location> move;

    // loop until valid move is executed
    while (true) {
      // make player move the capturing piece
      move = game.getMove(color, whereCaptured);

      // check that attempted move is to an empty adjacent square
      if (adjacentSquares.contains(move.getRight())) {
        game.forceMove(move);
        break;
      }
    }
  }

}
