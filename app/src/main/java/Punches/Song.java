package Punches;

import java.util.List;
import java.io.Serializable;

/**
 * @author Vince Aquilina
 * @version Wed 19 Jan 2022 07:57:16 PM
 *
 * A class representing the outline of a song, made up of Parts and metadata.
 */
public class Song implements Serializable
{
  private List<Part> parts;             // the parts that the song is comprised of
  private String title;                 // the song's title
  private TimeSignature signature;      // the song's time signature
  private int bpm;                      // the song's tempo

  /**
   * Contructs a default Song
   */
  public Song()
  {
    title = "";
    signature = new TimeSignature(4, 4);
    bpm = 120;
  }

  public void addPart()
  {
    Part newPart = new Part();
    parts.add(newPart);
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public void setSignature(TimeSignature signature)
  {
    this.signature = signature;
  }

  public void setBpm(int bpm)
  {
    this.bpm = bpm;
  }

  public List<Part> getParts()
  {
    return parts;
  }

  public String getTitle()
  {
    return title;
  }

  public TimeSignature getSignature()
  {
    return signature;
  }

  public int getBpm()
  {
    return bpm;
  }
}
