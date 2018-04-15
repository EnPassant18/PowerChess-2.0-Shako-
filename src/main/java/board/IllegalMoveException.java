package board;

/**
 * Thrown when an illegal move is attempted.
 *
 * @author knorms
 *
 */
public final class IllegalMoveException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs an IllegalMoveException with the specified detail message.
   *
   * @param message
   *          the detail message.
   */
  public IllegalMoveException(String message) {
    super(message);
  }

}
