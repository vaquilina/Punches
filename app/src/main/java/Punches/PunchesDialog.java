package Punches;

import javax.swing.JDialog;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

/**
 * @author Vince Aquilina
 * @version Wed 19 Jan 2022 05:06:59 PM
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
    return new Rhythm();
  }

  private Sequence captureSequence()
  {
    // PPQ indicates tempo-based timing (pulses per quarter)
    return new Sequence(Sequence.PPQ, 4);
  }*/
}
