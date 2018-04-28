package powerups;

/**
 * BlackHole marks a board location that cannot be landed on, but can be passed
 * over as normal.
 *
 * @author knorms
 *
 */
public class BlackHole extends Invulnerability {

  /**
   * Default constructor allows BlackHole to exist forever.
   */
  public BlackHole() {
  }

  /**
   * Constructs BlackHole with specified number of turns before it disappears.
   *
   * @param turnsRemaining
   *          Number of turns before PowerUp disappears
   */
  public BlackHole(int turnsRemaining) {
    super(turnsRemaining);
  }

  @Override
  public String toString() {
    return "a Black Hole";
  }

}
