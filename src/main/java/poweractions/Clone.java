package poweractions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import board.Board;
import board.Location;
import game.Color;
import game.Game;
import game.Move;
import pieces.Piece;
import powerups.PowerObject.Rarity;

/**
 * Place a copy of this piece on a random square in your back rank. Rarity:
 * Legendary.
 *
 * @author brad
 *
 */
public class Clone extends PowerAction implements PieceMover {
  
  private final static int SIZE = 8;
  private List<Location> vacantSquares;
  private Location startLocation, endLocation;
  private Board board;

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public Clone(Game game, Location whereCaptured) {
    super(Rarity.LEGENDARY, game, whereCaptured, 0);
    
    board = game.getBoard();
    Piece capturingPiece = board.getPieceAt(whereCaptured);
    Color color = capturingPiece.getColor();
    int row = color == Color.WHITE ? 0 : SIZE - 1;
    startLocation = whereCaptured;
    Location loc;
    vacantSquares = new ArrayList<>();

    for (int j = 0; j < SIZE; j++) {
      loc = new Location(row, j);
      if (board.isEmpty(loc)) {
        vacantSquares.add(loc);
      }
    }

  }

  @Override
  public String inputFormat() {
    return null;
  }

  @Override
  public boolean validInput(Object obj) {
    return true;
  }

  @Override
  public void act(Object obj) {
    if (vacantSquares.isEmpty()) {
      return;
    }
    endLocation = getRandomLocation();
    getGame().executeMove(new Move(startLocation, endLocation));
  }

  /**
   * Returns end location.
   *
   * @return End location.
   */
  public Location getEndLocation() {
    return endLocation;
  }

  private Location getRandomLocation() {
    if (vacantSquares.isEmpty()) {
      return null;
    }
    
    Collections.shuffle(vacantSquares);
    return vacantSquares.get(0);
  }

  @Override
  public String toString() {
    return "Clone: Place a copy of this piece on a random empty square on your "
        + "back rank. (If no empty squares, does nothing.)";
  }

}
