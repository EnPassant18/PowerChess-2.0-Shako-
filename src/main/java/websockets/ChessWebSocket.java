package websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import board.Board.EmptySpace;
import board.BoardObject;
import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.Game;
import game.Game.GameState;
import game.Game.TimeControl;
import game.Move;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import players.GuiPlayer;
import players.Player;
import poweractions.Armageddon;
import poweractions.PieceMover;
import poweractions.PowerAction;
import poweractions.Rewind;
import powerups.BlackHole;
import powerups.Invulnerability;
import powerups.PowerObject;
import powerups.PowerUp;

/**
 * WebSocket allows server to communicate with client during games.
 *
 * @author dwoods, knormand
 *
 */
@WebSocket
public class ChessWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> SESSIONS = new ConcurrentLinkedQueue<>();

  private static int nextGameId = 0;
  private static int nextPlayerId = 0;

  private static final Map<Integer, Game> GAME_ID_MAP = new HashMap<>();
  private static final Multimap<Integer, Player> GAME_PLAYER_MAP =
      HashMultimap.create();
  private static final Map<Integer, Session> PLAYER_SESSION_MAP =
      new HashMap<>();
  private static final Map<Integer, Boolean> PLAYER_DRAW_MAP = new HashMap<>();

  /**
   * Enumerates allowable websocket message types.
   *
   * @author dwoods
   *
   */
  private enum MessageType {
    CREATE_GAME, JOIN_GAME, GAME_OVER, REQUEST_DRAW, PLAYER_ACTION, GAME_UPDATE,
    ILLEGAL_ACTION, ERROR, PUBLIC_GAMES
  }

  /**
   * Enumerates player actions.
   *
   * @author dwoods
   *
   */
  private enum Action {
    NONE, MOVE, SELECT_POWER
  }

  /**
   * Enumerates reasons a game can end.
   *
   * @author dwoods
   *
   */
  private enum GameEndReason {
    MATE, RESIGNATION, TIME, DRAW_AGREED
  }

  /**
   * Enumerates game end results.
   *
   * @author dwoods
   *
   */
  private enum GameResult {
    WIN, LOSS, DRAW
  }

  /**
   * Enumerates types of entities that can be interacted with (on board and
   * off).
   *
   * @author dwoods
   *
   */
  private enum EntityTypes {
    NOTHING, PIECE, POWER, OTHER
  }

  /**
   * Enumerates the types of chess pieces.
   *
   * @author dwoods
   *
   */
  private enum PieceIds {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
  }

  /**
   * Enumerates all other entities that could appear on the board.
   *
   * @author knorms
   *
   */
  private enum OtherEntities {
    BLACK_HOLE
  }

  /**
   * On connect, add session to the queue.
   *
   * @param session
   *          Session that just connected.
   * @throws IOException
   *           If session cannot be added to the queue.
   */
  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    SESSIONS.add(session);
  }

  /**
   * Remove session from the queue.
   *
   * @param session
   *          Session to be removed.
   * @param statusCode
   *          Code indicating why session was closed.
   * @param reason
   *          Reason session was closed.
   */
  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    SESSIONS.remove(session);
  }

  /**
   * Respond to recieved messages by parsing and responding with appropriate
   * info/updates.
   *
   * @param session
   *          Session that sent message.
   * @param message
   *          Message recieved.
   * @throws IOException
   *           In case the response JsonObject doesn't get sent properly.
   */
  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);

    int typeIndex = received.get("type").getAsInt();
    MessageType messageType = MessageType.values()[typeIndex];

    switch (messageType) {
      case CREATE_GAME:
        createGame(session, received);
        break;

      case JOIN_GAME:
        addPlayer(session, received);
        break;

      case REQUEST_DRAW:
        requestDraw(session, received);
        break;

      case PLAYER_ACTION:
        int actionIndex = received.get("action").getAsInt();
        Action action = Action.values()[actionIndex];

        switch (action) {
          case MOVE:
            makeMove(session, received);
            break;
          case SELECT_POWER:
            powerSelect(session, received);
            break;
          case NONE:
          default:
            break;
        }
        break;

      case GAME_OVER:
        gameOver(session, received);
        break;

      default:
        break;

    }

  }

  /**
   * Called when server receives a GAME_OVER message. Server will inform both
   * players that the game is over and why (e.g. time, forfeit).
   *
   * @param session
   *          The session of the player requesting a draw.
   * @param received
   *          The JsonObject sent by session.
   * @throws IOException
   *           In case the response message couldn't be sent back properly.
   */
  private void gameOver(Session session, JsonObject received)
      throws IOException {
    int playerId = received.get("playerId").getAsInt();
    int gameId = received.get("gameId").getAsInt();

    int reason = received.get("reason").getAsInt();

    int otherId = getOtherId(gameId, playerId);

    // If other player id exists, then update them too
    if (otherId != -1) {
      Session otherSession = PLAYER_SESSION_MAP.get(otherId);
      JsonObject response =
          createGameOverUpdate(GameEndReason.values()[reason], GameResult.WIN,
              gameId);
      otherSession.getRemote().sendString(GSON.toJson(response));
    }

    GAME_PLAYER_MAP.removeAll(gameId);
    GAME_ID_MAP.remove(gameId);

  }

  /**
   * Called when server receives a REQUEST_DRAW message. If the other player is
   * awaiting draw response, game will end in draw. Otherwise, server will ask
   * the other player if they'd like to draw as well.
   *
   * @param session
   *          The session of the player requesting a draw.
   * @param received
   *          The JsonObject sent by session.
   * @throws IOException
   *           In case the response message couldn't be sent back properly.
   */
  private void requestDraw(Session session, JsonObject received)
      throws IOException {
    int playerId = received.get("playerId").getAsInt();
    int gameId = received.get("gameId").getAsInt();
    int otherId = getOtherId(gameId, playerId);

    // If other player id does not exist. Return an illegal action message.
    if (otherId == -1) {
      sendIllegalAction(session);
      return;
    }

    PLAYER_DRAW_MAP.replace(playerId, true);
    boolean otherDraw = PLAYER_DRAW_MAP.get(otherId);
    Session otherSession = PLAYER_SESSION_MAP.get(otherId);
    // If the other player is awaiting a draw message, end game. Otherwise a
    if (otherDraw) {
      JsonObject response =
          createGameOverUpdate(GameEndReason.DRAW_AGREED, GameResult.DRAW,
              gameId);

      session.getRemote().sendString(GSON.toJson(response));
      otherSession.getRemote().sendString(GSON.toJson(response));
    } else {
      JsonObject response = new JsonObject();
      response.addProperty("type", MessageType.REQUEST_DRAW.ordinal());
      otherSession.getRemote().sendString(GSON.toJson(response));
    }
  }

  /**
   * Called when server receives a POWER_SELECTION action message. Server
   * attempts to execute power action; returns ILLEGAL_ACTION if follow-up was
   * illegal, or GAME_UPDATE to both players if successfully executed.
   *
   * @param session
   *          The session of the player requesting a draw.
   * @param received
   *          The JsonObject sent by session.
   * @throws IOException
   *           In case the response message couldn't be sent back properly.
   */
  private void powerSelect(Session session, JsonObject received)
      throws IOException {
    boolean selection = received.get("selection").getAsBoolean();
    int gameId = received.get("gameId").getAsInt();
    int playerId = received.get("playerId").getAsInt();
    Game game = GAME_ID_MAP.get(gameId);

    List<PowerAction> actionOptions = game.getActionOptions();
    int index = selection ? 0 : 1;
    if (actionOptions.size() == 0) {
      sendError(session);
      return;
    }
    if (actionOptions.size() < index - 1) {
      index--;
    }
    PowerAction selected = actionOptions.get(index);
    game.getActivePlayer().setAction(selected);
    Location whereCaptured = selected.getWhereCaptured();

    Collection<BoardObject> preWhereCaptured = game.getObjsAt(whereCaptured);
    Collection<BoardObject> preLoc = null, preStart = null, preEnd = null;

    game.setGameState(GameState.WAITING_FOR_POWERUP_EXEC);
    if (!received.has("followUp") && selected.inputFormat() != null) {
      sendError(session);
      return;
    }

    Object input = null;
    Move move = null;

    if (received.has("followUp")) {
      JsonObject followUp = received.get("followUp").getAsJsonObject();
      if (followUp.has("row")) {
        int row = followUp.get("row").getAsInt();
        int col = followUp.get("col").getAsInt();
        input = new Location(row, col);
        preLoc = game.getObjsAt((Location) input);
      } else if (followUp.has("from")) {
        move = getMove(followUp);
        input = move.getEnd();
        preStart = game.getObjsAt(move.getStart());
        preEnd = game.getObjsAt(move.getEnd());
      }
    }

    // if input not valid, return ILLEGAL_ACTION
    if (!selected.validInput(input)) {
      sendIllegalAction(session);
      return;
    }

    // otherwise execute action and compile GAME_UPDATE
    game.executePowerAction(input);
    game.setGameState(GameState.WAITING_FOR_MOVE);

    JsonArray updates = new JsonArray();
    // Check where captured
    updates.addAll(getDifference(whereCaptured, preWhereCaptured, game));

    // Check specified followUp location
    if (preLoc != null) {
      Location loc = (Location) input;
      updates.addAll(getDifference(loc, preLoc, game));
    }

    // Check followUp move start and end
    if (preStart != null && !move.getStart().equals(whereCaptured)) {
      Location startLoc = move.getStart();
      updates.addAll(getDifference(startLoc, preStart, game));
    }
    if (preEnd != null) {
      Location endLoc = move.getEnd();
      updates.addAll(getDifference(endLoc, preEnd, game));
    }

    // If Armageddon, find all former pawn locations and curret king locations
    if (selected instanceof Armageddon) {
      // update invulnerability at king locs
      for (Location loc : ((Armageddon) selected).getKingLocations()) {
        Piece p = game.getPieceAt(loc);
        updates.add(createInvulnerableUpdate(loc, p));
      }

      // update pawn locs have nothing
      for (Location loc : ((Armageddon) selected).getPawnLocations()) {
        updates.add(createEmptyUpdate(loc));
      }

      // If Rewind, check game history
    } else if (selected instanceof Rewind) {
      List<Move> history = game.getHistory();
      move = history.get(history.size() - 2);
      Location start = move.getStart();
      Location end = move.getEnd();
      // if rewind was actually executed, end will be empty
      if (game.isEmpty(end)) {
        // update that former end loc is empty
        updates.add(createEmptyUpdate(end));

        // update that piece is back at former start loc
        Piece p = game.getPieceAt(start);
        updates.add(createPieceUpdate(start, p));
      }

      // If SendAway, check where sent
    } else if (selected instanceof PieceMover) {
      Location loc = ((PieceMover) selected).getEndLocation();
      Piece p = game.getPieceAt(loc);
      if (loc != null && p != null) {
        // update that endloc now has a piece
        updates.add(createPieceUpdate(loc, p));
      }
    }

    JsonObject response = new JsonObject();
    response.addProperty("type", MessageType.GAME_UPDATE.ordinal());
    response.add("updates", updates);

    if (game.getActionOptions().isEmpty()) {
      response.addProperty("action", Action.NONE.ordinal());
    } else {
      response.addProperty("action", Action.SELECT_POWER.ordinal());
    }

    int otherId = getOtherId(gameId, playerId);

    // update active player
    session.getRemote().sendString(GSON.toJson(response));

    // If other player id exists, then update them too
    if (otherId != -1) {
      Session otherSession = PLAYER_SESSION_MAP.get(otherId);
      response.remove("action");
      if (game.getActionOptions().isEmpty()) {
        response.addProperty("action", Action.MOVE.ordinal());
      } else {
        response.addProperty("action", Action.NONE.ordinal());
      }
      otherSession.getRemote().sendString(GSON.toJson(response));
    }
  }

  /**
   * Called when server receives a MOVE action message. Server attempts to make
   * desired move and either replies replies with ILLEGAL_MOVE or updates each
   * player as to the changed board state.
   *
   * @param session
   *          The session of the player requesting a draw.
   * @param received
   *          The JsonObject sent by session.
   * @throws IOException
   *           In case the response message couldn't be sent back properly.
   */
  private void makeMove(Session session, JsonObject received)
      throws IOException {
    JsonObject moveJson = received.get("move").getAsJsonObject();
    Move move = getMove(moveJson);

    int gameId = received.get("gameId").getAsInt();
    int playerId = received.get("playerId").getAsInt();
    Game game = GAME_ID_MAP.get(gameId);
    Player player = game.getActivePlayer();
    player.setMove(move);

    try {
      game.turn();
    } catch (IllegalMoveException e) {
      // If illegal move, send back an illegal action to session owner
      sendIllegalAction(session);
      return;
    }

    PLAYER_DRAW_MAP.replace(playerId, false);
    Map<PowerUp, Location> powers = game.getRemoved();

    // updates is a list of all changes in the board state after the turn gets
    // executed (not counting the move itself).
    JsonArray updates = new JsonArray();

    List<Location> castling = game.getCastling();
    // If the player just castled, add that to updates.
    if (castling.size() == 2) {
      Location loc1 = castling.get(0);
      updates.add(createEmptyUpdate(loc1));

      Location loc2 = castling.get(1);
      Piece p = game.getPieceAt(loc2);
      updates.add(createPieceUpdate(loc2, p));
    }

    Location enPassant = game.getEnPassant();
    if (enPassant != null) {
      updates.add(createEmptyUpdate(enPassant));
    }
    // If there are any power ups to update (any that ran out after executing
    // this turn)
    for (PowerUp power : powers.keySet()) {
      Location loc = powers.get(power);

      // If the powerup was a blackhole
      if (power instanceof BlackHole) {
        updates.add(createEmptyUpdate(loc));

        // If its invulnerability
      } else if (power instanceof Invulnerability) {
        Piece p = game.getPieceAt(loc);
        updates.add(createPieceUpdate(loc, p));
      }

    }

    Map<PowerObject, Location> addedPowerUp = game.getPowerObject();
    // If a power up was spawned, add the power up and its location to update
    for (PowerObject power : addedPowerUp.keySet()) {
      Location loc = addedPowerUp.get(power);
      updates.add(createPowerObjectUpdate(loc, power));
    }

    /*
     * If the game is waiting for a promotion, auto-promote the piece to queen
     * and add that change to updates.
     */
    if (game.getGameState() == GameState.WAITING_FOR_PROMOTE) {
      Location loc = game.executePromotionToQueen();
      Piece p = game.getPieceAt(loc);
      updates.add(createPieceUpdate(loc, p));
    }

    JsonObject response = new JsonObject();
    response.addProperty("type", MessageType.GAME_UPDATE.ordinal());
    response.add("updates", updates);

    // send valid message back
    JsonObject otherResponse = new JsonObject();
    otherResponse.addProperty("type", MessageType.GAME_UPDATE.ordinal());
    otherResponse.add("move", moveJson);
    otherResponse.add("updates", updates);
    List<PowerAction> actions = game.getActionOptions();

    // If the move captured a power up, request a power up selection;
    if (!actions.isEmpty()) {
      response.addProperty("action", Action.SELECT_POWER.ordinal());
      otherResponse.addProperty("action", Action.NONE.ordinal());
      PowerAction action1 = actions.get(0);
      response.addProperty("rarity", action1.getRarity().ordinal());
      response.addProperty("id1", action1.getId());
      PowerAction action2 = actions.get(1);
      response.addProperty("id2", action2.getId());

      // Otherwise, tell the session owner that their turn is over and tell the
      // other player
      // that their turn has started
    } else {
      response.addProperty("action", Action.NONE.ordinal());
      otherResponse.addProperty("action", Action.MOVE.ordinal());
    }
    session.getRemote().sendString(GSON.toJson(response));
    Collection<Player> playerList = GAME_PLAYER_MAP.get(gameId);
    for (Player p : playerList) {
      if (p.getId() != playerId) {
        Session otherSession = PLAYER_SESSION_MAP.get(p.getId());
        otherSession.getRemote().sendString(GSON.toJson(otherResponse));
        return;
      }
    }
  }

  /**
   * Returns the integer value id of the given piece. (NOTE: this is different
   * than the rank of the piece used in game).
   *
   * @param p
   *          The piece to get the id of.
   * @return The integer id of the piece.
   */
  private int getPieceValue(Piece p) {
    if (p instanceof King) {
      return PieceIds.KING.ordinal();
    } else if (p instanceof Queen) {
      return PieceIds.QUEEN.ordinal();
    } else if (p instanceof Bishop) {
      return PieceIds.BISHOP.ordinal();
    } else if (p instanceof Rook) {
      return PieceIds.ROOK.ordinal();
    } else if (p instanceof Knight) {
      return PieceIds.KNIGHT.ordinal();
    } else {
      return PieceIds.PAWN.ordinal();
    }
  }

  /**
   * Reads a move from the given JsonObject and returns the move.
   *
   * @param moveJson
   *          JsonObject containing the a "from" and "to" JsonObject, each with
   *          their own "row" and "col" ints.
   * @return A move, starting from from.row and from.col and ending at to.row
   *         and to.col.
   */
  private Move getMove(JsonObject moveJson) {
    JsonObject from = moveJson.get("from").getAsJsonObject();
    JsonObject to = moveJson.get("to").getAsJsonObject();
    int fromRow = from.get("row").getAsInt();
    int fromCol = from.get("col").getAsInt();
    int toRow = to.get("row").getAsInt();
    int toCol = to.get("col").getAsInt();
    Location startLocation = new Location(fromRow, fromCol);
    Location endLocation = new Location(toRow, toCol);
    Move move = new Move(startLocation, endLocation);
    return move;
  }

  /**
   * Creates a game, adding in a player with the given color. In addition,
   * stores (gameId -> game), (gameId -> playerId), (playerId -> session),
   * (playerId -> name), and (playerId -> false) in the corresponding maps.
   *
   * @param session
   *          The session of the client creating the game,
   * @param received
   *          The JsonObject sent by session.
   * @throws IOException
   *           In case the response JsonObject doesn't get sent properly.
   */
  private void createGame(Session session, JsonObject received)
      throws IOException {
    Game game = new Game();
    int gameId = nextGameId;
    nextGameId++;
    GAME_ID_MAP.put(gameId, game);

    boolean isPublic = received.get("public").getAsBoolean();
    game.setPublic(isPublic);

    int timeControlIndex = received.get("timeControl").getAsInt();
    game.setTimeControl(TimeControl.values()[timeControlIndex]);

    boolean colorBool = received.get("color").getAsBoolean();
    Color playerColor = Color.BLACK;
    if (colorBool) {
      playerColor = Color.WHITE;
    }
    String name = received.get("name").getAsString();
    int playerId = nextPlayerId;
    GuiPlayer player = new GuiPlayer(playerColor, playerId, name);

    nextPlayerId++;
    PLAYER_SESSION_MAP.put(playerId, session);

    PLAYER_DRAW_MAP.put(playerId, false);

    List<Integer> playerList = new ArrayList<>();
    playerList.add(playerId);
    GAME_PLAYER_MAP.put(gameId, player);

    game.addPlayer(player);

    JsonObject response = new JsonObject();
    response.addProperty("type", MessageType.CREATE_GAME.ordinal());
    response.addProperty("gameId", gameId);
    response.addProperty("playerId", playerId);

    session.getRemote().sendString(GSON.toJson(response));

    HomeWebSocket.gameAdded(createGameUpdate(gameId));
  }

  /**
   * Adds a player to a currently existing game. Updates gamePlayerMap (gameId
   * -> playerId1, playerId2), adds (playerId -> session), (playerid -> name),
   * and (playerId -> false) to their respective maps as well.
   *
   * @param session
   *          Session of the client trying to join the game.
   * @param received
   *          JsonObject send by session.
   * @throws IOException
   *           If the response JsonObject fails to send properly.
   */
  private void addPlayer(Session session, JsonObject received)
      throws IOException {
    int playerId = nextPlayerId;
    nextPlayerId++;
    int gameId = received.get("gameId").getAsInt();
    Color playerColor;
    try {
      Game game = GAME_ID_MAP.get(gameId);
      playerColor = game.getEmptyPlayerColor();

      // If there is no available player color, then this game cannot accept any
      // more players.
      if (playerColor == null) {
        sendError(session);
        return;
      }
      String name = received.get("name").getAsString();
      GuiPlayer player = new GuiPlayer(playerColor, playerId, name);

      String existingPlayerName;
      Collection<Player> playerCollection = GAME_PLAYER_MAP.get(gameId);
      List<Player> playerList = new ArrayList<>(playerCollection);
      // If the list size is 1, then the player can be added to the game
      // normally.
      // Otherwise, there was an error of some sort.
      if (playerList.size() == 1) {
        Player existingPlayer = playerList.get(0);
        GAME_PLAYER_MAP.put(gameId, player);

        JsonObject responseToOther = new JsonObject();
        responseToOther.addProperty("type", MessageType.JOIN_GAME.ordinal());
        responseToOther.addProperty("name", name);
        Session otherSession = PLAYER_SESSION_MAP.get(existingPlayer.getId());
        existingPlayerName = existingPlayer.getName();
        otherSession.getRemote().sendString(GSON.toJson(responseToOther));
      } else {
        sendIllegalAction(session);
        return;
      }

      PLAYER_SESSION_MAP.put(playerId, session);
      PLAYER_DRAW_MAP.put(playerId, false);

      game.addPlayer(player);

      JsonObject response = new JsonObject();
      response.addProperty("type", MessageType.JOIN_GAME.ordinal());
      response.addProperty("playerId", playerId);
      boolean colorBool = true;
      if (playerColor == Color.BLACK) {
        colorBool = false;
      }
      response.addProperty("color", colorBool);
      response.addProperty("name", existingPlayerName);
      response.addProperty("timeControl", game.getTimeControl().ordinal());
      session.getRemote().sendString(GSON.toJson(response));

    } catch (NullPointerException e) {
      sendError(session);
      return;
    }
  }

  /**
   * Send ERROR message to session owner if an error occured.
   *
   * @param session
   *          Session that tried to perform an illegal action.
   * @throws IOException
   *           If the response JsonObject fails to send properly.
   */
  private void sendError(Session session) throws IOException {
    JsonObject response = new JsonObject();
    response.addProperty("type", MessageType.ERROR.ordinal());
    session.getRemote().sendString(GSON.toJson(response));
  }

  /**
   * Send ILLEGAL_ACTION message to session owner.
   *
   * @param session
   *          Session that tried to perform an illegal action.
   * @throws IOException
   *           If the response JsonObject fails to send properly.
   */
  private void sendIllegalAction(Session session) throws IOException {
    JsonObject response = new JsonObject();
    response.addProperty("type", MessageType.ILLEGAL_ACTION.ordinal());
    session.getRemote().sendString(GSON.toJson(response));
  }

  /**
   * Retreive ID of other player in game.
   *
   * @param gameId
   *          Game ID.
   * @param playerId
   *          ID of first player.
   * @return ID of other player or -1 if no other player in game.
   */
  private int getOtherId(int gameId, int playerId) {
    int otherId = -1;
    try {
      Collection<Player> playerList = GAME_PLAYER_MAP.get(gameId);
      for (Player player : playerList) {
        if (player.getId() != playerId) {
          return player.getId();
        }
      }
    } catch (NullPointerException e) {
      // pass
    }
    return otherId;
  }

  /**
   * Get the pre-post difference between objects on a board location and return
   * a JsonArray representing all the changes.
   *
   * @param loc
   *          Location to check differences at.
   * @param preObjs
   *          Objects on space before power action execution.
   * @param game
   *          Game that was modified by power action.
   * @return JsonArray representing all changes at specified location.
   */
  private JsonArray getDifference(Location loc, Collection<BoardObject> preObjs,
      Game game) {
    JsonArray updates = new JsonArray();

    Collection<BoardObject> postObjs = game.getObjsAt(loc);
    postObjs.removeIf(obj -> obj instanceof EmptySpace);
    preObjs.removeIf(obj -> obj instanceof EmptySpace);

    // if there was something and now there's nothing
    if (postObjs.isEmpty() && !preObjs.isEmpty()) {
      updates.add(createEmptyUpdate(loc));
    }

    postObjs.removeAll(preObjs);

    // if there are new objects on location, add difference updates
    for (BoardObject obj : postObjs) {
      JsonObject updatePart = new JsonObject();

      // if added piece to loc
      if (obj instanceof Piece) {
        Piece p = ((Piece) obj);
        updatePart = createPieceUpdate(loc, p);

        // if added blackhole to loc
      } else if (obj instanceof BlackHole) {
        updatePart = createBlackHoleUpdate(loc);

        // if added invulnerability to loc
      } else if (obj instanceof Invulnerability) {
        Piece p = game.getPieceAt(loc);
        updatePart = createInvulnerableUpdate(loc, p);
      }

      updates.add(updatePart);
    }

    return updates;
  }

  /**
   * Create a JsonObject representing a PieceUpdate at specified location to be
   * placed in a GAME_UPDATE message.
   *
   * @param loc
   *          Location that changed.
   * @param p
   *          Piece that was added.
   * @return JsonObject with PieceUpdate.
   */
  private JsonObject createPieceUpdate(Location loc, Piece p) {
    JsonObject updatePart = new JsonObject();
    updatePart.addProperty("row", loc.getRow());
    updatePart.addProperty("col", loc.getCol());
    updatePart.addProperty("state", EntityTypes.PIECE.ordinal());
    updatePart.addProperty("color", p.getColor() == Color.WHITE);
    updatePart.addProperty("piece", getPieceValue(p));
    return updatePart;
  }

  /**
   * Create a JsonObject representing a Invulnerability Update (it's a piece
   * update where the piece ID has 6 added to it) at specified location to be
   * placed in a GAME_UPDATE message.
   *
   * @param loc
   *          Location that changed.
   * @param p
   *          Piece with invulnerability.
   * @return JsonObject with Invulnerability Update.
   */
  private JsonObject createInvulnerableUpdate(Location loc, Piece p) {
    JsonObject updatePart = createPieceUpdate(loc, p);
    updatePart.remove("piece");
    updatePart.addProperty("piece", getPieceValue(p) + 6);
    return updatePart;
  }

  /**
   * Create a JsonObject representing a Black Hole added at specified location
   * to be placed in a GAME_UPDATE message.
   *
   * @param loc
   *          Location that changed.
   * @return JsonObject with Black Hole Update.
   */
  private JsonObject createBlackHoleUpdate(Location loc) {
    JsonObject updatePart = new JsonObject();
    updatePart.addProperty("row", loc.getRow());
    updatePart.addProperty("col", loc.getCol());
    updatePart.addProperty("state", EntityTypes.OTHER.ordinal());
    updatePart.addProperty("other", OtherEntities.BLACK_HOLE.ordinal());
    return updatePart;
  }

  /**
   * Create a JsonObject representing a Power Object spawn at specified location
   * to be placed in a GAME_UPDATE message.
   *
   * @param loc
   *          Location that changed.
   * @param power
   *          PowerObject that spawned.
   * @return JsonObject with on-board Power Object.
   */
  public JsonObject createPowerObjectUpdate(Location loc, PowerObject power) {
    JsonObject updatePart = new JsonObject();
    updatePart.addProperty("row", loc.getRow());
    updatePart.addProperty("col", loc.getCol());
    updatePart.addProperty("state", EntityTypes.POWER.ordinal());
    updatePart.addProperty("rarity", power.getRarity().ordinal());
    return updatePart;
  }

  /**
   * Create a JsonObject representing an empty-space update (i.e. used to have
   * some object that has been removed) at specified location to be placed in a
   * GAME_UPDATE message.
   *
   * @param loc
   *          Location that changed.
   * @return JsonObject empty-space update.
   */
  private JsonObject createEmptyUpdate(Location loc) {
    JsonObject updatePart = new JsonObject();
    updatePart.addProperty("row", loc.getRow());
    updatePart.addProperty("col", loc.getCol());
    updatePart.addProperty("state", EntityTypes.NOTHING.ordinal());
    return updatePart;
  }

  /**
   * Create a JsonObject representing an Game Over Update.
   *
   * @param reason
   *          Reason the game ended (e.g. draw, resign).
   * @param result
   *          Result of game ending (win, lose, draw).
   * @return JsonObject with Game Over Update.
   */
  private JsonObject createGameOverUpdate(GameEndReason reason,
      GameResult result, int gameId) {
    JsonObject response = new JsonObject();
    response.addProperty("type", MessageType.GAME_OVER.ordinal());
    response.addProperty("reason", reason.ordinal());
    response.addProperty("result", result.ordinal());
    HomeWebSocket.gameRemoved(createGameUpdate(gameId));
    return response;
  }

  /**
   * Create a JsonArray of all public games including gameId, timeControl, and
   * opponent Name and Color.
   *
   * @return return JsonArray with all public games and their details.
   * @throws IOException
   *           In case the response JsonObject doesn't get sent properly.
   */
  public static JsonObject getPublicGames() throws IOException {
    JsonObject response = new JsonObject();
    Game g;
    for (int gameId : GAME_ID_MAP.keySet()) {
      g = GAME_ID_MAP.get(gameId);
      if (g.isPublic()) {
        response.add(String.valueOf(gameId), createGameUpdate(gameId));
      }
    }
    return response;
  }

  /**
   * Create a JsonObject representing a Game Update (includes timecontrol, name
   * and color of existing opponent).
   *
   * @param gameId
   *          Game Id to create update of.
   * @return JsonObject representing Game Update.
   */
  public static JsonObject createGameUpdate(int gameId) {
    Game game = GAME_ID_MAP.get(gameId);
    JsonObject gameUpdate = new JsonObject();
    gameUpdate.addProperty("timeControl", game.getTimeControl().ordinal());

    Collection<Player> playerCollection = GAME_PLAYER_MAP.get(gameId);
    for (Player player : playerCollection) {
      gameUpdate.addProperty("name", player.getName());
      gameUpdate.addProperty("color", player.getColor().ordinal());
    }

    return gameUpdate;
  }

}
