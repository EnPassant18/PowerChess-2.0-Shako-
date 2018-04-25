package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import board.Board;
import board.BoardObject;
import board.IllegalMoveException;
import board.Location;
import pieces.GhostPawn;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import players.Player;
import poweractions.PowerAction;
import powerups.PowerObject;
import randutils.RandomCollection;

/**
 * Game represents a game of chess.
 *
 * @author knorms
 *
 */
public class Game {
  private List<Player> players;
  private int activePlayerIndex;
  private Board board;
  private boolean gameOver;
  private List<Move> history; // list past moves
  private int tilNextPowerup;
  private Random rand = new java.util.Random();
  private final int lastRow = 7;
  private Location toPromote;

  private static RandomCollection<Location> spawnLocations;
  private static final int INNER_ROW_FREQ = 50;
  private static final int INNER_COL_FREQ = 20;
  private static final int OUTTER_ROW_FREQ = 30;
  private static final int OUTTER_COL_FREQ = 10;

  static {
    spawnLocations = new RandomCollection<>();
    int spawnFreq = 0;

    for (int row = 2; row < 6; row++) {
      for (int col = 0; col < Board.SIZE; col++) {

        // weight inner rows more highly for spawning frequency
        if (row == 2 || row == 5) {
          spawnFreq += OUTTER_ROW_FREQ;
        } else {
          spawnFreq += INNER_ROW_FREQ;
        }

        // weight inner columns slightly more highly for spawning frequency
        if (col < 2 || col > 5) {
          spawnFreq += OUTTER_COL_FREQ;
        } else {
          spawnFreq += INNER_COL_FREQ;
        }

        spawnLocations.add(spawnFreq, new Location(row, col));
        spawnFreq = 0;
      }
    }
  }

  /**
   * Constructs new default game.
   */
  public Game() {
    board = new Board();
    history = new ArrayList<>();
    players = new ArrayList<>();
    gameOver = false;
    updateTilNextPowerUp();
    toPromote = null;
  }

  /**
   * Start the game.
   */
  public void start() {
    while (!gameOver) {
      try {
        turn();
      } catch (IllegalMoveException e) {
        continue;
      }

      while (toPromote != null) {
        try {
          executePromotion(toPromote);
          toPromote = null;
        } catch (IllegalPromotionException e) {
          continue;
        }
      }
    }
  }

  /**
   * Adds a player to the game.
   *
   * @param player
   *          Player to be added.
   */
  public void addPlayer(final Player player) {
    players.add(player);
  }

  /**
   * Execute active player's turn.
   *
   * @throws IllegalMoveException
   *           If player attempts to move an empty space or ghost pawn or if
   *           other player's turn.
   * @throws IllegalPromotionException
   *           If player has not selected promotion or attempts to promote to
   *           Pawn or King.
   * @throws IllegalStateException
   *           If attempted to get player move before it has been set.
   */
  public void turn() throws IllegalMoveException, IllegalStateException {
    Player player = getActivePlayer();
    Move move = player.getMove();

    Piece p = board.getPieceAt(move.getStart());

    // check there is a piece to move at specified square
    if (p == null || p instanceof GhostPawn) {
      throw new IllegalMoveException(
          String.format("ERROR: No piece available to move at %s.",
              move.getStart().toString()));

      // check that piece belongs to active player
    } else if (p.getColor() != player.getColor()) {
      throw new IllegalMoveException(String.format(
          "ERROR: Wrong player, currently %s to move.", player.getColor()));
    }

    while (!validMove(move)) {
      throw new IllegalMoveException(String.format(
          "ERROR: Move is invalid for %s", p.getClass().getSimpleName()));
    }
    executeMove(move);

    // after move, check if new PowerObject should spawn
    if (tilNextPowerup == 0) {
      spawnPowerObject(getSpawnLoc(), PowerObject.createRandPowerObject());
      updateTilNextPowerUp();
    }

    // update active player and reset GhostPawns
    activePlayerIndex = (activePlayerIndex + 1) % 2;
    board.resetGhost(activePlayerIndex);

    // if promotion, execute
    Location end = move.getEnd();
    if ((end.getRow() == lastRow || end.getRow() == 0)
        && board.getPieceAt(end) instanceof Pawn) {
      toPromote = end;
    }

  }

  /**
   * Updates the number of turns until the next PowerObject will spawn; usually
   * uniformly random between 3 and 5 (inclusive).
   */
  private void updateTilNextPowerUp() {
    tilNextPowerup = rand.nextInt(3) + 2;
  }

  /**
   * Randomly select a board location on which to spawn a PowerObject; allowable
   * board locations range from rows 2 to 5 (inclusive) and includes all
   * columns. Inner columns, 2 through 5 (inclusive), and inner rows, 3 and 4,
   * are weighted more highly.
   *
   * @return Empty board location where power-up can be spawned.
   */
  private Location getSpawnLoc() {
    Location selection = spawnLocations.next();
    while (!board.isEmpty(selection)) {
      selection = spawnLocations.next();
    }
    return selection;
  }

