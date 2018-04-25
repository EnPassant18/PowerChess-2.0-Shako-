package repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Used to instantiate and run a REPL. Commands are evaluated by adding
 * ReplHandlers.
 *
 * @author bbentz
 *
 */
public class Repl extends CommandMap {

  /**
   * Creates new Repl object.
   */
  public Repl() {
    super();
  }

  /**
   * Begins the Read Evaluate Print Loop.
   */
  public void run() {
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

      String line = br.readLine();

      // EOF crashes my console
      while (line != null && !line.equalsIgnoreCase("QUIT")
          && !line.contains("\u001a")) {

        if ("".equals(line)) {
          line = br.readLine();
          continue;
        }

        String output = eval(line);
        if (!"".equals(output)) {
          System.out.println(output);
        }

        line = br.readLine();

      }
    } catch (IOException e) {
      System.err.println("ERROR: Could not read from console.");
    }
  }

}
