package Punches;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfugue.pattern.Pattern;
import org.jfugue.rhythm.Rhythm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Used to assemble a Staccato string containing a percussion pattern.
 *
 * @author Vince Aquilina
 * @version 04/08/22
 */
public class Recorder extends Rhythm implements MetronomeListener
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
  /** The captured sequences */
  private Map<String, List<Long>> captures;

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

  /**
   * Construct a Recorder with given data
   * @param tempo the tempo in bpm
   * @param signature the TimeSignature
   */
  public Recorder(int tempo, TimeSignature signature) {
    this.tempo = tempo;
    this.signature = signature;

    rhythmKit = new LinkedHashMap<>() {{
      put('.', "Rs"); // sixteenth rest
      put('o', "[ACOUSTIC_BASS_DRUM]s");
      put('s', "[ACOUSTIC_SNARE]s");
      put('`', "[CLOSED_HI_HAT]s");
      put('*', "[CRASH_CYMBAL_1]s");
      put('r', "[RIDE_CYMBAL_1]s");
      put('t', "[LO_MID_TOM]s");
      put('f', "[LO_FLOOR_TOM]s");
    }};
    setRhythmKit(rhythmKit);

    layers = new StringBuilder[] {
      new StringBuilder(""), // kick
          new StringBuilder(""), // snare
          new StringBuilder(""), // hi-hat
          new StringBuilder(""), // crash
          new StringBuilder(""), // ride
          new StringBuilder(""), // rack tom
          new StringBuilder("")  // floor tom
    };

    // key=voice, value=millis
    captures = new LinkedHashMap<>() {{
      put("kick",     new ArrayList<>());
      put("snare",    new ArrayList<>());
      put("hihat",    new ArrayList<>());
      put("crash",    new ArrayList<>());
      put("ride",     new ArrayList<>());
      put("racktom",  new ArrayList<>());
      put("floortom", new ArrayList<>());
    }};
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
   * Records a "hit"; the voice, and the time it occurred
   * @param keys the set of keys currently depressed
   */
  public void registerHit(Set<Character> keys)
  {
    long millis = System.currentTimeMillis() - barStart;
    for (Character c : keys) {
      c = Character.toLowerCase(c);
      switch (c) {
        case 'c':
          captures.get("crash").add(millis);
          logger.debug("recorded crash at {}", millis);
          break;
        case 'r':
          captures.get("ride").add(millis);
          logger.debug("recorded ride at {}", millis);
          break;
        case 'h':
          captures.get("hihat").add(millis);
          logger.debug("recorded hihat at {}", millis);
          break;
        case 't':
          captures.get("racktom").add(millis);
          logger.debug("recorded racktom at {}", millis);
          break;
        case 's':
          captures.get("snare").add(millis);
          logger.debug("recorded snare at {}", millis);
          break;
        case 'f':
          captures.get("floortom").add(millis);
          logger.debug("recorded floortom at {}", millis);
          break;
        case ' ':
          captures.get("kick").add(millis);
          logger.debug("recorded kickdrum at {}", millis);
          break;
      }
    }
  }

  /**
   * Convert captures to JFugue Rhythms
   */
  private void parseCaptures()
  {
    // TODO work out time of hit based on millis
    // TODO replace rest in rhythmic layer with hit
  }

  ///////////////////////////////
  // MetronomeListener Methods //
  ///////////////////////////////

  /** the duration of a note in the current context, in millis */
  private long noteDuration;
  /** the total duration of the bar in the current context, in millis */
  private long barDuration;

  private long barStart = 0;
  private long noteBegin = 0;
  private long noteEnd = 0;

  private int counter = 0;
  private boolean isCountedIn;

  @Override
  public void metronomeTicked()
  {
    logger.debug("metronome ticked");

    // calculate the note duration in millis while the metronome is counting in
    if (counter == 0) {
      noteBegin = System.currentTimeMillis();
      barStart = noteBegin;
    } else if (counter == 1) {
      noteEnd = System.currentTimeMillis();
      noteDuration = noteEnd - noteBegin;
      barDuration = noteDuration * signature.getBeatsPerBar();

      logger.debug("note duration in millis: {}", noteDuration);
    }
    if (counter < signature.getBeatsPerBar()) {
      counter++;
    } else {
      isCountedIn = true;
    }

    if (isCountedIn) {
      for (StringBuilder layer : layers) {
        layer.append('.');
      }
    }
  }

  @Override
  public void metronomeEnded()
  {
    logger.debug("metronome ended");

    String[] compiledLayers = new String[7];
    for (int i = 0; i < layers.length; i++) {
      compiledLayers[i] = layers[i].toString();
      logger.debug(compiledLayers[i]);
    }

    parseCaptures();
  }
}

