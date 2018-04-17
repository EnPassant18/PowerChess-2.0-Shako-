package powerups;

import java.util.ArrayList;
import java.util.List;

import board.Location;
import game.Color;
import game.Game;
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
    super("Common");
  }

  @Override
  void act(Location whereCaptured, Game game) {
    Color color = game.getColorAt(whereCaptured);

    int row = whereCaptured.getRow();
    int col = whereCaptured.getCol();

    List<Location> validMoves = new ArrayList<>();

    Location adjacentSqr;
    for (int i = row - 1; i <= row + 1; i++) {
      for (int j = col - 1; j <= col + 1; j++) {
        try {
          adjacentSqr = new Location(i, j);
          if (game.isEmpty(adjacentSqr)) {
            validMoves.add(adjacentSqr);
          }
        } catch (IllegalArgumentException e) {
          continue; // ignore invalid, off-board location
        }
      }
    }

    Pair<Location, Location> move;

    // loop until valid move is executed
    while (true) {
      // make player move the capturing piece
      move = game.getMove(color, whereCaptured);

      // check that attempted move is to adjacent square
      if (validMoves.contains(move.getRight())) {
        game.forceMove(move);
      }
    }
  }

}
