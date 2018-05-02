package poweractions;

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
/**
 * @author knorms
 *
 */
public abstract class PowerAction {
  private final Rarity rarity;
  private final Game game;
  private final Location whereCaptured;
  private static final Multimap<Rarity, String> POWER_ACTIONS;

  static {
    POWER_ACTIONS = HashMultimap.create();
    POWER_ACTIONS.put(Rarity.COMMON, "Adjust");
    POWER_ACTIONS.put(Rarity.COMMON, "SecondEffort");
    POWER_ACTIONS.put(Rarity.COMMON, "Shield");
    POWER_ACTIONS.put(Rarity.COMMON, "Swap");
    POWER_ACTIONS.put(Rarity.COMMON, "Rewind");
    POWER_ACTIONS.put(Rarity.RARE, "BlackHole");
    POWER_ACTIONS.put(Rarity.RARE, "EyeForEye");
    POWER_ACTIONS.put(Rarity.LEGENDARY, "Armageddon");
  }

  /**
   * Constructor takes a rarity, a game object, and the location where the
   * PowerAction was captured.
   *
   * @param rarity
   *          PowerAction rarity.
   * @param game
   *          Game to be affected by PowerAction.
   * @param whereCaptured
   *          Location where PowerAction was captured.
   */
  public PowerAction(Rarity rarity, Game game, Location whereCaptured) {
    this.rarity = rarity;
    this.game = game;
    this.whereCaptured = whereCaptured;
  }

  /**
   * Return list of 2 PowerActions at specified rarity.
   *
   * @param rarity
   *          Rarity of PowerActions to return.
   * @param game
   *          Game PowerAction will modify.
   * @param whereCaptured
   *          Location where PowerObject was captured.
   * @return List of 2 PowerActions.
   */
  public static List<PowerAction> ofRarity(Rarity rarity, Game game,
      Location whereCaptured) {
    List<String> availableActions = new ArrayList<>(POWER_ACTIONS.get(rarity));
    Collections.shuffle(availableActions);
    if (availableActions.size() >= 2) {
      availableActions = availableActions.subList(0, 2);
    }
    List<PowerAction> actionOptions = new ArrayList<>();
    availableActions.forEach((action) -> actionOptions
        .add(stringToAction(action, game, whereCaptured)));
    return actionOptions;
  }

  private static PowerAction stringToAction(String actionName, Game game,
      Location whereCaptured) {
    switch (actionName) {
      case "Adjust":
        return new Adjust(game, whereCaptured);
      case "SecondEffort":
        return new SecondEffort(game, whereCaptured);
      case "Shield":
        return new Shield(game, whereCaptured);
      case "BlackHole":
        return new BlackHole(game, whereCaptured);
      case "EyeForEye":
        return new EyeForEye(game, whereCaptured);
      case "Swap":
        return new Swap(game, whereCaptured);
      case "Rewind":
        return new Rewind(game, whereCaptured);
      case "Armageddon":
        return new Armageddon(game, whereCaptured);
      default:
        return null;
    }
  }

  /**
   * Get a String representing the expected format for valid user input needed
   * to execute the PowerAction.
   *
   * @return expected format of user input
   */
  public abstract String inputFormat();

  /**
   * Check whether user input is valid for PowerAction.
   *
   * @param input
   *          Object required to execute PowerAction (e.g. end location for
   *          additional move, location selection of black hole).
   * @return true if input is valid for PowerAction, otherwise false.
   */
  public abstract boolean validInput(Object input);

  /**
   * Execute the action.
   *
   * @param input
   *          Object that is whatever user input is required to execute the
   *          PowerAction (e.g the end location for additional move or the
   *          location to place a black hole).
   */
  public abstract void act(Object input);

  /**
   * @return the rarity
   */
  public Rarity getRarity() {
    return rarity;
  }

  /**
   * @return the game
   */
  public Game getGame() {
    return game;
  }

  /**
   * @return the whereCaptured
   */
  public Location getWhereCaptured() {
    return whereCaptured;
  }

}
