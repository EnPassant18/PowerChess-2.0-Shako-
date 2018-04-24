package powerups;

import java.util.List;

import board.Board;
import board.BoardObject;
import game.Move;
import poweractions.PowerAction;
import randutils.RandomCollection;

/**
 * PowerObject represents a powerup that can appear on the game board and be
 * captured.
 *
 * @author knorms
 *
 */
public class PowerObject implements BoardObject {
  private Rarity rarity;

  private static RandomCollection<Rarity> rarities;
  private static final int COMMON_FREQ = 73;
  private static final int RARE_FREQ = 25;
  private static final int LEGENDARY_FREQ = 2;

  static {
    rarities = new RandomCollection<>();
    rarities.add(COMMON_FREQ, Rarity.COMMON);
    rarities.add(RARE_FREQ, Rarity.RARE);
    rarities.add(LEGENDARY_FREQ, Rarity.LEGENDARY);
  }

  /**
   * Rarity represents the frequency with which a given PowerAction will be
   * offered.
   *
   * @author knorms
   *
   */
  public enum Rarity {
    COMMON, RARE, LEGENDARY
  }

  /**
   * Generate a new PowerObject at a random rarity, where commons more likely
   * than rares, which are more likely than legendary.
   *
   * @return a PowerObject.
   */
  public static PowerObject createRandPowerObject() {
    return new PowerObject(rarities.next());
  }

  /**
   * Construct PowerObject of specified rarity.
   *
   * @param rarity
   *          Rarity of PowerObject (Common, Rare, or Legendary).
   */
  public PowerObject(Rarity rarity) {
    this.rarity = rarity;
  }

  /**
   * Get a list of PowerActions allowed upon capturing this PowerObject.
   *
   * @return randomly generated list of 3 PowerActions.
   */
  public List<PowerAction> getPowerActions() {
    return PowerAction.ofRarity(rarity);
  }

  @Override
  public boolean move(Move move, Board board) {
    return false; // powerobjects cannot be moved
  }

  @Override
  public boolean canBeJumped() {
    return false;
  }

}
