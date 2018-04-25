package randutils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * A RadomCollection holds an assortment of objects that can be queried to
 * return a weighted, random selection.
 *
 * @author stackExchange
 *
 * @param <E>
 *          Type of object to be held by RandomCollection.
 */
public class RandomCollection<E> {
  private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
  private final Random random;
  private double total = 0;

  /**
   * Construct a new random collection.
   */
  public RandomCollection() {
    this(new Random());
  }

  /**
   * Construct a random collection with a specified random number generator.
   *
   * @param random
   *          Random number generator for collection to use.
   */
  public RandomCollection(Random random) {
    this.random = random;
  }

  /**
   * Add a new item to the collection with a specified weighting.
   *
   * @param weight
   *          Weight the likelihood that this collection will return the given
   *          result.
   * @param result
   *          The result.
   * @return the RandomCollection itself after the new item has been added.
   */
  public RandomCollection<E> add(double weight, E result) {
    if (weight <= 0) {
      return this;
    }
    total += weight;
    map.put(total, result);
    return this;
  }

  /**
   * Return a weighted, randomly selected item from the collection.
   *
   * @return an item from the collection.
   */
  public E next() {
    double value = random.nextDouble() * total;
    return map.higherEntry(value).getValue();
  }
}
