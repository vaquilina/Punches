package Punches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SongTest
{
  private final Song song = new Song();

  @Test
  void defaultSongObjectShouldBeCreated()
  {
    assertEquals("", song.getTitle());
    assertEquals("4/4", song.getSignature().toString());
    assertEquals(120, song.getBpm());
    assertTrue(song.getParts().isEmpty());
  }

  @Test
  void customSongShouldBeCreated()
  {
    song.setParts(new ArrayList<Part>());
    song.addNewPart();
    song.setSignature(new TimeSignature(6, 8));
    song.setTitle("15 Step");
    song.setBpm(80);

    assertFalse(song.getParts().isEmpty());
    assertEquals("6/8", song.getSignature().toString());
    assertEquals("15 Step", song.getTitle());
    assertEquals(80, song.getBpm());
  }

  @Test
  void newPartShouldBeAdded()
  {
    song.setParts(new ArrayList<Part>());
    song.addNewPart();

    assertEquals(1, song.getParts().size());
  }

  @Test
  void existingPartShouldBeAdded()
  {
    Part testPart = new Part("intro", 4, "count in 4");
    song.addPart(testPart);

    assertEquals("intro", song.getParts().get(0).getName());
  }
}
