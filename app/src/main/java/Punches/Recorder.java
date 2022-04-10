package Punches;

import java.util.ArrayList;
import java.util.Arrays;
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
 * @version 04/09/22
 */
public class Recorder extends Rhythm implements MetronomeListener
{
  private final Logger logger = LoggerFactory.getLogger(Recorder.class);

  /** The number of notes per beat */
  private final int RESOLUTION = 4;

  /** The rhythm kit for this drumkit */
  public Map<Character, String> rhythmKit;
  /** The rhythmic "layers" (zero-based) */
  private StringBuilder[] layers;
  /** The captured sequences */
  private Map<String, List<Long>> captures;

  /** The tempo in bpm */
  private final int tempo;
  /** The time signature */
  private final TimeSignature signature;
  /** The duration of the phrase, in bars */
  private final int numOfBars;

  /**
   * Construct a Recorder
   */
  public Recorder()
  {
    this(120, new TimeSignature(4, BeatValue.QUARTER), 4);
  }

  /**
   * Construct a Recorder with given data
   * @param tempo the tempo in bpm
   * @param signature the TimeSignature
   */
  public Recorder(int tempo, TimeSignature signature, int numOfBars) {
    this.tempo = tempo;
    this.signature = signature;
    this.numOfBars = numOfBars;

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
      new StringBuilder(""), // 0 kick
      new StringBuilder(""), // 1 snare
      new StringBuilder(""), // 2 hi-hat
      new StringBuilder(""), // 3 crash
      new StringBuilder(""), // 4 ride
      new StringBuilder(""), // 5 rack tom
      new StringBuilder("")  // 6 floor tom
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
    long millis = System.currentTimeMillis() - captureStartMillis;
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
    if (noteDuration > 0) {
      // mark timestamp of each note position
      final int totalNotes =
        (signature.getBeatsPerBar() * RESOLUTION) * numOfBars;

      final long[] notePositions = new long[totalNotes];
      for (int i = 0; i < notePositions.length; i++) {
        notePositions[i] = (i * noteDuration) / RESOLUTION;
      }
      logger.debug("note positions: {}", Arrays.toString(notePositions));

      /* place notes in rhythm layers */

      // KICK layer
      List<Long> timestamps = captures.get("kick");
      logger.debug("kick timestamps: {}", timestamps.toString());
      StringBuilder layer = layers[0];
      for (int i = 0; i < totalNotes; i++) {
        for (Long timestamp : timestamps) {
          if (timestamp >= notePositions[i] &&
              timestamp < notePositions[i + 1]) {
            layer.setCharAt(i, 'o');
            logger.debug("placed kick @ position {}", i);
          }
        }
      }

      // SNARE layer
      timestamps = captures.get("snare");
      logger.debug("snare timestamps: {}", timestamps.toString());
      layer = layers[1];
      for (int i = 0; i < totalNotes; i++) {
        for (Long timestamp : timestamps) {
          if (timestamp >= notePositions[i] &&
              timestamp < notePositions[i + 1]) {
            layer.setCharAt(i, 's');
            logger.debug("placed snare @ position {}", i);
          }
        }
      }

      // HIHAT layer
      timestamps = captures.get("hihat");
      logger.debug("hihat timestamps: {}", timestamps.toString());
      layer = layers[2];
      for (int i = 0; i < totalNotes; i++) {
        for (Long timestamp : timestamps) {
          if (timestamp >= notePositions[i] &&
              timestamp < notePositions[i + 1]) {
            layer.setCharAt(i, '`');
            logger.debug("placed hihat @ position {}", i);
          }
        }
      }

      // CRASH layer
      timestamps = captures.get("crash");
      logger.debug("crash timestamps: {}", timestamps.toString());
      layer = layers[3];
      for (int i = 0; i < totalNotes; i++) {
        for (Long timestamp : timestamps) {
          if (timestamp >= notePositions[i] &&
              timestamp < notePositions[i + 1]) {
            layer.setCharAt(i, '*');
            logger.debug("placed crash @ position {}", i);
          }
        }
      }

      // RIDE layer
      timestamps = captures.get("ride");
      logger.debug("ride timestamps: {}", timestamps.toString());
      layer = layers[4];
      for (int i = 0; i < totalNotes; i++) {
        for (Long timestamp : timestamps) {
          if (timestamp >= notePositions[i] &&
              timestamp < notePositions[i + 1]) {
            layer.setCharAt(i, 'r');
            logger.debug("placed ride @ position {}", i);
          }
        }
      }

      // RACKTOM layer
      timestamps = captures.get("racktom");
      logger.debug("racktom timestamps: {}", timestamps.toString());
      layer = layers[5];
      for (int i = 0; i < totalNotes; i++) {
        for (Long timestamp : timestamps) {
          if (timestamp >= notePositions[i] &&
              timestamp < notePositions[i + 1]) {
            layer.setCharAt(i, 't');
            logger.debug("placed racktom @ position {}", i);
          }
        }
      }

      // FLOORTOM layer
      timestamps = captures.get("floortom");
      logger.debug("floortom timestamps: {}", timestamps.toString());
      layer = layers[6];
      for (int i = 0; i < totalNotes; i++) {
        for (Long timestamp : timestamps) {
          if (timestamp >= notePositions[i] &&
              timestamp < notePositions[i + 1]) {
            layer.setCharAt(i, 'f');
            logger.debug("placed floortom @ position {}", i);
          }
        }
      }
    }
  }

  ///////////////////////////////
  // MetronomeListener Methods //
  ///////////////////////////////

  /** the duration of a note in the current context, in millis */
  private long noteDuration;

  private long captureStartMillis = 0;

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
    } else if (counter == 1) {
      noteEnd = System.currentTimeMillis();
      noteDuration = noteEnd - noteBegin;
      captureStartMillis =
        noteBegin + noteDuration * signature.getBeatsPerBar(); 

      logger.debug("note duration in millis: {}", noteDuration);
    }
    if (counter < signature.getBeatsPerBar()) {
      counter++;
    } else {
      isCountedIn = true;
    }

    if (isCountedIn) {
      for (StringBuilder layer : layers) {
        for (int i = 0; i < RESOLUTION; i++) {
          layer.append('.');
        }
      }
    }
  }

  @Override
  public void metronomeEnded()
  {
    logger.debug("metronome ended");

    StringBuilder countGuide = new StringBuilder();

    for (int i = 0; i < numOfBars; i++) {
      for (int j = 1; j <= signature.getBeatsPerBar(); j++) {
        countGuide.append(Character.forDigit(j, 10));
        countGuide.append("e&a");
      }
    }

    parseCaptures();

    logger.debug(countGuide.toString());
    List<String> compiledLayers = new ArrayList<>();
    for (int i = 0; i < layers.length; i++) {
      compiledLayers.add(layers[i].toString());
      logger.debug(compiledLayers.get(i));
    }

    setLayers(compiledLayers);
  }
}

