package powerups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import board.Location;
import game.Game;
import powerups.PowerObject.Rarity;

/**
 * Represents an action that can be taken as the result of capturing a
 * PowerObject.
 *
 * @author knorms
 *
 */
public abstract class PowerAction {
  private final Rarity rarity;
  private static final Multimap<Rarity, PowerAction> POWER_ACTIONS;

  static {
    POWER_ACTIONS = HashMultimap.create();
    POWER_ACTIONS.put(Rarity.COMMON, new Adjust());
    POWER_ACTIONS.put(Rarity.COMMON, new SecondEffort());
  }

  /**
   * Constructor takes a Game object.
   *
   * @param rarity
   *          PowerAction rarity.
   */
  public PowerAction(Rarity rarity) {
    this.rarity = rarity;
  }

  /**
   * Return list of 3 PowerActions at specified rarity.
   *
   * @param rarity
   *          Rarity of PowerActions to return.
   * @return List of 3 PowerActions.
   */
  public static List<PowerAction> ofRarity(Rarity rarity) {
    List<PowerAction> availableActions =
        new ArrayList<>(POWER_ACTIONS.get(rarity));
    Collections.shuffle(availableActions);
    return availableActions.subList(0, 3);
  }

  /**
   * Execute the action.
   *
   * @param whereCaptured
   *          The board location where the PowerObject associated with this
   *          PowerAction was captured (i.e. the location of the capturer
   *          piece).
   * @param game
   *          Game that the powerAction will modify
   */
  public abstract void act(Location whereCaptured, Game game);

  /**
   * @return the rarity
   */
  public Rarity getRarity() {
    return rarity;
  }

}
