package Punches;

import javax.sound.midi.Sequence;
import javax.sound.midi.InvalidMidiDataException;
import java.awt.image.BufferedImage;
/**
 * Objects of this class represent a quantized midi 
 * sequence of the rhythm keyed in to the Punches Interface.
 *
 * @author Vince Aquilina
 * @version 03/11/22
 */
public class Rhythm
{
  /** The "raw" (unquantized) MIDI sequence */
  private Sequence received;
  /** The "prepared" (quantized) MIDI sequence */
  private Sequence preparedRhythm;

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

