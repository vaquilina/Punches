package Punches;

import javax.swing.JDialog;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

/**
 * @author Vince Aquilina
 * @version 03/10/22
 *
 * The "Punches Interface".
 */
public class PunchesDialog extends JDialog
{
  private Sequence sequence;
  private Track track;
  private Rhythm rhythm;
  private Part relevantPart;

  public PunchesDialog(Part relevantPart)
  {
    this.relevantPart = relevantPart;
  }

  /*private Rhythm generateRhythm()
  {
    // TODO implement method
    return new Rhythm();
  }

  private Sequence captureSequence()
  {
    // TODO implement method
    // PPQ indicates tempo-based timing (pulses per quarter)

    return new Sequence(Sequence.PPQ, 4);
    // will want to use higher resolution, then quantize down
  }*/
}
