package stringutils;

import java.util.List;

/**
 * Used to create TokenMatcher object. Supports
 * floats/doubles/ints/strings/quoted strings/string literals.
 *
 * Right now basically a builder for Matcher. More functionality can be added
 * later.
 *
 * @author bbentz
 *
 */
public class TokenPattern {

  /*
   * private final String INT = "%d"; private final String FLOAT = "%f"; private
   * final String STRING = "%s"; private final String QUOTED_STRING = "%q";
   * private final String FILEPATH = "%sf";
   */

  private final String pattern;

  /**
   * Constructs a TokenPattern object with a given input string.
   *
   * @param pattern
   *          The given input String.
   */
  public TokenPattern(final String pattern) {
    this.pattern = pattern;
  }

  /**
   * Compiles the TokenPattern in to a TokenMatcher object.
   *
   * @return Returns the new TokenMatcher object.
   */
  public TokenMatcher compile() {
    List<String> tokens = StringUtils.tokenize(pattern);
    TokenMatcher tm = new TokenMatcher(tokens);
    return tm;
  }

}
