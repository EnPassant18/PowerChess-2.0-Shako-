package poweractions;

import java.util.Random;

import board.Location;
import game.Color;
import game.Game;
import game.Move;
import pieces.Piece;
import powerups.PowerObject.Rarity;

/**
 * SendAway allows a player to return any one piece on the board to a random
 * square on the owner's back rank. Rarity: Rare.
 *
 * @author knorms
 *
 */
public class SendAway extends PowerAction {
  private final int lastRank = 7;

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public SendAway(Game game, Location whereCaptured) {
    super(Rarity.RARE, game, whereCaptured, 4);
  }

  @Override
  public String inputFormat() {
    return "[a-h][1-8] location of piece to be sent away";
  }

  @Override
  public boolean validInput(Object input) {
    try {
      Piece p = getGame().getPieceAt((Location) input);
      return p != null;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public void act(Object input) {
    Random rand = new Random();
    int col = rand.nextInt(lastRank + 1);
    Piece p = getGame().getPieceAt((Location) input);
    int row = p.getColor() == Color.WHITE ? 0 : lastRank;
    getGame().executeMove(new Move(getWhereCaptured(), new Location(row, col)));
  }

  @Override
  public String toString() {
    return "Send Away: return any one piece on the "
        + "board to a random square on the owner's back rank.";
  }

}
