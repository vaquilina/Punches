package Punches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Vince Aquilina
 * @version 03/09/22
 *
 * Tests for the Song class.
 */
class SongTest
{
  private final Song song = new Song();

  /**
   * Default Song object should be created
   */
  @Test
  void defaultSongObjectShouldBeCreated()
  {
    assertEquals("", song.getTitle());
    assertEquals("4/4", song.getSignature().toString());
    assertEquals(120, song.getBpm());
    assertNotNull(song.getParts().get(0));
  }

  /**
   * Custom Song object should be created
   */
  @Test
  void customSongShouldBeCreated()
  {
    List<Part> parts = new ArrayList<Part>();
    parts.add(new Part());
    TimeSignature signature = new TimeSignature(6, BeatValue.EIGHTH);
    String title = "15 Step";
    int bpm = 80;

    Song newSong = new Song(parts, title, signature, bpm);

    assertFalse(newSong.getParts().isEmpty());
    assertEquals("6/8", newSong.getSignature().toString());
    assertEquals("15 Step", newSong.getTitle());
    assertEquals(80, newSong.getBpm());
  }

  /**
   * New Part should be added
   */
  @Test
  void newPartShouldBeAdded()
  {
    song.setParts(new ArrayList<Part>());
    song.addPart(new Part());

    assertFalse(song.getParts().isEmpty());
  }

  /**
   * Existing Part should be added
   */
  @Test
  void existingPartShouldBeAdded()
  {
    Part testPart = new Part("intro", 4, "count in 4");
    song.addPart(testPart);

    assertEquals("intro", song.getParts().get(0).getName());
  }
}
