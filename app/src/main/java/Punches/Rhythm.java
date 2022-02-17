package Punches;

import javax.sound.midi.Sequence;
import javax.sound.midi.InvalidMidiDataException;
import java.awt.image.BufferedImage;

/**
 * @author Vince Aquilina
 * @version Wed 16 Feb 2022
 *
 * Objects of this class represent a quantized midi 
 * sequence of the rhythm keyed in to the Punches Interface.
 */
public class Rhythm
{
  private Sequence received;        // "raw" unquantized
  private Sequence preparedRhythm;  // quantized

  /**
   * Construct a Rhythm from a Sequence
   *
   * @param sequence - the Sequence
   */
  public Rhythm(Sequence sequence)
  {
    received = sequence;
  }

  /**
   * Get the Sequence that was passed in
   *
   * @return the Sequence that was passed in
   */
  public Sequence getReceived()
  {
    return received;
  }

  /**
   * Get the prepared Sequence
   *
   * @return the Sequence that was passed in
   */
  public Sequence getPreparedRhythm()
  {
    return preparedRhythm;
  }

  // helper method that quantizes the received rhythm
  // private Sequence prepare() throws InvalidMidiDataException {}
  
  // converts preparedRhythm to sheet image
  // public BufferedImage toSheet() {}
  
  // converts preparedRhythm to tab snippet
  // public String[] toTab() {}
}
