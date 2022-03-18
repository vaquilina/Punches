package Punches;

import javax.swing.JDialog;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

/**
 * The "Punches Interface" - The user will key in their rhythm here.
 *
 * @author Vince Aquilina
 * @version 03/17/22
 */
public class PunchesDialog extends JDialog
{
  /** The MIDI sequence */
  private Sequence sequence;
  /** The MIDI track; */
  private Track track;
  /** The quantized rhythm */
  private Rhythm rhythm;
  /** The Part to which the rhythm will be assigned */
  private Part relevantPart;

  /**
   * Construct a PunchesDialog
   *
   * @param relevantPart the Part to which the rhythm will be assigned
   */
  public PunchesDialog(Part relevantPart)
  {
    this.relevantPart = relevantPart;
  }

  /**
   * Create a Rhythm from a MIDI sequence
   *
   * @return the newly created rhythm
   */
  /*private Rhythm generateRhythm()
  {
    // TODO implement method
    return new Rhythm();
  }

  /**
   * Capture a sequence
   *
   * @return the captured sequence
   */
  //private Sequence captureSequence()
  //{
  //  // TODO implement method
  //  // PPQ indicates tempo-based timing (pulses per quarter)

  //  return new Sequence(Sequence.PPQ, 4);
  //  // will want to use higher resolution, then quantize down
  //}
}

