package main;

import java.io.PrintWriter;
import java.io.StringWriter;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import repl.ChessProjectHandler;
import repl.Repl;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import websockets.ChessWebSocket;
import websockets.HomeWebSocket;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private ChessProjectHandler chessProjectHandler = new ChessProjectHandler();

  // Set to a small quantity in order to prevent floating point errors
  // from ruining the case where the input maximum radius is exactly the
  // distance two stars are from each other
  // private static final double delta = .01;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    Repl repl = new Repl();

    repl.add(chessProjectHandler);
    repl.run();

  }

  @SuppressWarnings("unchecked")
  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    Spark.webSocket("/play", ChessWebSocket.class);
    Spark.webSocket("/home", HomeWebSocket.class);

    Spark.get("/", new Route() {

      @Override
      public Object handle(Request req, Response res) throws Exception {
        res.redirect("home.html");
        return null;
      }

    });
    Spark.init();

  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  @SuppressWarnings("rawtypes")
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
