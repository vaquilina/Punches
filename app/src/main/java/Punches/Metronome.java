package Punches;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;
/**
 * Metronome for the Punches Interface.
 *
 * @author Vince Aquilina
 * @version 03/22/22
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
  /** The progess */
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

    // TODO figure out values
    accentTone = generateTone(440, 20, 44100.00f, 20, false);
    regTone = generateTone(540, 20, 44100.00f, 20, false);
  }

  /**
   * Signal the metronome to stop running
   */
  public void end()
  {
    keepRunning.set(false);
  }

  /**
   * Generate a tone and store it in a clip
   *
   * https://stackoverflow.com/questions/7782721/
   * java-raw-audio-output/7782749#7782749
   *
   * @param hz the frequency of the note 
   * @param wavelengths the number of wavelengths
   * @param sampleRate the sample rate
   * @param framesPerWavelength the number of frames per wavelength
   * @param addHarmonic whether to add harmonic
   */
  public Clip generateTone(int hz, int wavelengths, float sampleRate,
      int framesPerWavelength, boolean addHarmonic)
  {
    Clip clip = null;
    try {
      clip = AudioSystem.getClip();

      if (accentTone != null) {
        accentTone.stop();
        accentTone.close();
      } else {
        accentTone = AudioSystem.getClip();
      }
      if (regTone != null) {
        regTone.stop();
        regTone.close();
      } else {
        regTone = AudioSystem.getClip();
      }

      // sound does not loop well for less than 5 wavelengths...
      if (wavelengths < 5) wavelengths = 5; 

      byte[] buf = new byte[2 * framesPerWavelength * wavelengths];

      AudioFormat af = new AudioFormat(
        sampleRate,
        8,     // sample size in bits
        2,     // channels
        true,  // signed
        false  // big endian
      );

      int maxVol = 127;
      for (int i = 0; i < framesPerWavelength * wavelengths; i++) {
        double angle =
          ((float) (i * 2)/ ((float) framesPerWavelength)) * (Math.PI);

        buf[i * 2] =
          (byte) (Integer.valueOf(
                (int) Math.round(Math.sin(angle) * maxVol))).byteValue();

        if (addHarmonic) {
          buf[(i * 2) + 1] =
            (byte) (Integer.valueOf(
                  (int) Math.round(Math.sin(2 * angle) * maxVol))).byteValue();
        } else {
          buf[(i * 2) + 1] = buf[i * 2];
        }
      }

      byte[] b = buf;
      AudioInputStream ais = new AudioInputStream(
          new ByteArrayInputStream(b),
          af,
          buf.length / 2);

      try {
        clip.open(ais);
      }
      catch (IOException ex) {
        logger.error(ex.getMessage());

        ex.printStackTrace();
      }
    }
    catch (LineUnavailableException ex) {
      logger.error(ex.getMessage());
      ex.printStackTrace();
    }
    return clip;
  }

  /**
   * The Main task
   */
  @Override
  public void run()
  {
    progress = 0;

    while(keepRunning.get()) {
      try {
        Thread.sleep((long)(1000 * (60.0 / bpm)));
      }
      catch (InterruptedException ex) {
        logger.error(ex.getMessage());
      }

      if (keepRunning.get() == false) {
        if (accentTone != null) {
          accentTone.stop();
          accentTone.close();
        }
        if (regTone != null) {
          regTone.stop();
          regTone.close();
        }

        break;
      }

      counter++;
      if (counter > duration) {
        keepRunning.set(false);

        logger.debug("end");
        break;
      }
      else if (counter % beatsPerMeasure == 0) {
        accentTone.setFramePosition(0);
        accentTone.loop(1);

        progress++;

        logger.debug("tick");
      }
      else {
        regTone.setFramePosition(0);
        regTone.loop(1);

        progress++;

        logger.debug("tock");
      }
      PunchesDialog.updateProgress(progress, totalBeats);
    }
    progress = 0;
  }
}

