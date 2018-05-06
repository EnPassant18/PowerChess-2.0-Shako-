package websockets;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * HomeWebSocket allows server to communicate with clients on home page.
 *
 * @author knorms
 *
 */
@WebSocket
public class HomeWebSocket {
  private static final Queue<Session> SESSIONS = new ConcurrentLinkedQueue<>();
  private static final Gson GSON = new Gson();

  /**
   * Enumerates types of messages server sends to clients on the homepage.
   *
   * @author knorms
   *
   */
  public enum MessageType {
    ALL_GAMES, ADD_GAME, REMOVE_GAME
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
    System.out.println("Session joined");
    SESSIONS.add(session);
    JsonObject response = new JsonObject();
    response.add("games", ChessWebSocket.getPublicGames());
    response.addProperty("type", MessageType.ALL_GAMES.ordinal());
    session.getRemote().sendString(GSON.toJson(response));
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
   * Update all clients on homepage that public game was added.
   *
   * @param gameUpdate
   *          Game Add Update to send.
   */
  public static void gameAdded(JsonObject gameUpdate) {
    gameUpdate.addProperty("type", MessageType.ADD_GAME.ordinal());

    String toSend = GSON.toJson(gameUpdate);
    SESSIONS.forEach(session -> {
      try {
        session.getRemote().sendString(toSend);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Update all clients on homepage that public game was removed.
   *
   * @param gameUpdate
   *          Game Removal Update to send.
   */
  public static void gameRemoved(JsonObject gameUpdate) {
    gameUpdate.addProperty("type", MessageType.ADD_GAME.ordinal());

    String toSend = GSON.toJson("jsonMessage");
    SESSIONS.forEach(session -> {
      try {
        session.getRemote().sendString(toSend);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}
