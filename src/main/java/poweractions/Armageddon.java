package poweractions;

import board.*;
import game.*;
import pieces.*;
import powerups.*;
import powerups.PowerObject.Rarity;
import java.util.*;
/**
 * Destroy all pawns. All kings are invulnerable for the next two turns.
 * @author Brad
 *
 */
public class Armageddon extends PowerAction {

  /**
   * Constructor takes a game object and the location where the PowerAction was
   * captured.
   *
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public Armageddon(Game game, Location whereCaptured) {
    super(Rarity.LEGENDARY, game, whereCaptured);
  }

  @Override
  public String inputFormat() {
    return "";
  }

  @Override
  public boolean validInput(Object input) {
    return true;
  }

  @Override
  public void act(Object input) {
    Board board = getGame().getBoard();
    
    // Must separate to prevent ConcurrentModificationException
    Set<Location> pawnLocations = new HashSet<>();
    Set<Location> kingLocations = new HashSet<>();
    
    for(Location loc : board.getLocationSet()) {
      Piece p = board.getPieceAt(loc);
      
      if(p != null) {
        
        if(p instanceof Pawn) {
          pawnLocations.add(loc);
        } else if (p instanceof King) {
          kingLocations.add(loc);
        } 
        
      }
    }
    
    for(Location loc : pawnLocations) {
      board.removePieceAt(loc);
    }
    
    for(Location loc : kingLocations) {
      getGame().addPowerUp(loc, new Invulnerability(2));
    }
  }

  @Override
  public String toString() {
    return "Armageddon: Destroy all pawns. All kings are invulnerable for the next two turns.";
  }

}
