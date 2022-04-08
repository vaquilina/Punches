package Punches;

import java.io.Serializable;
/**
 * A type representing a Song's time signature,
 * made up of a numerator and a denominator.
 *
 * @author Vince Aquilina
 * @version 03/17/22
 */
public class TimeSignature implements Serializable
{
  /** The number of beats per bar */
  private int beatsPerBar;
  /** The value of a beat */
  private BeatValue valueOfABeat;

  /**
   * Construct a TimeSignature with given values
   *
   * @param beatsPerBar the number of beats per bar
   * @param valueOfABeat the value of one beat
   */
  public TimeSignature(int beatsPerBar, BeatValue valueOfABeat)
  {
    this.beatsPerBar = beatsPerBar;
    this.valueOfABeat = valueOfABeat;
  }

  /**
   * Set the number of beats per bar
   *
   * @param beatsPerBar the number of beats per bar
   */
  public void setBeatsPerBar(int beatsPerBar)
  {
    this.beatsPerBar = beatsPerBar;
  }

  /**
   * Set the value of a beat
   *
   * @param valueOfABeat the value of one beat
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
    StringBuilder builder = new StringBuilder();
    builder.append(beatsPerBar + "/");

    switch (valueOfABeat) {
      case WHOLE:
        builder.append("1");
        break;
      case HALF:
        builder.append("2");
        break;
      case QUARTER:
        builder.append("4");
        break;
      case EIGHTH:
        builder.append("8");
        break;
      case SIXTEENTH:
        builder.append("16");
        break;
      default:
        builder.append("4");
    }

    return builder.toString();
  }
}
