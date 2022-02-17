package Punches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class TimeSignatureTest
{
  private final TimeSignature sig = new TimeSignature(4, 4);

  @Test
  void timeSignatureShouldBeCreated()
  {
    assertEquals(4, sig.getBeatsPerBar());
    assertEquals(4, sig.getValueOfABeat());
  }

  @Test
  void stringRepresentationShouldBeReturned()
  {
    assertEquals("4/4", sig.toString());
  }
}
