package Punches;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * @author Vince Aquilina
 * @version Thu 17 Feb 2022
 *
 * A class representing the outline of a song, made up of Parts and metadata.
 */
public class Song implements Serializable
{
  private List<Part> parts;             // the parts that comprise the song
  private String title;                 // the song's title
  private TimeSignature signature;      // the song's time signature
  private int bpm;                      // the song's tempo

  /**
   * Contructs a default Song
   */
  public Song()
  {
    title = "";
    signature = new TimeSignature(4, BeatValue.QUARTER);
    bpm = 120;
    parts = new ArrayList<Part>();
  }

  /**
   * Constructs a song with given values
   *
   * @param parts - the collection of parts that comprise the song
   * @param title - the song title
   * @param signature - the TimeSignature of the song
   * @param bpm - the song's tempo (beats per minute)
   */
  public Song(List<Part> parts, String title, TimeSignature signature, int bpm)
  {
    this.parts = parts;
    this.title = title;
    this.signature = signature;
    this.bpm = bpm;
  }

  /**
   * Add a new default part to the Song
   */
  public void addNewPart()
  {
    Part newPart = new Part();
    parts.add(newPart);
  }

  /**
   * Add an existing part to the Song (Useful when pasting)
   *
   * @param part - the existing part
   */
  public void addPart(Part part)
  {
    parts.add(part);
  }

  /**
   * Assign a new collection of parts to the Song
   *
   * @param parts - the new list of parts
   */
  public void setParts(List<Part> parts)
  {
    this.parts = parts;
  }

  /**
   * Set the song title
   *
   * @param title - the song title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   * Set the time signature
   *
   * @param the time signature
   */
  public void setSignature(TimeSignature signature)
  {
    this.signature = signature;
  }

  /**
   * Set the song's tempo (beats per minute)
   *
   * @param bpm - the song's tempo (bpm) 
   */
  public void setBpm(int bpm)
  {
    this.bpm = bpm;
  }

  /**
   * Get the list of parts
   *
   * @return the list of parts
   */
  public List<Part> getParts()
  {
    return parts;
  }

  /**
   * Get the song title
   *
   * @return the song title
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Get the time signature
   *
   * @return the time signature
   */
  public TimeSignature getSignature()
  {
    return signature;
  }

  /**
   * Get the song's tempo (beats per minute)
   *
   * @return the song's tempo (bpm)
   */
  public int getBpm()
  {
    return bpm;
  }
}
