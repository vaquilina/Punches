package Punches;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;
/**
 * Metronome for the Punches Interface.
 *
 * @author Vince Aquilina
 * @version 04/01/22
 */
public class Metronome extends Thread
{
  /*
   * TODO: add options for beat subdivisions
   */
  private final Logger logger = LoggerFactory.getLogger(Metronome.class);

  /** Whether the metronome should keep running  */
  private AtomicBoolean keepRunning;
  /** The counter */
  private int counter;
  /** The duration in bars */
  private int duration;
  /** The progress */
  private int progress;
  /** The accent tone */
  private Clip accentTone;
  /** The regular tone */
  private Clip regTone;

  /** The number of beats per minute */
  private double bpm;
  /** The number of beats per measure */
  private int beatsPerMeasure;
  /** The total number of beats in the sequence */
  private int totalBeats;

  /** List of metronome listeners */
  private List<MetronomeListener> listeners = new ArrayList<>();

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
    totalBeats = beatsPerMeasure * duration;

    File accentSoundFile =
      new File(Metronome.class.getResource(
            "/sounds/metro_accent.wav").getPath());

    File regSoundFile = 
      new File(Metronome.class.getResource(
            "/sounds/metro_reg.wav").getPath());

    try {
      accentTone = AudioSystem.getClip();
      accentTone.open(AudioSystem.getAudioInputStream(accentSoundFile));

      regTone = AudioSystem.getClip();
      regTone.open(AudioSystem.getAudioInputStream(regSoundFile));
    }
    catch (IOException ex) {
      logger.error(ex.getMessage());
      ex.printStackTrace();
    }
    catch (LineUnavailableException ex) {
      logger.error(ex.getMessage());
      ex.printStackTrace();
    }
    catch (UnsupportedAudioFileException ex) {
      logger.error(ex.getMessage());
      ex.printStackTrace();
    }
  }

  /**
   * Adds a metronome listener
   *
   * @param listener the listener to add
   */
  public void addMetronomeListener(MetronomeListener listener)
  {
    listeners.add(listener);
  }

  /**
   * Sound the metronome beep
   *
   * @param isAccent whether or not this beep should be an accent
   */
  public void sound(boolean isAccent) 
  {
    if (isAccent) {
      accentTone.setFramePosition(0);
      accentTone.start();
    } else {
      regTone.setFramePosition(0);
      regTone.start();
    }
  }

  public void stopSound()
  {
    if (accentTone.isRunning()) accentTone.stop();
    if (regTone.isRunning()) regTone.stop();
  }

  /**
   * Signal the metronome to stop running
   */
  public void end()
  {
    keepRunning.set(false);
  }

  /**
   * The Main task
   */
  @Override
  public void run()
  {
    progress = 0;
    boolean isCountingIn = true;
    PunchesDialog.setProgressMode(true);

    while(keepRunning.get()) {
      try {
        Thread.sleep((long)(1000 * (60.0 / bpm)));
      }
      catch (InterruptedException ex) {
        logger.error(ex.getMessage());
      }

      if (isCountingIn) {
        if (counter >= beatsPerMeasure) {
          counter = 0;
          PunchesDialog.setProgressMode(false);
          isCountingIn = false;
        }
      }

      if (keepRunning.get() == false) {
        stopSound();

        accentTone.close();
        regTone.close();

        break;
      }

      if (counter >= duration) {
        stopSound();
        keepRunning.set(false);

        logger.debug("end");
      } else if (counter % beatsPerMeasure == 0) {
        // play accent tone
        stopSound();
        sound(true);
        if (! isCountingIn) progress++;

        logger.debug("tick");
      } else {
        // play reg tone
        stopSound();
        sound(false);
        if (! isCountingIn) progress++;

        logger.debug("tock");
      }
      counter++;
      if (! isCountingIn) {
        PunchesDialog.updateProgress(progress, totalBeats);

        for (MetronomeListener listener : listeners) {
          listener.metronomeTicked();
        }
      }
    }
    progress = 0;
  }
}

