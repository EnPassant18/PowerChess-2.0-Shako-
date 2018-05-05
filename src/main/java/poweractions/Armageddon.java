package poweractions;

import java.util.HashSet;
import java.util.Set;

import board.Board;
import board.Location;
import game.Game;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import powerups.Invulnerability;
import powerups.PowerObject.Rarity;

/**
 * Destroy all pawns. All kings are invulnerable for the next two turns.
 *
 * @author Brad
 *
 */
public class Armageddon extends PowerAction {
  private Set<Location> pawnLocations;
  private Set<Location> kingLocations;

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
    super(Rarity.LEGENDARY, game, whereCaptured, 0);
    pawnLocations = new HashSet<>();
    kingLocations = new HashSet<>();
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
    Board board = getGame().getBoard();

    // Must separate to prevent ConcurrentModificationException
    pawnLocations = new HashSet<>();
    kingLocations = new HashSet<>();

    for (Location loc : board.getLocationSet()) {
      Piece p = board.getPieceAt(loc);

      if (p != null) {

        if (p instanceof Pawn) {
          pawnLocations.add(loc);
        } else if (p instanceof King) {
          kingLocations.add(loc);
        }

      }
    }

    for (Location loc : pawnLocations) {
      board.removePieceAt(loc);
    }

    for (Location loc : kingLocations) {
      getGame().addPowerUp(loc, new Invulnerability(2));
    }
  }

  /**
   * @return the pawnLocations
   */
  public Set<Location> getPawnLocations() {
    return new HashSet<Location>(pawnLocations);
  }

  /**
   * @return the kingLocations
   */
  public Set<Location> getKingLocations() {
    return new HashSet<Location>(kingLocations);
  }

  @Override
  public String toString() {
    return "Armageddon: Destroy all pawns. All kings are "
        + "invulnerable for the next two turns.";
  }

}
