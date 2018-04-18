package players;

import java.util.List;

import board.Location;
import game.Color;
import game.Move;
import pieces.Piece;
import powerups.PowerAction;

/**
 * A Player that can play a game of chess via interaction with the command line
 * interface.
 *
 * @author knorms
 *
 */
public class CliPlayer extends Player {

  /**
   * Constructs command line interface player of specified color.
   *
   * @param color
   *          Player color.
   */
  public CliPlayer(Color color) {
    super(color);
  }

  @Override
  public Move getMove() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Move getMove(Location start) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Piece getPromotion() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PowerAction selectPowerAction(List<PowerAction> actions) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean getCanCastleLong() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean getCanCastleShort() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setCanCastleLong(boolean canCastleLong) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setCanCastleShort(boolean canCastleShort) {
    // TODO Auto-generated method stub

  }

}
