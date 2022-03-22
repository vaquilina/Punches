package Punches;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;
/**
 * Metronome for the Punches Interface.
 *
 * @author Vince Aquilina
 * @version 03/21/22
 */
public class Metronome extends Thread
{
  /*
   * TODO: allow for counting with true beat value
   * TODO: ensure duration and start/end are correct
   */
  private final Logger logger = LoggerFactory.getLogger(Metronome.class);

  /** Whether the metronome should keep running  */
  private AtomicBoolean keepRunning;
  /** The counter */
  private int counter;
  /** The duration in bars */
  private int duration;

  /** The number of beats per minute */
  private double bpm;
  /** The number of beats per measure */
  private int beatsPerMeasure;

  /**
   * Construct a Metronome.
   *
   * @param bpm the number of beats per minutes
   * @param beatsPerMeasure the number of beats per measure
   * @param duration the number of bars for metronome to play
   */
  public Metronome(double bpm, int beatsPerMeasure, int duration) 
  {
    this.bpm = bpm;
    this.beatsPerMeasure = beatsPerMeasure;
    this.duration = duration * beatsPerMeasure;

    keepRunning = new AtomicBoolean(true);
    counter = 0;
  }

  /**
   * Signal the metronome to stop running
   */
  public void end()
  {
    keepRunning.set(false);
  }

  @Override
  public void run()
  {
    while(keepRunning.get()) {
     try {
      Thread.sleep((long)(1000 * (60.0 / bpm)));
     }
     catch (InterruptedException ex) {
       logger.error(ex.getMessage());
     }

     counter++;
     if (counter % beatsPerMeasure == 0) {
        // TODO generate accent tone 

       logger.debug("tick");
     }
     else if (counter >= duration) {
        // TODO generate accent tone 
       keepRunning.set(false);

       logger.debug("end");
     }
     else {
       // TODO generate reg tone

      logger.debug("tock");
     }
    }
  }
}
