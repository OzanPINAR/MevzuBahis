package ege.mevzubahis.Network;

/**
 * Created by ege on 18.03.2017.
 */

public class Bets {

  private String result;
  private String event;

  public Bets(String result, String event) {
    this.result = result;
    this.event = event;
  }

  public Bets() {
  } // you MUST have an empty constructor

  public String getResult() {
    return result;
  }

  public String getEvent() {
    return event;
  }
}
