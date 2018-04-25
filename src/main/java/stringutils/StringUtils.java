package stringutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class providing some number of string utilities.
 *
 * @author bbentz
 *
 */
public final class StringUtils {

  private StringUtils() {
  }

  /**
   * Take a string of tokens separated by spaces and split it into a list of
   * tokens. Allows for spaces inside of quotes.
   *
   * @param str
   *          The string to tokenize.
   * @return A list of tokens.
   */
  public static List<String> tokenize(String str) {
    int index;
    List<String> output = new ArrayList<>();
    while (!"".equals(str)) {
      if (str.charAt(0) == '\"') {
        index = str.indexOf("\"", 1);
        if (index == -1) {
          throw new IllegalArgumentException(
              "ERROR: called tokenize on malformed string.");
        }
        index++;
      } else {
        index = str.indexOf(" ", 1);
      }
      if (index == -1) {
        output.add(str);
        return output;
      }
      output.add(str.substring(0, index));
      str = str.substring(index).trim();
    }

    return output;
  }

  /**
   * Returns the first token of a string or an empty string if the string is
   * empty.
   *
   * @param str
   *          String of tokens.
   * @return The first token of the string.
   */
  public static String firstToken(final String str) {
    List<String> tokens = tokenize(str);
    if (tokens.size() > 0) {
      return tokens.get(0);
    }
    return "";
  }

  /**
   * Returns true if the input string is quoted and false otherwise.
   *
   * @param str
   *          The string to check if is quoted.
   * @return True if the string is quoted and false otherwise.
   */
  public static boolean isStringQuoted(final String str) {
    if (str.length() < 2) {
      return false;
    }
    return str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"';
  }

  /**
   * Removes the quotes from around a quoted string.
   *
   * @param quotedString
   *          A quoted string.
   * @return The string without the quotes around it.
   */
  public static String parseQuotedString(final String quotedString) {
    if (!isStringQuoted(quotedString)) {
      throw new IllegalArgumentException(
          "ERROR: parseQuotedString called on not quoted string.");
    }
    return quotedString.substring(1, quotedString.length() - 1);
  }

  /**
   * Consumes two strings and returns the length of the longest shared prefix of
   * both strings.
   *
   * @param str1
   *          First string.
   * @param str2
   *          Second string.
   * @return The length of the longest shared prefix of the strings.
   */
  public static int sharedPrefixLength(final String str1, final String str2) {
    int output = 0;
    while (output < str1.length() && output < str2.length()
        && str1.charAt(output) == str2.charAt(output)) {
      output++;
    }
    return output;
  }

  /**
   * Returns the input distance between two strings.
   *
   * @param str1
   *          The first input string.
   * @param str2
   *          The second input string.
   * @return The edit distance (Levenshtein distance) between the strings
   */
  public static int editDistance(final String str1, final String str2) {
    int n = str1.length();
    int m = str2.length();

    if (n == 0 || m == 0) {
      return n + m;
    }

    // variables exist for checkstyle reasons
    int nPlusOne = n + 1;
    int mPlusOne = m + 1;
    int[][] arr = new int[nPlusOne][mPlusOne];

    for (int i = 0; i <= n; i++) {
      arr[i][0] = i;
    }

    for (int j = 0; j <= m; j++) {
      arr[0][j] = j;
    }

    int hasSubstitution;
    for (int j = 1; j <= m; j++) {
      for (int i = 1; i <= n; i++) {
        hasSubstitution = str2.charAt(j - 1) == str1.charAt(i - 1) ? 0 : 1;
        arr[i][j] = Math.min(arr[i - 1][j] + 1,
            Math.min(arr[i][j - 1] + 1, arr[i - 1][j - 1] + hasSubstitution));
      }
    }

    // Useful for debugging
    /*
     * if (false) { System.out.println("Edit distance between " + str1 + " and "
     * + str2 + "."); System.out.println(Arrays.deepToString(arr).replace("], ",
     * "]\n") .replace("[[", "[").replace("]]", "]")); }
     */

    return arr[n][m];
  }
}
