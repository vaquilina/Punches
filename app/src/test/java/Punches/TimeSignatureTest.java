package Punches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

/**
 * Tests for the TimeSignature class.
 */
class TimeSignatureTest
{
  private final TimeSignature sig = new TimeSignature(4, BeatValue.QUARTER);

  /**
   * Default TimeSignature should be created
   */
  @Test
  void defaultTimeSignatureShouldBeCreated()
  {
    assertEquals(4, sig.getBeatsPerBar());
    assertEquals(BeatValue.QUARTER, sig.getValueOfABeat());
  }

  /**
   * String representation of TimeSignature object should be returned
   */
  @Test
  void stringRepresentationShouldBeReturned()
  {
    assertEquals("4/4", sig.toString());
  }
}
