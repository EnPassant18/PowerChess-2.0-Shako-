package websockets;

import game.Color;
import game.Game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import players.CliPlayer;
import players.GuiPlayer;

public class ChessWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;
  
  private static final Map<Integer, Game> gameIdMap = new HashMap<>();
  private static final Map<Integer, Integer> playerIdMap = new HashMap<>();

  private static enum MESSAGE_TYPE {
    CREATE_GAME,
    ADD_PLAYER,
    GAME_OVER,
    OFFER_DRAW,
    DRAW_OFFERED,
    PLAYER_ACTION,
    GAME_UPDATE,
    ILLEGAL_ACTION,
    ERROR
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
	  int gameId;
	  int playerId;
	  Game game;
	  /*switch(received.get("type").getAsInt()) {
		  case MESSAGE_TYPE.CREATE_GAME.ordinal():
			  game = new Game();
		  	  gameId = received.get("gameId").getAsInt();
		  	  gameIdMap.put(gameId, game);
			  break;
		  case MESSAGE_TYPE.ADD_PLAYER.ordinal():
			  boolean colorBool = received.get("playerColor").getAsBoolean();
		  	  Color playerColor = Color.BLACK;
		  	  if(colorBool) {
		  		playerColor = Color.WHITE;
		  	  }
			  GuiPlayer player = new GuiPlayer(playerColor);
			  gameId = received.get("gameId").getAsInt();
			  playerId = received.get("playerId").getAsInt();
			  playerIdMap.put(playerId, gameId);
			  game = gameIdMap.get(gameId);
			  game.addPlayer(player);
			  break;
		  case MESSAGE_TYPE.GAME_OVER.ordinal():
			  break;
		  case MESSAGE_TYPE.OFFER_DRAW.ordinal():
			  break;
		  case MESSAGE_TYPE.DRAW_OFFERED.ordinal():
			  break;
		  case MESSAGE_TYPE.PLAYER_ACTION.ordinal():
			  break;
		  case MESSAGE_TYPE.GAME_UPDATE.ordinal():
			  break;
		  case MESSAGE_TYPE.ILLEGAL_ACTION.ordinal():
			  break;
		  case MESSAGE_TYPE.ERROR.ordinal():
			  break;
		  default:
			  break;
	  }*/
  }
}
