package Punches;

/**
 * @author Vince Aquilina
 * @version Wed 19 Jan 2022 08:04:00 PM
 *
 * A type representing a Song's time signature, made up of a numerator and a denominator.
 */
public class TimeSignature
{
  private int beatsPerBar;
  private int valueOfABeat;

  public TimeSignature(int beatsPerBar, int valueOfABeat)
  {
    this.beatsPerBar = beatsPerBar;
    this.valueOfABeat = valueOfABeat;
  }

  public void setBeatsPerBar(int beatsPerBar)
  {
    this.beatsPerBar = beatsPerBar;
  }

  public void setValueOfABeat(int valueOfABeat)
  {
    this.valueOfABeat = valueOfABeat;
  }

  public int getBeatsPerBar()
  {
    return beatsPerBar;
  }

  public int getValueOfABeat()
  {
    return valueOfABeat;
  }
}
