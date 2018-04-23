package repl;

import java.util.function.Function;

/**
 * Command Evaluator for the case where the command has one argument that is a
 * string. The string can contain whitespace.
 *
 * @author bbentz
 *
 */
public class StringCommandEvaluator implements CommandEvaluator {

  private Function<String, String> function;
  private String command;

  /**
   * Public constructor for StringCommandEvaluator.
   *
   * @param command
   *          Command for the evaluator.
   * @param function
   *          Function to be applied to the input.
   */
  public StringCommandEvaluator(final String command,
      final Function<String, String> function) {
    this.command = command;
    this.function = function;
  }

  @Override
  public String eval(String input) {
    if (input.equals(command)) {
      return function.apply("");
    } else if (input.startsWith(command + " ")) {
      return function.apply(input.substring(command.length() + 1));
    }
    return "ERROR: Invalid command. USAGE: " + command + " [String].";
  }

}
