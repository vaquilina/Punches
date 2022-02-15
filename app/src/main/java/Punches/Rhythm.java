package Punches;

import javax.sound.midi.Sequence;
import javax.sound.midi.InvalidMidiDataException;
import java.awt.Image;

/**
 * @author Vince Aquilina
 * @version Wed 19 Jan 2022 07:55:45 PM
 *
 * Objects of this class represent a quantized midi 
 * sequence of the rhythm keyed in to the Punches Interface.
 */
public class Rhythm
{
  private Sequence preparedRhythm;

  public Rhythm(Sequence sequence)
  {
    preparedRhythm = sequence;
  }

  //public Sequence prepare()
  //{
  //}

  public String[] toTab()
  {
    return new String[]{};
  }

  //public Image toSheet()
  //{
  //}
}
