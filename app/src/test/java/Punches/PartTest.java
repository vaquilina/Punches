package Punches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class PartTest
{
  Part part = new Part();

  @Disabled
  @Test
  void defaultPartObjectShouldBeCreated()
  {
    assertEquals("intro", part.getName());
    assertEquals(4, part.getLengthInBars());
    assertEquals("", part.getNotes());
    assertNull(part.getSheetSnippet());
    assertNull(part.getTabSnippet()[0]);
  }

  @Disabled
  @Test
  void customPartObjectShouldBeCreated()
  {
    part.setName("verse");
    part.setLengthInBars(8);
    part.setNotes("keep it cool");

    assertEquals("verse", part.getName());
    assertEquals(8, part.getLengthInBars());
    assertEquals("keep it cool", part.getNotes());
  }

  // TODO: incomplete
}
