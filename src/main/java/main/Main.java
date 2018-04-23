package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

//import projects.ChessProjectHandler;
import repl.Repl;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import projects.ChessProjectHandler;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author jj
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

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes

    //Spark.get("/autocorrect", autoCorrectHandler.getGUIHandler(), freeMarker);
    //Spark.get("/stars", StarsHandler.getStarsFrontHandler(), freeMarker);
    //Spark.get("/bacon", BaconHandler.getGUIHandler(), freeMarker);
    //Spark.get("/bacon/actors/:name", baconHandler.getActorPage(), freeMarker);
    //Spark.get("/bacon/movies/:name", baconHandler.getMoviePage(), freeMarker);
    //Spark.post("/correct", autoCorrectHandler.getCorrectHandler());
    //Spark.post("/neighbors", starsHandler.getNeighborsHandler(), freeMarker);
    //Spark.post("/bacon/search", baconHandler.getResultsHandler(), freeMarker);
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
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
