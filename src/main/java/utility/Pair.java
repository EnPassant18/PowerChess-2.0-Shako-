package utility;

/**
 * Pair of objects.
 *
 * @author knorms
 *
 * @param <L>
 *          Left object type.
 * @param <R>
 *          Right object type.
 */
public class Pair<L, R> {
  private L left;
  private R right;

  /**
   * Constructs a pair of objects.
   *
   * @param left
   *          left object.
   * @param right
   *          right object.
   */
  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  /**
   * @return the left
   */
  public L getLeft() {
    return left;
  }

  /**
   * @param left
   *          the left to set
   */
  public void setLeft(L left) {
    this.left = left;
  }

  /**
   * @return the right
   */
  public R getRight() {
    return right;
  }

  /**
   * @param right
   *          the right to set
   */
  public void setRight(R right) {
    this.right = right;
  }

}
