package poweractions;

import java.util.ArrayList;
import java.util.List;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.Game;
import game.Move;
import powerups.PowerObject.Rarity;

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
  public void act(Location whereCaptured, Game game) {
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

    Move move;

    // loop until valid move is executed
    while (true) {
      // make player move the capturing piece
      while (true) {
        try {
          move = game.getMove(color, whereCaptured);
          break;
        } catch (IllegalMoveException e) {
          System.out.println(e.getMessage());
        }
      }

      // check that attempted move is to an empty adjacent square
      if (adjacentSquares.contains(move.getEnd())) {
        game.executeMove(move);
        break;
      }
    }
  }

}
