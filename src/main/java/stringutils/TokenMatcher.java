package stringutils;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * Used to test if a list of tokens matches a given pattern.
 *
 * @author bbentz
 *
 */
public class TokenMatcher {

  private final List<String> tokens;
  private Function<List<String>, String> function;

  /**
   * No args constructor.
   */
  public TokenMatcher() {
    tokens = null;
    function = null;
  }

  /**
   * Constructor. Intended to be called from TokenPattern.
   *
   * @param tokens
   *          Input tokens.
   */
  protected TokenMatcher(final List<String> tokens) {
    this.tokens = tokens;
  }

  /**
   * Sets the function for the token matcher.
   *
   * @param fun
   *          Function that belongs to the token matcher.
   */
  public void setFunction(final Function<List<String>, String> fun) {
    function = fun;
  }

  /**
   * Returns true if the given input string matches the tokens.
   *
   * @param input
   *          A string of tokens.
   * @return Returns true if the given input string matches the tokens.
   */
  public boolean matches(final String input) {
    List<String> inputTokens = StringUtils.tokenize(input);
    if (inputTokens.size() != tokens.size()) {
      return false;
    }
    for (int i = 0; i < inputTokens.size(); i++) {
      if (!tokenMatches(inputTokens.get(i), tokens.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks to see if one token matches the corresponding string.
   *
   * @param input
   *          String that claims to match a token.
   * @param token
   *          Token that claims to match the string.
   *
   * @return True if the string matches the token.
   */
  public static boolean tokenMatches(final String input, final String token) {
    switch (token) {
      case "%d":
        return tryTo(s -> Integer.parseInt(s), input);
      case "%f":
        return tryTo(s -> Double.parseDouble(s), input);
      case "%q":
        return StringUtils.isStringQuoted(input);
      case "%s":
        return true;
      case "%sf":
        return true;
      default:
        return input.equals(token);
    }
  }

  private static String tokenToString(final String token) {
    switch (token) {
      case "%d":
        return "[Integer]";
      case "%f":
        return "[Double]";
      case "%q":
        return "[Quoted String]";
      case "%s":
        return "[String]";
      case "%sf":
        return "[Filepath]";
      default:
        return token;
    }
  }

  /**
   * Evaluates the function on tokens if the input matches the pattern.
   *
   * @param input
   *          Input to the function in this TokenMatcher object.
   * @return The output of the function
   */
  public String eval(final String input) {
    if (!matches(input)) {
      throw new IllegalArgumentException(
          "ERROR: Tried to evaluate TokenMatcher on "
              + "string that does not match the tokens.");
    }
    return function.apply(StringUtils.tokenize(input));
  }

  /**
   * Outputs a usage string for the TokenMatcher.
   *
   * @return Usage string for the TokenMatcher.
   */
  public String usageString() {

    StringJoiner output = new StringJoiner(" ");

    if (tokens.size() == 0) {
      return output.add("<No Arguments>").toString();
    }

    for (String token : tokens) {
      output.add(tokenToString(token));
    }

    return output.toString();
  }

  /**
   * Tries to apply a function. Returns true if there is no exception thrown,
   * otherwise returns false.
   *
   * @param function
   * @param input
   * @return
   */
  private static <U, V> boolean tryTo(final Function<U, V> function,
      final U input) {
    try {
      function.apply(input);
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }

}
