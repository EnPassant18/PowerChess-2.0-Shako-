package poweractions;

import java.util.List;

import board.Location;
import game.Game;
import game.Move;
import powerups.PowerObject.Rarity;

/**
 * Allows the capturing player to undo their opponents last move (if possible.)
 * Does not undo captures.
 * 
 * @author Brad
 *
 */
public class Rewind extends PowerAction {

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public Rewind(Game game, Location whereCaptured) {
    super(Rarity.COMMON, game, whereCaptured, 1, 0);
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
    List<Move> history = getGame().getHistory();
    Move move = history.get(history.size() - 2);

    Location start = move.getStart();
    Location end = move.getEnd();

    if (getGame().getBoard().isEmpty(start)) {
      getGame().getBoard().swap(start, end);
    }

  }

  @Override
  public String toString() {
    return "Rewind: Undo an opponents previous move (does not undo captures).";
  }

}
