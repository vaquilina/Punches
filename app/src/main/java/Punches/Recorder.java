package Punches;

import java.util.Map;
import java.util.LinkedHashMap;

import org.jfugue.pattern.Pattern;
import org.jfugue.rhythm.Rhythm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Used to assemble a Staccato string containing a percussion pattern.
 *
 * @author Vince Aquilina
 * @version 03/31/22
 */
public class Recorder extends Rhythm
{
  private final Logger logger = LoggerFactory.getLogger(Recorder.class);

  /** The rhythm kit for this drumkit */
  private Map<Character, String> rhythmKit;

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

    rhythmKit = new LinkedHashMap<>();
    //TODO
    //initialize kit with custom values
    //setRhythmKit(rhythmKit);
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

}
