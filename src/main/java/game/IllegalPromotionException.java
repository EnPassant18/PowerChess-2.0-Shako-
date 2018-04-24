package game;

/**
 * IllegalPromotionException is thrown when a player selects an illegal piece
 * type to try to promote their pawn to.
 *
 * @author knorms
 *
 */
public class IllegalPromotionException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs an IllegalPromotionException with the specified detail message.
   *
   * @param message
   *          the detail message.
   */
  public IllegalPromotionException(String message) {
    super(message);
  }
}
