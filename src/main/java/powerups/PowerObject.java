package powerups;

import java.util.List;

import board.Board;
import board.BoardObject;
import board.Location;
import game.Game;
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
   * Generate a new PowerObject of a specified rarity.
   *
   * @param rarity
   *          Rarity of PowerObject.
   * @return a PowerObject of the specified rarity.
   */
  public static PowerObject ofRarity(Rarity rarity) {
    return new PowerObject(rarity);
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
   * Get rarity of PowerObject.
   *
   * @return PowerObject rarity.
   */
  public Rarity getRarity() {
    return this.rarity;
  }

  /**
   * Get a list of PowerActions allowed upon capturing this PowerObject.
   *
   * @param game
   *          Game PowerAction will modify.
   * @param whereCaptured
   *          Location where PowerObject was captured.
   * @return randomly generated list of 2 PowerActions.
   */
  public List<PowerAction> getPowerActions(Game game, Location whereCaptured) {
    return PowerAction.ofRarity(rarity, game, whereCaptured);
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
