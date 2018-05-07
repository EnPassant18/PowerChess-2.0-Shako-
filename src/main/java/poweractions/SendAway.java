package poweractions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class SendAway extends PowerAction implements PieceMover {
  private final int beyondBoard = 8;
  private List<Integer> columns;
  private Location endLocation = null;

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

    columns = new ArrayList<>();
    for (int i = 0; i < beyondBoard; i++) {
      columns.add(i);
    }
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
    Piece p = getGame().getPieceAt((Location) input);
    int row = p.getColor() == Color.WHITE ? 0 : beyondBoard - 1;
    Game game = getGame();

    Collections.shuffle(columns);
    int col;
    while (true) {
      col = columns.remove(0);
      if (game.isEmpty(new Location(row, col))) {
        endLocation = new Location(row, col);
        break;
      } else if (columns.isEmpty()) {
        return;
      }
    }

    game.executeMove(new Move((Location) input, endLocation));
  }

  /**
   * @return the endLocation
   */
  public Location getEndLocation() {
    return endLocation;
  }

  @Override
  public String toString() {
    return "Send Away: return any one piece on the "
        + "board to a random square on the owner's back rank.";
  }

}
