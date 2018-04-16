package main;

/**
 * The Main class is where execution begins.
 */
public final class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private Main(String[] args) {
    this.args = args;
  }

  private String[] args;

  private void run() {

  }

}
