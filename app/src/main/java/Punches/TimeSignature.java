package Punches;

/**
 * @author Vince Aquilina
 * @version Tue 15 Feb 2022
 *
 * A type representing a Song's time signature,
 * made up of a numerator and a denominator.
 */
public class TimeSignature
{
  private int beatsPerBar;
  private int valueOfABeat;

  /**
   * Constructs a TimeSignature with given values
   *
   * @param beatsPerBar - the number of beats per bar
   * @param valueOfABeat - the value of one beat
   */
  public TimeSignature(int beatsPerBar, int valueOfABeat)
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
  public void setValueOfABeat(int valueOfABeat)
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
  public int getValueOfABeat()
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
    return beatsPerBar + "/" + valueOfABeat;
  }
}
