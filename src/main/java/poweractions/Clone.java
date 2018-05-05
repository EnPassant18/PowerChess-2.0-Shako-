package poweractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import board.*;
import game.Game;
import game.Move;
import game.Color;
import powerups.PowerObject.Rarity;
import pieces.Piece;

/**
 * Place a copy of this piece on a random square in your back rank. Rarity:
 * Legendary.
 *
 * @author brad
 * 
 */
public class Clone extends PowerAction {
  
  private final static int SIZE = 8;
  private List<Location> vacantSquares;
  private Location endLocation;

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
    
    Board board = game.getBoard();
    Piece capturingPiece = board.getPieceAt(whereCaptured);
    Color color = capturingPiece.getColor();
    int row = color == Color.WHITE ? 0 : SIZE - 1;
    
    Location loc;
    vacantSquares = new ArrayList<>();
    
    for(int j = 0; j < SIZE; j++) {
      loc = new Location(row, j);
      if(board.isEmpty(loc)) {
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
    if(vacantSquares.isEmpty()) {
      return;
    }
    endLocation = getRandomLocation();
  }
  
  /**
   * Returns end location.
   * @return
   *    End location.
   */
  public Location getEndLocation() {
    return endLocation;
  }
  
  private Location getRandomLocation() {
    if(vacantSquares.isEmpty()) {
      return null;
    }
    
    int index = ThreadLocalRandom.current().nextInt(0, vacantSquares.size());
  
    return vacantSquares.get(index);  
  }

  @Override
  public String toString() {
    return "Clone: Place a copy of this piece on a random empty square on your back rank. (If no empty squares, does nothing.)";
  }

}
