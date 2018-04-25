package stringutils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;

import repl.CommandEvaluator;

/**
 * Represents a list of token matchers. Used to group token matchers. In theory,
 * one input string should match at most one token matcher. When eval() is
 * called, this class searches for a TokenMatcher object that matches the input
 * string and calls eval with that matcher. Undefined behavior when multiple
 * matchers match the input.
 *
 * @author bbentz
 *
 */
public class TokenMatcherList implements CommandEvaluator {

  private List<TokenMatcher> matcherList;

  /**
   * Construct new TokenMatcherList object.
   */
  public TokenMatcherList() {
    matcherList = new ArrayList<>();
  }

  /**
   * Adds a TokenMatcher to the TokenMatcherList.
   *
   * @param tokens
   *          String representing the tokens to match.
   * @param fun
   *          Function to be called on the tokens.
   * @return This TokenMatcherList object.
   */
  public TokenMatcherList addMatcher(final String tokens,
      final Function<List<String>, String> fun) {
    TokenPattern tp = new TokenPattern(tokens);
    TokenMatcher tm = tp.compile();
    tm.setFunction(fun);
    return addMatcher(tm);
  }

  /**
   * Adds a TokenMatcher to the TokenMatcherList.
   *
   * @param tokenMatcher
   *          TokenMatcher to be added.
   * @return This TokenMatcherList object.
   */
  public TokenMatcherList addMatcher(final TokenMatcher tokenMatcher) {
    matcherList.add(tokenMatcher);
    return this;
  }

  /**
   * Evaluates an input string with the TokenMatcher that the input string
   * matches.
   *
   * @param input
   *          Input String.
   * @return Result of the matching matcher's eval() function or a usage message
   *         if no matchers match the input.
   */
  public String eval(final String input) {
    for (TokenMatcher tokenMatcher : matcherList) {
      if (tokenMatcher.matches(input)) {
        return tokenMatcher.eval(input);
      }
    }
    return "ERROR: Invalid command. " + usageString();
  }

  /**
   * Returns a usage string for the TokenMatcher.
   *
   * @return Usage string for the Token Matcher. Combines the usage string for
   *         each TokenMatcher in the matcher list.
   */
  public String usageString() {
    StringJoiner output = new StringJoiner(" OR ");
    for (TokenMatcher tokenMatcher : matcherList) {
      output.add(tokenMatcher.usageString());
    }
    return "USAGE: " + output.toString();
  }

}
