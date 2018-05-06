package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.google.common.collect.ImmutableMap;

import board.Board;
import board.BoardObject;
import board.IllegalMoveException;
import board.Location;
import pieces.GhostPawn;
import pieces.King;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import players.Player;
import poweractions.PowerAction;
import powerups.PowerObject;
import powerups.PowerUp;
import randutils.RandomCollection;
import repl.ChessReplUtils;

/**
 * Game represents a game of chess.
 *
 * @author knorms
 *
 */
public class Game {
  private Player whitePlayer;
  private Player blackPlayer;
  private boolean whiteToMove;

  private Board board;
  private boolean gameOver;

  private List<Move> history; // list past moves

  private int tilNextPowerup;
  private List<PowerAction> actionOptions;
  private Map<PowerUp, Location> powerUps;
  private Random rand = new java.util.Random();
  private final int lastRow = 7;

  private Location toPromote;

  private TimeControl timeControl;
  private boolean isPublic;

  private Map<PowerUp, Location> removedLocations;
  private Map<PowerObject, Location> addedPowerObject;

  private static final String START_POSITION_FEN =
      "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

  /**
   * GameState enumerates the various states of a game (e.g. waiting for a
   * player to make a move).
   *
   * @author bbentz
   *
   */
  public enum GameState {
    WAITING_FOR_MOVE, WAITING_FOR_PROMOTE, WAITING_FOR_POWERUP_CHOICE,
    WAITING_FOR_POWERUP_EXEC, GAME_OVER
  }

  /**
   * Enumerates options for game time controls.
   *
   * @author knorms
   *
   */
  public enum TimeControl {
    QUICK, STANDARD, SLOW
  }

  private GameState gameState;

