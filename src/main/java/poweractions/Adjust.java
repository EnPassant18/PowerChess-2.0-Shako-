package poweractions;

import java.util.ArrayList;
import java.util.List;

import board.Location;
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
  private List<Location> adjacentSquares;

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public Adjust(Game game, Location whereCaptured) {
    super(Rarity.COMMON, game, whereCaptured);

    int row = whereCaptured.getRow();
    int col = whereCaptured.getCol();

    adjacentSquares = new ArrayList<>();

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

    if (adjacentSquares.isEmpty()) {
      throw new IllegalStateException("ERROR: Cannot execute poweraction"
          + " 'Adjust' because there are no adjacent empty spaces.");
    }

  }

  @Override
  public String inputFormat() {
    return "[a-h][1-8] adjacent empty space";
  }

  @Override
  public boolean validInput(Object obj) {
    return obj instanceof Location && adjacentSquares.contains((Location) obj);
  }

  @Override
  public void act(Object obj) {
    getGame().executeMove(new Move(getWhereCaptured(), (Location) obj));
  }

  @Override
  public String toString() {
    return "Adjust: You may move the capturing "
        + "piece to an adjacent vacant square. Rarity: Common";
  }

}
