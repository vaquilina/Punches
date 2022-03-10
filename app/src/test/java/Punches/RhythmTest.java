package Punches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import javax.sound.midi.Sequence;
import javax.sound.midi.InvalidMidiDataException;

/**
 * @author Vince Aquilina
 * @version 03/09/22
 *
 * Tests for the Rhythm class.
 */ 
class RhythmTest
{
  /* TODO 
   *
   * - rhythm gets quantized correctly
   * - preparedRhythm is assigned 
   * - sequence successfully converted to image
   * - sequence successfully converted to tab
   */
  private Rhythm rhythm;

  /**
   * Default Rhythm should be constructed.
   */
  @Test
  void defaultRhythmShouldBeConstructed() throws InvalidMidiDataException
  {
    rhythm = new Rhythm(new Sequence(Sequence.PPQ, 4));

    assertNotNull(rhythm.getReceived());
    assertNull(rhythm.getPreparedRhythm());
  }
}
