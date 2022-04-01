package Punches;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.jfugue.pattern.Pattern;
import org.jfugue.rhythm.Rhythm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Used to assemble a Staccato string containing a percussion pattern.
 *
 * @author Vince Aquilina
 * @version 04/01/22
 */
public class Recorder extends Rhythm implements MetronomeListener, KeyListener
{
  private final Logger logger = LoggerFactory.getLogger(Recorder.class);

  /** The rhythm kit for this drumkit */
  public Map<Character, String> rhythmKit;
  /** The rhythmic "layers" (zero-based)
   *
   * Layers:
   * 0 - Kick/Bass Drum
   * 1 - Floor Tom
   * 2 - Snare Drum
   * 3 - Rack Tom
   * 4 - Hi-hat
   * 5 - Ride Cymbal
   * 6 - Crash Cymbal
   */
  private StringBuilder[] layers;

  /** The tempo in bpm */
  private int tempo;
  /** The time signature */
  private TimeSignature signature;

  /**
   * Construct a Recorder
   */
  public Recorder()
  {
    this(120, new TimeSignature(4, BeatValue.QUARTER));
  }

  /** Construct a Recorder with given data
   * @param tempo the tempo in bpm
   * @param signature the TimeSignature
   */
  public Recorder(int tempo, TimeSignature signature) {
    this.tempo = tempo;
    this.signature = signature;

    rhythmKit = new LinkedHashMap<>() {{
      put('.', "Rs");
      put('o', "[ACOUSTIC_BASS_DRUM]s");
      put('s', "[ACOUSTIC_SNARE]s");
      put('`', "[CLOSED_HI_HAT]s");
      put('*', "[CRASH_CYMBAL_1]s");
      put('r', "[RIDE_CYMBAL_1]s");
      put('t', "[LO_MID_TOM]s");
      put('f', "[LO_FLOOR_TOM]s");
    }};
    setRhythmKit(rhythmKit);

    layers = new StringBuilder[7];
  }

  /**
   * Get the modified Staccato string
   * @return the modified Staccato string
   */
  public Pattern getModifiedPattern()
  {
    // TODO modify pattern with appropriate tempo & time signature
    logger.debug(getPattern().toString());
    return getPattern();
  }

  /**
   * Iterate over the map of keys currently depressed and construct
   * a Rhythm consisting of all the active voices
   */
  private void processKeys()
  {
    if (pressed.size() > 1) {
      for (Character c : pressed) {
        c = Character.toLowerCase(c);
        switch (c) {
          case 'c':
            layers[6].append('*');
            logger.debug("found crash");
            break;
          case 'r':
            layers[5].append('r');
            logger.debug("found ride");
            break;
          case 'h':
            layers[4].append('`');
            logger.debug("found hihat");
            break;
          case 't':
            layers[3].append('t');
            logger.debug("found racktom");
            break;
          case 's':
            layers[2].append('s');
            logger.debug("found snare");
            break;
          case 'f':
            layers[1].append('f');
            logger.debug("found floortom");
            break;
          case ' ':
            layers[0].append(' ');
            logger.debug("found kickdrum");
            break;
        }
      }
    } else {
      for (StringBuilder layer : layers) {
        layer.append('.');
      }
      logger.debug("found rest");
    }
  }
  ///////////////////////////////
  // MetronomeListener Methods //
  ///////////////////////////////

  @Override
  public void metronomeTicked()
  {
    // TODO figure out how to capture notes in time!!!!!
    logger.debug("metronome ticked");
  }

  /////////////////////////
  // KeyListener Methods //
  /////////////////////////

  private final String VALID_CHARS = "crhtsf CRHTSF";
  private final Set<Character> pressed = new HashSet<>();

  @Override
  public void keyPressed(KeyEvent e)
  {
    // check for character of interest
    if (! (VALID_CHARS.indexOf(String.valueOf(e.getKeyChar())) < 0))  {
      pressed.add(e.getKeyChar());
    }
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    processKeys();
    pressed.clear();
  }

  @Override
  public void keyTyped(KeyEvent e) { /* not used */ }
}

