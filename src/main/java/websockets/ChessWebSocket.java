package websockets;

import game.Color;
import game.Game;
import game.Game.GameState;
import game.Move;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import board.IllegalMoveException;
import board.Location;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import players.CliPlayer;
import players.GuiPlayer;
import players.Player;
import poweractions.Adjust;
import poweractions.PowerAction;
import poweractions.Rewind;
import poweractions.SecondEffort;
import poweractions.Shield;
import poweractions.Swap;
import powerups.BlackHole;
import powerups.Invulnerability;
import powerups.PowerObject;
import powerups.PowerUp;
import repl.ChessReplUtils;

public class ChessWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;
  
  private static int nextGameId = 0;
  private static int nextPlayerId = 0;
  
  private static final Map<Integer, Game> gameIdMap = new HashMap<>();
  private static final Map<Integer, List<Integer>> gamePlayerMap = new HashMap<>();
  private static final Map<Integer, Session> playerSessionMap = new HashMap<>();

  private static enum MESSAGE_TYPE {
    CREATE_GAME,
    ADD_PLAYER,
    GAME_OVER,
    REQUEST_DRAW,
    PLAYER_ACTION,
    GAME_UPDATE,
    ILLEGAL_ACTION,
    ERROR
  }
  
  private static enum ACTION {
	  NONE,
	  MOVE,
	  SELECT_POWER,
	  SELECT_SQUARE,
	  SELECT_PIECE,
	  MOVE_THIS
  }
  
  private static enum GAME_END_REASON {
	  MATE,
	  RESIGNATION,
	  TIME,
	  DRAW_AGREED
  }
  
  private static enum GAME_RESULT {
	  WIN,
	  LOSS,
	  DRAW
  }
  
  private static enum ENTITY_TYPES {
	  NOTHING,
	  PIECE,
	  POWER,
	  OTHER
  }
  
  private static enum PIECE_IDS {
	  KING,
	  QUEEN,
	  ROOK,
	  BISHOP,
	  KNIGHT,
	  PAWN
  }
  
  private static enum POWER_RARITIES {
	  COMMON,
	  RARE,
	  LEGENDARY
  }
  
  private static enum COMMON_POWERS {
	  ADJUST,
	  REWIND,
	  SECOND_EFFORT,
	  SHIELD,
	  SWAP
  }
  
  private static enum RARE_POWERS {
	  BLACK_HOLE,
	  ENERGIZE,
	  EYE_FOR_AN_EYE,
	  SAFETY_NET,
	  SEND_AWAY
  }
  private static enum LEGENDARY_POWERS {
	  ARMAGEDDON,
	  AWAKEN,
	  CLONE,
	  REANIMATE
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
	  else if(type == MESSAGE_TYPE.ADD_PLAYER.ordinal()) {
		  addPlayer(session, received);
	  }
	  else if(type == MESSAGE_TYPE.REQUEST_DRAW.ordinal()) {
		  
	  }
	  else if(type == MESSAGE_TYPE.PLAYER_ACTION.ordinal()) {
		  int action = received.get("action").getAsInt();
		  if(action == ACTION.MOVE.ordinal()) {
			  makeMove(session, received);
		  }
	  }
	  else if(type == MESSAGE_TYPE.ERROR.ordinal()) {
		  
	  }
	  else {
		  
	  }
  }
  
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
		  JsonObject response = new JsonObject();
		  response.addProperty("type", MESSAGE_TYPE.ILLEGAL_ACTION.ordinal());
		  session.getRemote().sendString(GSON.toJson(response));
		  return;
	  }
	  Map<PowerUp, Location> powers = game.getRemoved();
	  JsonArray updates = new JsonArray();
	  for(PowerUp power : powers.keySet()) {
		  JsonObject updatePart = new JsonObject();
		  Location loc = powers.get(power);
		  updatePart.addProperty("row", loc.getRow());
		  updatePart.addProperty("col", loc.getCol());
		  if(power instanceof BlackHole) {
			  updatePart.addProperty("state", ENTITY_TYPES.NOTHING.ordinal());
		  }
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
	  for(PowerObject power : addedPowerUp.keySet()) {
		  JsonObject updatePart = new JsonObject();
		  Location loc = addedPowerUp.get(power);
		  updatePart.addProperty("row", loc.getRow());
		  updatePart.addProperty("col", loc.getCol());
		  updatePart.addProperty("state", ENTITY_TYPES.POWER.ordinal());
		  updatePart.addProperty("rarity", power.getRarity().ordinal());
		  updates.add(updatePart);
	  }
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
	  if(!actions.isEmpty()) {
		  response.addProperty("action", ACTION.SELECT_POWER.ordinal());
		  otherResponse.addProperty("action", ACTION.NONE.ordinal());
		  PowerAction action1 = actions.get(0);
		  response.addProperty("rarity", action1.getRarity().ordinal());
		  response.addProperty("id1", action1.getId());
		  response.addProperty("followUp1", action1.getFollowUp());
		  PowerAction action2 = actions.get(1);
		  response.addProperty("id2", action2.getId());
		  response.addProperty("followUp2", action2.getFollowUp());
	  }
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
  
  private int getPieceValue(Piece p) {
	  if(p instanceof King) {
		  return PIECE_IDS.KING.ordinal();
	  }
	  else if(p instanceof Queen) {
		  return PIECE_IDS.QUEEN.ordinal();
	  }
	  else if(p instanceof Bishop) {
		  return PIECE_IDS.BISHOP.ordinal();
	  }
	  else if(p instanceof Rook) {
		  return PIECE_IDS.ROOK.ordinal();
	  }
	  else if(p instanceof Knight) {
		  return PIECE_IDS.KNIGHT.ordinal();
	  }
	  else {
		  return PIECE_IDS.PAWN.ordinal();
	  }
  }
  
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
  
  private void createGame(Session session, JsonObject received) throws IOException {
	  Game game = new Game();
	  int gameId = nextGameId;
	  nextGameId++;
	  gameIdMap.put(gameId, game);
	  
	  JsonObject response = new JsonObject();
	  response.addProperty("type", MESSAGE_TYPE.CREATE_GAME.ordinal());
	  response.addProperty("gameId", gameId);

	  session.getRemote().sendString(GSON.toJson(response));
  }
  
  private void addPlayer(Session session, JsonObject received) throws IOException {
	  boolean colorBool = received.get("playerColor").getAsBoolean();
	  Color playerColor = Color.BLACK;
	  if(colorBool) {
		  playerColor = Color.WHITE;
	  }
	  GuiPlayer player = new GuiPlayer(playerColor);
	  int gameId = received.get("gameId").getAsInt();
	  int playerId = nextPlayerId;
	  nextPlayerId++;
	  playerSessionMap.put(playerId, session);
	  
	  Game game = gameIdMap.get(gameId);
	  
	  List<Integer> playerList = gamePlayerMap.get(gameId);
	  if(playerList == null) {
		  playerList = new ArrayList<Integer>();
		  playerList.add(playerId);
		  gamePlayerMap.put(gameId, playerList);
	  }
	  else if(playerList.size() == 1) {
		  playerList.add(playerId);
		  gamePlayerMap.replace(gameId, playerList);
		  
		  String name = received.get("name").getAsString();
		  JsonObject responseToOther = new JsonObject();
		  responseToOther.addProperty("type", MESSAGE_TYPE.ADD_PLAYER.ordinal());
		  responseToOther.addProperty("name", name);
		  Session otherSession = playerSessionMap.get(playerList.get(0));
		  otherSession.getRemote().sendString(GSON.toJson(responseToOther));
	  }
	  else {
		  return;
	  }
	  
	  game.addPlayer(player);
	  
	  JsonObject response = new JsonObject();
	  response.addProperty("type", MESSAGE_TYPE.ADD_PLAYER.ordinal());
	  response.addProperty("playerId", playerId);
	  session.getRemote().sendString(GSON.toJson(response));
  }
}
