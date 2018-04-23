package repl;

/**
 * Evaluates a String command and returns a String.
 *
 * @author bbentz
 *
 */
public interface CommandEvaluator {

  /**
   * Evaluates a given String command.
   *
   * @param input
   *          Command.
   * @return Evaluation of the command.
   */
  String eval(String input);

}
