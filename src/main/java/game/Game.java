package game;

import java.time.Clock;
import java.util.List;
import java.util.Map;

import board.Board;
import utility.Pair;

/**
 * Game represents a game of chess.
 *
 * @author knorms
 *
 */
public class Game {
  private Map<Color, Pair<Player, Clock>> players;
  private Board board;
  private List<Board> history; // TODO should this be list of strings?

  // TODO add constructor that starts a game

  /**
   * Execute next player's turn.
   */
  public void turn() {
    // TODO implement
  }

}
