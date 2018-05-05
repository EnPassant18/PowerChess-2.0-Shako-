package websockets;

import java.io.IOException;
import java.util.ArrayList;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import board.IllegalMoveException;
import board.Location;
import game.Color;
import game.Game;
import game.Game.GameState;
import game.Move;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import players.GuiPlayer;
import players.Player;
import poweractions.PowerAction;
import powerups.BlackHole;
import powerups.Invulnerability;
import powerups.PowerObject;
import powerups.PowerUp;

@WebSocket
public class ChessWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;

  private static int nextGameId = 0;
  private static int nextPlayerId = 0;

  private static final Map<Integer, Game> gameIdMap = new HashMap<>();
  private static final Map<Integer, List<Integer>> gamePlayerMap =
      new HashMap<>();
  private static final Map<Integer, Session> playerSessionMap = new HashMap<>();
  private static final Map<Integer, String> playerNameMap = new HashMap<>();
  private static final Map<Integer, Boolean> playerDrawMap = new HashMap<>();

  private static enum MESSAGE_TYPE {
    CREATE_GAME, JOIN_GAME, GAME_OVER, REQUEST_DRAW, PLAYER_ACTION,
    GAME_UPDATE, ILLEGAL_ACTION, ERROR
  }

  private static enum ACTION {
    NONE, MOVE, SELECT_POWER, SELECT_SQUARE, SELECT_PIECE, MOVE_THIS
  }

  private static enum GAME_END_REASON {
    MATE, RESIGNATION, TIME, DRAW_AGREED
  }

  private static enum GAME_RESULT {
    WIN, LOSS, DRAW
  }

  private static enum ENTITY_TYPES {
    NOTHING, PIECE, POWER, OTHER
  }

  private static enum PIECE_IDS {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
  }

  private static enum POWER_RARITIES {
    COMMON, RARE, LEGENDARY
  }

  private static enum COMMON_POWERS {
    ADJUST, REWIND, SECOND_EFFORT, SHIELD, SWAP
  }

  private static enum RARE_POWERS {
    BLACK_HOLE, ENERGIZE, EYE_FOR_AN_EYE, SAFETY_NET, SEND_AWAY
  }

  private static enum LEGENDARY_POWERS {
    ARMAGEDDON, AWAKEN, CLONE, REANIMATE
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException, InterruptedException {
	  JsonObject received = GSON.fromJson(message, JsonObject.class);
	  int type = received.get("type").getAsInt();
	  if(type == MESSAGE_TYPE.CREATE_GAME.ordinal()) {
		  createGame(session, received);
	  }
	  else if(type == MESSAGE_TYPE.JOIN_GAME.ordinal()) {
		  addPlayer(session, received);
	  }
	  else if(type == MESSAGE_TYPE.REQUEST_DRAW.ordinal()) {
		  requestDraw(session, received);
	  }
	  else if(type == MESSAGE_TYPE.PLAYER_ACTION.ordinal()) {
		  int action = received.get("action").getAsInt();
		  if(action == ACTION.MOVE.ordinal()) {
			  makeMove(session, received);
		  }
		  else if(action == ACTION.SELECT_POWER.ordinal()) {
			  powerSelect(session, received);
		  }
	  }
	  else if(type == MESSAGE_TYPE.GAME_OVER.ordinal()) {
		  gameOver(session, received);
	  }
	  else if(type == MESSAGE_TYPE.ERROR.ordinal()) {
		  
	  }
	  else {
		  
	  }
  }
	
  //TODO: this should be pretty simple. It's called either when time runs out or a game is forfeited.
  //Simply inform both players that the game is over and the reason that it's over (which is explained in
  //Daniel's GUI documentation.
  private void gameOver(Session session, JsonObject received) {
	  
  }
  
  /**
   * Called when server receives a REQUEST_DRAW message. If the other player is awaiting draw response,
   * game will end in draw. Otherwise, server will ask the other player if they'd like to draw as well.
   * 
   * @param session
   * 	The session of the player requesting a draw.
   * @param received
   * 	The JsonObject sent by session.
   * @throws IOException
   * 	In case the response message couldn't be sent back properly.
   */
  private void requestDraw(Session session, JsonObject received) throws IOException {
	  int playerId = received.get("playerId").getAsInt();
	  int gameId = received.get("gameId").getAsInt();
	  List<Integer> playerList = gamePlayerMap.get(gameId);
	  int otherId = -1;
	  //Retrieves the id of the other player in the game.
	  for(int id : playerList) {
		  if(id != playerId) {
			  otherId = id;
			  break;
		  }
	  }
	  //If other player id does not exist. Return an illegal action message.
	  if(otherId == -1) {
		  JsonObject response = new JsonObject();
		  response.addProperty("type", MESSAGE_TYPE.ILLEGAL_ACTION.ordinal());
		  session.getRemote().sendString(GSON.toJson(response));
		  return;
	  }
	  
	  boolean otherDraw = playerDrawMap.get(otherId);
	  Session otherSession = playerSessionMap.get(otherId);
	  //If the other player is awaiting a draw message, end game. Otherwise a
	  if(otherDraw) {
		  JsonObject response = new JsonObject();
		  response.addProperty("type", MESSAGE_TYPE.GAME_OVER.ordinal());
		  response.addProperty("reason", GAME_END_REASON.DRAW_AGREED.ordinal());
		  response.addProperty("result", GAME_RESULT.DRAW.ordinal());
		  
		  session.getRemote().sendString(GSON.toJson(response));
		  otherSession.getRemote().sendString(GSON.toJson(response));
	  }
	  else {
		  JsonObject response = new JsonObject();
		  response.addProperty("type", MESSAGE_TYPE.REQUEST_DRAW.ordinal());
		  otherSession.getRemote().sendString(GSON.toJson(response));
	  }
  }
  
	
  //TODO: this method is incomplete. This is called when the session owner made a power up choice. 
  //Need to update the game board based off that selection (probably similar to how its handled
  //in the REPL.
  private void powerSelect(Session session, JsonObject received) throws IOException {
	  boolean selection = received.get("selection").getAsBoolean();
	  int gameId = received.get("gameId").getAsInt();
	  int playerId = received.get("playerId").getAsInt();
	  Game game = gameIdMap.get(gameId);
	  
	  List<PowerAction> actionOptions = game.getActionOptions();
	    PowerAction selected;
	    try {
	      int index = 1;
	      if(selection) {
	    	  index = 0;
	      }
	      selected = actionOptions.get(index - 1);
	      game.getActivePlayer().setAction(selected);
	    } catch (NumberFormatException | IndexOutOfBoundsException e) {
	    	JsonObject response = new JsonObject();
			response.addProperty("type", MESSAGE_TYPE.ILLEGAL_ACTION.ordinal());
			session.getRemote().sendString(GSON.toJson(response));
			return;
	    }

	    game.setGameState(GameState.WAITING_FOR_POWERUP_EXEC);
	    if (game.getActionInputFormat() == null) {
	      game.executePowerAction(null);
	      game.setGameState(GameState.WAITING_FOR_MOVE);
	    }
  }
  
  //TODO: make a proper header for this as github doesn't format well.
  //This function takes in the session and received Json and attempts to make a move based off the
  //the contents of the Json.
  private void makeMove(Session session, JsonObject received) throws IOException {
	  JsonObject moveJson = received.get("move").getAsJsonObject();
	  Move move = getMove(moveJson);
	  
	  int gameId = received.get("gameId").getAsInt();
	  int playerId = received.get("playerId").getAsInt();
	  Game game = gameIdMap.get(gameId);
	  Player player = game.getActivePlayer();
	  player.setMove(move);
	  
	  try {
		  game.turn();
	  } catch (IllegalMoveException e) {
		  //If illegal move, send back an illegal action message to the session owner.
		  JsonObject response = new JsonObject();
		  response.addProperty("type", MESSAGE_TYPE.ILLEGAL_ACTION.ordinal());
		  session.getRemote().sendString(GSON.toJson(response));
		  return;
	  }
	  playerDrawMap.replace(playerId, false);
	  Map<PowerUp, Location> powers = game.getRemoved();
	  //updates is a list of all changes in the board state after the turn gets executed (not counting the move itself).
	  JsonArray updates = new JsonArray();
	  //If there are any power ups to update (any that ran out after executing this turn)
	  for(PowerUp power : powers.keySet()) {
		  JsonObject updatePart = new JsonObject();
		  Location loc = powers.get(power);
		  updatePart.addProperty("row", loc.getRow());
		  updatePart.addProperty("col", loc.getCol());
		  //If the powerup is a blackhole
		  if(power instanceof BlackHole) {
			  updatePart.addProperty("state", ENTITY_TYPES.NOTHING.ordinal());
		  }
		  //If its invulnerability
		  else if(power instanceof Invulnerability) {
			  Piece p = game.getPieceAt(loc);
			  updatePart.addProperty("state", ENTITY_TYPES.PIECE.ordinal());
			  updatePart.addProperty("piece", getPieceValue(p));
			  if(p.getColor() == Color.WHITE) {
				  updatePart.addProperty("color", true);
			  }
			  else {
				  updatePart.addProperty("color", false);
			  }
		  }
		  updates.add(updatePart);
	  }
	  Map<PowerObject, Location> addedPowerUp = game.getPowerObject();
	  //If a power up was spawned, add the power up and its location to update
	  for(PowerObject power : addedPowerUp.keySet()) {
		  JsonObject updatePart = new JsonObject();
		  Location loc = addedPowerUp.get(power);
		  updatePart.addProperty("row", loc.getRow());
		  updatePart.addProperty("col", loc.getCol());
		  updatePart.addProperty("state", ENTITY_TYPES.POWER.ordinal());
		  updatePart.addProperty("rarity", power.getRarity().ordinal());
		  updates.add(updatePart);
	  }
	  //If the game is waiting for a promotion, auto-promote the piece to queen and add that
	  //change to updates.
	  if(game.getGameState() == GameState.WAITING_FOR_PROMOTE) {
		  JsonObject updatePart = new JsonObject();
		  Location loc = game.executePromotionToQueen();
		  updatePart.addProperty("row", loc.getRow());
		  updatePart.addProperty("col", loc.getCol());
		  updatePart.addProperty("state", ENTITY_TYPES.PIECE.ordinal());
		  Piece p = game.getPieceAt(loc);
		  updatePart.addProperty("piece", PIECE_IDS.QUEEN.ordinal());
		  if(p.getColor() == Color.WHITE) {
			  updatePart.addProperty("color", true);
		  }
		  else {
			  updatePart.addProperty("color", false);
		  }
		  updates.add(updatePart);
	  }
	  JsonObject response = new JsonObject();
	  response.addProperty("type", MESSAGE_TYPE.GAME_UPDATE.ordinal());
	  response.add("updates", updates);
	  //send valid message back
	  JsonObject otherResponse = new JsonObject();
	  otherResponse.addProperty("type", MESSAGE_TYPE.GAME_UPDATE.ordinal());
	  otherResponse.add("move", moveJson);
	  otherResponse.add("updates", updates);
	  List<PowerAction> actions = game.getActionOptions();
	  //If the move captured a power up, request a power up selection;
	  if(!actions.isEmpty()) {
		  response.addProperty("action", ACTION.SELECT_POWER.ordinal());
		  otherResponse.addProperty("action", ACTION.NONE.ordinal());
		  PowerAction action1 = actions.get(0);
		  response.addProperty("rarity", action1.getRarity().ordinal());
		  response.addProperty("id1", action1.getId());
		  PowerAction action2 = actions.get(1);
		  response.addProperty("id2", action2.getId());
	  }
	  //Otherwise, tell the session owner that their turn is over and tell the other player
	  //that their turn has started
	  else {
		  response.addProperty("action", ACTION.NONE.ordinal());
		  otherResponse.addProperty("action", ACTION.MOVE.ordinal());
	  }
	  session.getRemote().sendString(GSON.toJson(response));
	  List<Integer> playerList = gamePlayerMap.get(gameId);
	  for(int i = 0; i < playerList.size(); i++) {
		  if(playerList.get(i) != playerId) {
			  Session otherSession = playerSessionMap.get(playerList.get(i));
			  otherSession.getRemote().sendString(GSON.toJson(otherResponse));
			  return;
		  }
	  }
  }
  
  /**
   * Returns the integer value id of the given piece. (NOTE: this is different than the rank of the piece used in
   * game).
   * 
   * @param p
   * 	The piece to get the id of.
   * @return
   * 	The integer id of the piece.
   */
  private int getPieceValue(Piece p) {
    if (p instanceof King) {
      return PIECE_IDS.KING.ordinal();
    } else if (p instanceof Queen) {
      return PIECE_IDS.QUEEN.ordinal();
    } else if (p instanceof Bishop) {
      return PIECE_IDS.BISHOP.ordinal();
    } else if (p instanceof Rook) {
      return PIECE_IDS.ROOK.ordinal();
    } else if (p instanceof Knight) {
      return PIECE_IDS.KNIGHT.ordinal();
    } else {
      return PIECE_IDS.PAWN.ordinal();
    }
  }

  /**
   * Reads a move from the given JsonObject and returns the move.
   * 
   * @param moveJson
   * 	JsonObject containing the a "from" and "to" JsonObject, each with their own "row" and "col" ints.
   * @return
   * 	A move, starting from from.row and from.col and ending at to.row and to.col.
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
   * Creates a game, adding in a player with the given color. In addition, stores (gameId -> game), (gameId -> playerId),
   * (playerId -> session), (playerId -> name), and (playerId -> false) in the corresponding maps.
   * 
   * @param session
   * 	The session of the client creating the game,
   * @param received
   * 	The JsonObject sent by session.
   * @throws IOException
   * 	In case the response JsonObject doesn't get sent properly.
   */
  private void createGame(Session session, JsonObject received) throws IOException {
	  Game game = new Game();
	  int gameId = nextGameId;
	  nextGameId++;
	  gameIdMap.put(gameId, game);
	  
	  boolean colorBool = received.get("color").getAsBoolean();
	  Color playerColor = Color.BLACK;
	  if(colorBool) {
		  playerColor = Color.WHITE;
	  }
	  GuiPlayer player = new GuiPlayer(playerColor);
	  int playerId = nextPlayerId;
	  nextPlayerId++;
	  playerSessionMap.put(playerId, session);
	  String name = received.get("name").getAsString();
	  playerNameMap.put(playerId, name);
	  playerDrawMap.put(playerId, false);
	  
	  List<Integer> playerList = new ArrayList<>();
	  playerList.add(playerId);
	  gamePlayerMap.put(gameId, playerList);
	  
	  game.addPlayer(player);
	  
	  JsonObject response = new JsonObject();
	  response.addProperty("type", MESSAGE_TYPE.CREATE_GAME.ordinal());
	  response.addProperty("gameId", gameId);
	  response.addProperty("playerId", playerId);

	  session.getRemote().sendString(GSON.toJson(response));
  }
  
  /**
   * Adds a player to a currently existing game. Updates gamePlayerMap (gameId -> playerId1, playerId2), adds
   * (playerId -> session), (playerid -> name), and (playerId -> false) to their respective maps as well.
   * 
   * @param session
   * 	Session of the client trying to join the game.
   * @param received
   * 	JsonObject send by session.
   * @throws IOException
   * 	If the response JsonObject fails to send properly.
   */
  private void addPlayer(Session session, JsonObject received) throws IOException {
	  int playerId = nextPlayerId;
	  nextPlayerId++;
	  int gameId = received.get("gameId").getAsInt();
	  Game game = gameIdMap.get(gameId);
	  Color playerColor = game.getEmptyPlayerColor();
	  //If there is no available player color, then this game cannot accept any more players.
	  if(playerColor == null) {
		  JsonObject response = new JsonObject();
		  response.addProperty("type", MESSAGE_TYPE.ILLEGAL_ACTION.ordinal());
		  session.getRemote().sendString(GSON.toJson(response));
		  return;
	  }
	  GuiPlayer player = new GuiPlayer(playerColor);
	  
	  String name = received.get("name").getAsString();
	  String otherName;
	  List<Integer> playerList = gamePlayerMap.get(gameId);
	  //If the list size is 1, then the player can be added to the game normally. Otherwise, there was an error of some sort.
	  if(playerList.size() == 1) {
		  playerList.add(playerId);
		  gamePlayerMap.replace(gameId, playerList);
		  
		  JsonObject responseToOther = new JsonObject();
		  responseToOther.addProperty("type", MESSAGE_TYPE.JOIN_GAME.ordinal());
		  responseToOther.addProperty("name", name);
		  Session otherSession = playerSessionMap.get(playerList.get(0));
		  otherName = playerNameMap.get(playerList.get(0));
		  otherSession.getRemote().sendString(GSON.toJson(responseToOther));
	  }
	  else {
		  JsonObject response = new JsonObject();
		  response.addProperty("type", MESSAGE_TYPE.ILLEGAL_ACTION.ordinal());
		  session.getRemote().sendString(GSON.toJson(response));
		  return;
	  }
	  
	  playerSessionMap.put(playerId, session);
	  playerNameMap.put(playerId, name);
	  playerDrawMap.put(playerId, false);
	  
	  game.addPlayer(player);
	  
	  JsonObject response = new JsonObject();
	  response.addProperty("type", MESSAGE_TYPE.JOIN_GAME.ordinal());
	  response.addProperty("playerId", playerId);
	  boolean colorBool = true;
	  if(playerColor == Color.BLACK) {
		  colorBool = false;
	  }
	  response.addProperty("color", colorBool);
	  response.addProperty("name", otherName);
	  session.getRemote().sendString(GSON.toJson(response));
  }
}
