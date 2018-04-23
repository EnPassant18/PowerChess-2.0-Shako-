package repl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import stringutils.StringUtils;
import stringutils.TokenMatcherList;

/**
 * Maps strings describing commands to their functionality.
 *
 * @author bbentz
 *
 */
public class CommandMap implements CommandEvaluator {

  private Map<String, CommandEvaluator> commandMap;
  private Map<String, TokenMatcherList> tokenMatcherMap;

  /**
   * Public constructor for CommandMap.
   */
  public CommandMap() {
    commandMap = new HashMap<>();
    tokenMatcherMap = new HashMap<>();
  }

  /**
   * Adds a command to the command map.
   *
   * @param command
   *          String describing the command to be added.
   * @param evaluator
   *          Evaluator of the command.
   */
  public void add(final String command, final CommandEvaluator evaluator) {
    commandMap.put(command, evaluator);
  }

  /**
   * Adds a specific command pattern to the command map.
   *
   * @param command
   *          Name of the command.
   * @param pattern
   *          Pattern of the command.
   * @param function
   *          Function accepting tokens of the pattern as input.
   */
  public void add(final String command, final String pattern,
      final Function<List<String>, String> function) {

    TokenMatcherList tokenMatcherList;

    if (tokenMatcherMap.containsKey(command)) {
      tokenMatcherList = tokenMatcherMap.get(command);
    } else {
      tokenMatcherList = new TokenMatcherList();
      tokenMatcherMap.put(command, tokenMatcherList);
      add(command, tokenMatcherList);
    }

    tokenMatcherList.addMatcher(pattern, function);

  }

  /**
   * Adds a CommandMap to the CommandEvaluator. Adds the commands in the
   * CommandMap to this CommandMap object.
   *
   * @param map
   *          CommandMap to be added to the evaluator.
   *
   */
  public void add(final CommandMap map) {
    for (String command : map.getCommandSet()) {
      add(command, s -> map.eval(s));
    }
  }

  /**
   * Gets the set of commands for the command map.
   *
   * @return Set of defined commands.
   */
  public Set<String> getCommandSet() {
    return commandMap.keySet();
  }

  @Override
  public String eval(String input) {
    String firstToken = StringUtils.firstToken(input);

    if (commandMap.containsKey(firstToken)) {
      return commandMap.get(firstToken).eval(input);
    }

    return errorMessage();
  }

  /**
   * Returns an error message for an invalid command.
   *
   * @return Error message for invalid command.
   */
  public String errorMessage() {
    return "ERROR: Invalid command.";
  }

}
