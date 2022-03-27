package Punches;

import java.awt.image.BufferedImage;

import java.io.Serializable;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Objects of this class represent a quantized midi 
 * sequence of the rhythm keyed in to the Punches Interface.
 *
 * @author Vince Aquilina
 * @version 03/26/22
 */
public class Rhythm implements Serializable
{
  private final Logger logger = LoggerFactory.getLogger(Rhythm.class);

  /** The "raw" (unquantized) MIDI sequence */
  private Sequence receivedRhythm;
  /** The "prepared" (quantized) MIDI sequence */
  private Sequence preparedRhythm;

  /**
   * Construct a Rhythm from a Sequence
   *
   * @param sequence the Sequence
   */
  public Rhythm(Sequence sequence)
  {
    receivedRhythm = sequence;
  }

  /**
   * Get the Sequence that was passed in
   *
   * @return the Sequence that was passed in
   */
  public Sequence getReceivedRhythm()
  {
    return receivedRhythm;
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
}

