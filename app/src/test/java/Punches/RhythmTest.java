package Punches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import javax.sound.midi.Sequence;
import javax.sound.midi.InvalidMidiDataException;

/*
 * TODO 
 *
 * - rhythm gets quantized correctly
 * - preparedRhythm is assigned 
 * - sequence successfully converted to image
 * - sequence successfully converted to tab
 */
class RhythmTest
{
  private Rhythm rhythm;

  @Test
  void defaultRhythmShouldBeConstructed() throws InvalidMidiDataException
  {
    rhythm = new Rhythm(new Sequence(Sequence.PPQ, 4));

    assertNotNull(rhythm.getReceived());
    assertNull(rhythm.getPreparedRhythm());
  }
}
