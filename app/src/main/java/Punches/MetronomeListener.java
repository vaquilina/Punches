package Punches;

/**
 * An interface to be implemented for listening for Metronome ticks.
 *
 * @author Vince Aquilina
 * @version 04/01/22
 */
public interface MetronomeListener
{
  /**
   * The metronome has sounded a tick
   */
  void metronomeTicked();

  /**
   * The metronome has stopped sounding
   */
  void metronomeEnded();
}