  /**
   * Spawn capturable PowerObject on the board at specified location.
   *
   * @param loc
   *          Location where to place PowerObject.
   * @param powerObj
   *          PowerObject to add to board.
   */
  public void spawnPowerObject(Location loc, PowerObject powerObj) {
    board.addBoardObject(loc, powerObj);
  }

  /**
   * Check whether specified move is valid.
   *
   * @param move
   *          Move to check.
   * @return true if valid, false otherwise.
   * @throws IllegalMoveException
   *           If no piece or only GhostPawn at move starting location.
   */
  public boolean validMove(Move move) throws IllegalMoveException {
    Location start = move.getStart();
    Piece p = board.getPieceAt(start);
    if (!(p instanceof Piece)) {
      throw new IllegalMoveException(String
          .format("ERROR: No piece to move at starting location %s", start));
    } else if (p instanceof GhostPawn) {
      throw new IllegalMoveException("ERROR: Cannot move ghost pawn");
    }

    Piece piece = board.getPieceAt(start);

    return piece.move(move, board);
  }

  /**
   * Get next move by player of specified color.
   *
   * @param color
   *          Color of player.
   * @return a move (a pair of locations representing start and ending locations
   *         of selected player move).
   */
  public Move getMove(Color color) {
    return getActivePlayer().getMove();
  }

  /**
   * Get active player.
   *
   * @return the player whose turn it is.
   */
  public Player getActivePlayer() {
    return players.get(activePlayerIndex);
  }

  /**
   * Get next move by player of specified color of whatever piece sits at
   * specified starting location.
   *
   * @param color
   *          Color of player.
   * @param start
   *          Starting location of piece to be moved.
   * @return a move (a pair of locations representing start and ending locations
   *         of selected player move).
   * @throws IllegalMoveException
   *           If player attempts to move piece other than piece at the
   *           specified starting location.
   */
  public Move getMove(Color color, Location start) throws IllegalMoveException {
    return getActivePlayer().getMove(start);
  }

  /**
   * Execute a move; does not check for validity. Updates the board and manages
   * casling and captured pieces/power-ups.
   *
   * @param move
   *          Move to execute.
   */
  public void executeMove(Move move) {
    Collection<BoardObject> captured = board.move(move);

    // add move to history
    history.add(move);

    Location end = move.getEnd();

    // manage captured power-ups or king
    manageCaptured(captured, end);

  }

  /**
   * Execute promotion.
   *
   * @param loc
   *          Location of piece to promote.
   * @throws IllegalPromotionException
   *           If player tries to promote to Pawn or King.
   */
  public void executePromotion(Location loc) throws IllegalPromotionException {
    Piece newPiece = getActivePlayer().getPromotion();
    try {
      board.replacePiece(loc, newPiece);
    } catch (IllegalMoveException e) {
      throw new IllegalPromotionException(e.getMessage());
    }
  }

  private void manageCaptured(Collection<BoardObject> captured,
      Location whereCaptured) {

    for (BoardObject obj : captured) {
      if (obj instanceof PowerObject) {
        List<PowerAction> actions = ((PowerObject) captured).getPowerActions();
        Player player = getActivePlayer();
        PowerAction powerup = player.selectPowerAction(actions);
        powerup.act(whereCaptured, this);

      } else if (obj instanceof King) {
        gameOver = true;
      }
    }

  }

  /**
   * Check whether given board location is empty.
   *
   * @param loc
   *          Location to check.
   * @return true if location is empty, otherwise false.
   */
  public boolean isEmpty(Location loc) {
    return board.isEmpty(loc);
  }

  /**
   * Get the color of the piece at specified location.
   *
   * @param loc
   *          Location of piece.
   * @return color of piece at location or null if no piece at specified
   *         location.
   */
  public Color getColorAt(Location loc) {
    try {
      return getPieceAt(loc).getColor();
    } catch (NullPointerException e) {
      return null;
    }
  }

  /**
   * Get the piece at specified location.
   *
   * @param loc
   *          Location of piece.
   * @return Piece at location or null if no piece at specified location.
   */
  public Piece getPieceAt(Location loc) {
    return board.getPieceAt(loc);
  }

  /**
   * Get all objects at a specified board location.
   *
   * @param loc
   *          Board location.
   * @return collection of objects at board location.
   */
  public Collection<BoardObject> getObjsAt(Location loc) {
    return board.getObjsAt(loc);
  }

  /**
   * Getter method for board.
   *
   * @return Current board of the game.
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Getter method for active player index.
   *
   * @return Current active player.
   */
  public int getActivePlayerIndex() {
    return activePlayerIndex;
  }

  /**
   * Checks whether or not the game is over.
   *
   * @return True if the game is over and false otherwise.
   */
  public boolean getGameOverStatus() {
    return gameOver;
  }

}
