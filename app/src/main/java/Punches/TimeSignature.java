package Punches;

import java.io.Serializable;
/**
 * A type representing a Song's time signature,
 * made up of a numerator and a denominator.
 *
 * @author Vince Aquilina
 * @version 03/13/22
 */
public class TimeSignature implements Serializable
{
  private int beatsPerBar;
  private BeatValue valueOfABeat;

  /**
   * Construct a TimeSignature with given values
   *
   * @param beatsPerBar - the number of beats per bar
   * @param valueOfABeat - the value of one beat
   */
  public TimeSignature(int beatsPerBar, BeatValue valueOfABeat)
  {
    this.beatsPerBar = beatsPerBar;
    this.valueOfABeat = valueOfABeat;
  }

  /**
   * Set the number of beats per bar
   *
   * @param beatsPerBar - the number of beats per bar
   */
  public void setBeatsPerBar(int beatsPerBar)
  {
    this.beatsPerBar = beatsPerBar;
  }

  /**
   * Set the value of a beat
   *
   * @param valueOfABeat - the value of one beat
   */
  public void setValueOfABeat(BeatValue valueOfABeat)
  {
    this.valueOfABeat = valueOfABeat;
  }

  /**
   * Get the number of beats per bar
   *
   * @return the number of beats per bar
   */
  public int getBeatsPerBar()
  {
    return beatsPerBar;
  }

  /**
   * Get the value of a beat
   *
   * @return the value of one beat
   */
  public BeatValue getValueOfABeat()
  {
    return valueOfABeat;
  }

  /**
   * Get String representation of the TimeSignature
   *
   * @return a string representation of the TimeSignature
   */
  @Override
  public String toString()
  {
    // TODO replace with better enum

    String beatValue = "";
    switch(valueOfABeat) {
      case WHOLE:
        beatValue = "1";
        break;
      case HALF:
        beatValue = "2";
        break;
      case QUARTER:
        beatValue = "4";
        break;
      case EIGHTH:
        beatValue = "8";
        break;
      case SIXTEENTH:
        beatValue = "16";
        break;
      case THIRTY_SECOND:
        beatValue = "32";
    }

    return beatsPerBar + "/" + beatValue;
  }
}