  private static RandomCollection<Location> spawnLocations;
  private static final int INNER_ROW_FREQ = 60;
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
    this(START_POSITION_FEN);
  }

  /**
   * Constructor for a game starting from a given FEN.
   *
   * @param fen
   *          FEN.
   */
  public Game(final String fen) {
    if (!ChessReplUtils.isFenValid(fen)) {
      throw new IllegalArgumentException(
          "ERROR: Invalid FEN" + " given to Game constructor.");
    }

    String[] fenArray = fen.split("\\s+");

    String piecePlacement = fenArray[0];
    String activeColor = fenArray[1];
    String castling = fenArray[2];
    String enPassant = fenArray[3];

    board = new Board(fen);
    whiteToMove = activeColor.equals("w");

    history = new ArrayList<>();
    gameOver = false;
    updateTilNextPowerUp();
    toPromote = null;
    actionOptions = new ArrayList<>();
    powerUps = new TreeMap<>((p1, p2) -> {
      return Integer.compare(p1.getTurnsRemaining(), p2.getTurnsRemaining());
    });
    gameState = GameState.WAITING_FOR_MOVE;
    addedPowerObject = new HashMap<PowerObject, Location>();

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
        executePromotion(toPromote);
        toPromote = null;
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
    if (player.getColor() == Color.WHITE) {
      whitePlayer = player;
    } else {
      blackPlayer = player;
    }
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

    if (!validMove(move)) {
      throw new IllegalMoveException(String.format(
          "ERROR: Move is invalid for %s", p.getClass().getSimpleName()));
    }
    executeMove(move);
    tilNextPowerup--;

    // after move, check if new PowerObject should spawn
    if (tilNextPowerup == 0) {
      spawnPowerObject(getSpawnLoc(), PowerObject.createRandPowerObject());
      updateTilNextPowerUp();
    }

    // update active player and reset GhostPawns
    whiteToMove = !whiteToMove;
    board.resetGhost(getActivePlayer().getColor());

    // if promotion, execute
    Location end = move.getEnd();
    if ((end.getRow() == lastRow || end.getRow() == 0)
        && board.getPieceAt(end) instanceof Pawn) {
      toPromote = end;
      gameState = GameState.WAITING_FOR_PROMOTE;
      whiteToMove = !whiteToMove; // Change move back to previous turn
    }

    // if PowerObject was captured, switch active player back
    if (!actionOptions.isEmpty()) {
      whiteToMove = !whiteToMove;
    }

    // decrement lifetime of PowerUp
    removedLocations = new HashMap<PowerUp, Location>();
    List<PowerUp> toRemove = new ArrayList<>();
    powerUps.keySet().forEach((power) -> {
      power.decrementTurns();
      if (power.toRemove()) {
        removedLocations.put(power, powerUps.get(power));
        toRemove.add(power);
      }
    });
    for (PowerUp power : toRemove) {
      removePowerUp(power);
    }
  }

  /**
   * Returns a list of powerups removed after the last turn.
   *
   * @return list of power ups.
   */
  public Map<PowerUp, Location> getRemoved() {
    return this.removedLocations;
  }

  /**
   * Returns the Color of the first empty player.
   *
   * @return Either black or white depending on status of players.
   */
  public Color getEmptyPlayerColor() {
    if (whitePlayer == null) {
      return Color.WHITE;
    } else if (blackPlayer == null) {
      return Color.BLACK;
    }
    return null;
  }

  /**
   * Returns a Powerobject and its location if one was added.
   *
   * @return map of newly spawned on-board PowerObject and its location.
   */
  public Map<PowerObject, Location> getPowerObject() {
    return this.addedPowerObject;
  }

  /**
   * Returns a list of the knight's movement if player just castled. Index 0 contains where the knight
   * was before castling and index 1 contains where the knight moved to. Will return an empty list otherwise.
   * 
   * @return List of Locations if player just castled or an empty list otherwise.
   */
  public List<Location> getCastling() {
	  return board.getCastling();
  }
  
  /**
   * Returns the location of the pawn captured through en passant if it happened last turn. Otherwise returns null.
   * 
   * @return Location of pawn or null.
   */
  public Location getEnPassant() {
	  return board.getEnPassant();
  }
  
  /**
   * Add a PowerUp to the game at the specified location.
   *
   * @param loc
   *          Location where to add.
   * @param power
   *          PowerUp to add.
   */
  public void addPowerUp(Location loc, PowerUp power) {
    powerUps.put(power, loc);
    board.addBoardObject(loc, power);
  }

  /**
   * Removes powerup from board and from list of on-board powerups held by Game.
   *
   * @param power
   *          Power to remove.
   */
  private void removePowerUp(PowerUp power) {
    Location loc = powerUps.get(power);
    board.removePowerUp(loc, power);
    powerUps.remove(power);
  }

  /**
   * Get a map of all powerups currently on board and their locations.
   *
   * @return map of on-board powerups to their location.
   */
  public Map<PowerUp, Location> getOnBoardPowers() {
    return ImmutableMap.copyOf(powerUps);
  }

  /**
   * Updates the number of turns until the next PowerObject will spawn to a
   * uniformly random number between 2 and 4 (inclusive).
   */
  private void updateTilNextPowerUp() {
    tilNextPowerup = rand.nextInt(3) + 2;
  }

  /**
   * Set number of turns until next PowerObject spawns.
   *
   * @param remainingTurns
   *          number of turns until next PowerObject will spawn.
   */
  public void setTilNextPowerUp(int remainingTurns) {
    tilNextPowerup = remainingTurns;
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
   * Spawn PowerObject at specified location.
   *
   * @param loc
   *          Location where to place PowerObject.
   * @param powerObj
   *          PowerObject to spawn.
   */
  public void spawnPowerObject(Location loc, PowerObject powerObj) {
    board.addBoardObject(loc, powerObj);
    addedPowerObject.put(powerObj, loc);
  }

  /**
   * Removes a piece at a given location (if one exists).
   *
   * @param loc
   *          Location to remove the piece from.
   */
  public void removePieceAt(Location loc) {
    Piece p = board.getPieceAt(loc);
    if (p instanceof Pawn) {
      int dir = p.getColor() == Color.WHITE ? -1 : 1;
      try {
        Location ghostLoc = new Location(loc.getRow() + dir, loc.getCol());
        if (board.getPieceAt(ghostLoc) instanceof GhostPawn) {
          board.resetGhost(p.getColor());
        }
      } catch (IllegalArgumentException e) {
        // pass
      }
    }
    board.removePieceAt(loc);
  }

  /**
   * Check whether specified move is valid.
   *
   * @param move
   *          Move to check.
   * @return true if valid, false otherwise.
   */
  public boolean validMove(Move move) {
    Location start = move.getStart();
    Piece piece = board.getPieceAt(start);
    if (piece == null) {
      return false;
    }

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
    return whiteToMove ? whitePlayer : blackPlayer;
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
   * Executes a promotion at the toPromote location.
   */
  public void executePromotion() {
    executePromotion(toPromote);
  }

  /**
   * Execute promotion.
   *
   * @param loc
   *          Location of piece to promote.
   * @throws IllegalPromotionException
   *           If player tries to promote to Pawn or King.
   */
  public void executePromotion(Location loc) {
    Piece newPiece = getActivePlayer().getPromotion();
    board.placePiece(loc, newPiece);
    gameState = GameState.WAITING_FOR_MOVE;
    whiteToMove = !whiteToMove;
  }

  /**
   * Execute an automatic promotion to queen.
   *
   * @return location of the newly promoted queen.
   */
  public Location executePromotionToQueen() {
    Piece newPiece = new Queen(getActivePlayer().getColor());
    board.placePiece(toPromote, newPiece);
    gameState = GameState.WAITING_FOR_MOVE;
    whiteToMove = !whiteToMove;
    return toPromote;
  }

  private void manageCaptured(Collection<BoardObject> captured, Location end) {

    // update powerup location with move
    PowerUp power = board.getPowerUpAt(end);
    if (power != null) {
      powerUps.put(power, end);
    }

    for (BoardObject obj : captured) {
      if (obj instanceof PowerObject) {
        gameState = GameState.WAITING_FOR_POWERUP_CHOICE;
        actionOptions = ((PowerObject) obj).getPowerActions(this, end);

      } else if (power != null && obj instanceof Piece) {
        // if piece with powerup captures, powerup should be removed
        power.setTurnsRemaining(0);

      } else if (obj instanceof King) {
        gameOver = true;
      }
    }

  }

  /**
   * Get the list of available power action options active player may choose
   * from.
   *
   * @return List of up to 2 power actions.
   */
  public List<PowerAction> getActionOptions() {
    return actionOptions;
  }

  /**
   * Check whether input is valid for active player's power action.
   *
   * @param input
   *          Input for PowerAction.
   * @return true if valid input, otherwise false.
   */
  public boolean validActionInput(Object input) {
    return getActivePlayer().validActionInput(input);
  }

  /**
   * Get the desired input format to execute active player's selected
   * PowerAction.
   *
   * @return the desired input format to execute active player's selected
   *         PowerAction.
   */
  public String getActionInputFormat() {
    return getActivePlayer().getActionInputFormat();
  }

  /**
   * Execute specified power action.
   *
   * @param input
   *          Object that is the required input to execute the PowerAction.
   */
  public void executePowerAction(Object input) {
    getActivePlayer().executeAction(input);
    actionOptions.clear();
    gameState = GameState.WAITING_FOR_MOVE;
    whiteToMove = !whiteToMove;
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
  public boolean whiteToMove() {
    return whiteToMove;
  }

  /**
   * Checks whether or not the game is over.
   *
   * @return True if the game is over and false otherwise.
   */
  public boolean getGameOverStatus() {
    return gameOver;
  }

  /**
   * Returns the current state of the game.
   *
   * @return The current state of the game.
   */
  public GameState getGameState() {
    return gameState;
  }

  /**
   * Set the current state of the game.
   *
   * @param gameState
   *          The current stat of the game to set.
   */
  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  /**
   * Gets the move history of the game.
   *
   * @return Move history of the game.
   */
  public List<Move> getHistory() {
    return history;
  }

  /**
   * @return the timeControl
   */
  public TimeControl getTimeControl() {
    return timeControl;
  }

  /**
   * @param timeControl
   *          the timeControl to set
   */
  public void setTimeControl(TimeControl timeControl) {
    this.timeControl = timeControl;
  }

  /**
   * @return the isPublic
   */
  public boolean isPublic() {
    return isPublic;
  }

  /**
   * @param pub
   *          the public to set
   */
  public void setPublic(boolean pub) {
    this.isPublic = pub;
  }

}
