package powerups;

import board.BoardObject;

/**
 * A PowerUp is a BoardObject that describes a power-up that follows a piece
 * around (e.g. invulnerability).
 *
 * @author knorms
 *
 */
public abstract class PowerUp implements BoardObject {
  private int turnsRemaining;

  /**
   * Default constructor allows PowerUp to exist forever.
   */
  public PowerUp() {
    this.turnsRemaining = Integer.MAX_VALUE;
  }

  /**
   * Constructs PowerUp with specified number of turns before it disappears.
   *
   * @param turnsRemaining
   *          Number of turns before PowerUp disappears
   */
  public PowerUp(int turnsRemaining) {
    this.turnsRemaining = turnsRemaining;
  }

  /**
   * Decrement the number of turns a PowerUp has to remain on the board.
   */
  public void decrementTurns() {
    if (Integer.MAX_VALUE != turnsRemaining) {
      turnsRemaining--;
    }
  }

  /**
   * Check the number of turns remaining before the PowerUp disappears.
   *
   * @return turns remaining.
   */
  public int getTurnsRemaining() {
    return turnsRemaining;
  }

  /**
   * Set the number of turns remaining before the PowerUp disappears.
   *
   * @param turnsRemaining
   *          Number of turns PowerUp should exist before disappearing.
   */
  public void setTurnsRemaining(int turnsRemaining) {
    this.turnsRemaining = turnsRemaining;
  }

  /**
   * Check whether PowerUp should be removed (i.e. has 0 turns remaining).
   *
   * @return true if time to remove PowerUp, false otherwise.
   */
  public boolean toRemove() {
    return turnsRemaining <= 0;
  }

}
