package Punches;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Encapsulates the outline of a song, made up of Parts and metadata.
 *
 * @author Vince Aquilina
 * @version 04/11/22
 */
public class Song implements Serializable
{
  private final Logger logger = LoggerFactory.getLogger(Song.class);

  /** The parts that comprise the Song */
  private List<Part> parts;
  /** The song's title */
  private String title;
  /** The song's time signature */
  private TimeSignature signature;
  /** The song's tempo */
  private int bpm;

  /**
   * Contruct a default Song
   */
  public Song()
  {
    title = "";
    signature = new TimeSignature(4, BeatValue.QUARTER);
    bpm = 120;
    parts = new ArrayList<Part>();
    parts.add(new Part());
  }

  /**
   * Copy Constructor
   * @param song the Song object with which to initialize the new object
   */
  public Song(Song song)
  {
    title = song.title;
    signature = song.signature;
    bpm = song.bpm;
    parts = song.parts;
  }

  /**
   * Construct a song with given values
   * @param parts the collection of parts that comprise the song
   * @param title the song title
   * @param signature the TimeSignature of the song
   * @param bpm the song's tempo (beats per minute)
   */
  public Song(List<Part> parts, String title, TimeSignature signature, int bpm)
  {
    this.parts = parts;
    this.title = title;
    this.signature = signature;
    this.bpm = bpm;
  }

  /**
   * Add an existing part to the Song (Useful when putting/pasting)
   * @param part the existing part
   */
  public void addPart(Part part)
  {
    parts.add(part);
  }

  /**
   * Clears the list of Parts
  public void clearParts()
  {
    parts.clear();
  }

  /**
   * Assign a new collection of parts to the Song
   * @param parts the new list of parts
   */
  public void setParts(List<Part> parts)
  {
    this.parts = parts;
  }

  /**
   * Set the song title
   * @param title the song title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   * Set the time signature
   * @param signature the time signature
   */
  public void setSignature(TimeSignature signature)
  {
    this.signature = signature;
  }

  /**
   * Set the song's tempo (beats per minute)
   * @param bpm the song's tempo (bpm) 
   */
  public void setBpm(int bpm)
  {
    this.bpm = bpm;
  }

  /**
   * Get the list of parts
   * @return the list of parts
   */
  public List<Part> getParts()
  {
    return parts;
  }

  /**
   * Get the song title
   * @return the song title
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Get the time signature
   * @return the time signature
   */
  public TimeSignature getSignature()
  {
    return signature;
  }

  /**
   * Get the song's tempo (beats per minute)
   * @return the song's tempo (bpm)
   */
  public int getBpm()
  {
    return bpm;
  }

  /**
   * Tell Parts about their position in the list
   */
  public void refreshIndices()
  {
    ListIterator<Part> itParts = parts.listIterator();
    while (itParts.hasNext()) {
      Part part = itParts.next();
      part.setIndex(parts.indexOf(part));

      //DEBUG {{{
      logger.info("refreshed index: [{}] {}", part.getIndex(), part.getName()); 
      //////////// }}}
    }
  }

  /**
   * Produce a String representation of the Song's metadata
   * @return a String representation of the Song's metadata
   */
  @Override
  public String toString()
  {
    String songString = 
      "Title:\t" + this.title + "\n" +
      "# of parts:\t" + this.parts.size() + "\n" +
      "Tempo:\t" + this.bpm + "\n" +
      "Time signature:\t" + this.signature;

    return songString;
  }
}

