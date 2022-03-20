package Punches;

import java.awt.Image;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Encapsulates information about sections of a Song.
 *
 * @author Vince Aquilina
 * @version 03/17/22
 */
public class Part implements Serializable
{
  /*
   * TODO: chain constuctors
   */

  private final Logger logger = LoggerFactory.getLogger(Part.class);

  /** Part name */
  private String name;
  /** Part notes */
  private String notes;
  /** Part's position in the Song */
  private int index;
  /** Part length in bars */
  private int lengthInBars;

  /** Tabulature snippet (line-wise) */
  private String[] tabSnippet;
  /** Sheet music snippet */
  private Image sheetSnippet;

  /**
   * Construct a default Part
   */
  public Part()
  {
    name = "intro";
    lengthInBars = 4;
    this.notes = "";
    sheetSnippet = null;
    tabSnippet = null;
  }

  /**
   * Construct a Part with the given metadata
   *
   * @param name name of section
   * @param lengthInBars length of the part, in bars
   * @param notes sidenotes about the part
   */
  public Part(String name, int lengthInBars, String notes)
  {
    this.name = name;
    this.lengthInBars = lengthInBars;
    this.notes = notes;
    sheetSnippet = null;
    tabSnippet = null;
  }

  /**
   * Construct a Part with the given metadata, with a sheet music snippet
   *
   * @param name name of section
   * @param lengthInBars length of the part, in bars
   * @param notes sidenotes about the part
   * @param sheetSnippet the sheet music snippet
   */
  public Part(String name, int lengthInBars, String notes, Image sheetSnippet)
  {
    this.name = name;
    this.lengthInBars = lengthInBars;
    this.notes = notes;
    this.sheetSnippet = sheetSnippet;
    tabSnippet = null;
  }

  /**
   * Construct a Part with the given metadata, with a tabulature snippet
   *
   * @param name name of section
   * @param lengthInBars length of the part, in bars
   * @param notes sidenotes about the part
   * @param tabSnippet the tabulature snippet
   */
  public Part(String name, int lengthInBars, String notes, String[] tabSnippet)
  {
    this.name = name;
    this.lengthInBars = lengthInBars;
    this.notes = notes;
    this.tabSnippet = tabSnippet;
    sheetSnippet = null;
  }

  /**
   * Construct a Part with all fields set
   *
   * @param name name of section
   * @param lengthInBars length of the part, in bars
   * @param notes sidenotes about the part
   * @param tabSnippet the tabulature snippet
   * @param sheetSnippet the sheet music snippet
   */
  public Part(String name, int lengthInBars, String notes,
      String[] tabSnippet, Image sheetSnippet)
  {
    this.name = name;
    this.lengthInBars = lengthInBars;
    this.notes = notes;
    this.tabSnippet = tabSnippet;
    this.sheetSnippet = sheetSnippet;
  }

  /**
   * Set the name of the part
   *
   * @param name the part name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the length of the part
   *
   * @param lengthInBars the length of the part, in bars
   */
  public void setLengthInBars(int lengthInBars)
  {
    this.lengthInBars = lengthInBars;
  }

  /**
   * Set the part notes
   *
   * @param notes the part notes
   */
  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  /**
   * Set the sheet music snippet
   *
   * @param sheetSnippet the sheet music snippet
   */
  public void setSheetSnippet(Image sheetSnippet)
  {
    this.sheetSnippet = sheetSnippet;
  }

  /**
   * Set the tabulature snippet
   *
   * @param tabSnippet the tabulature snippet
   */
  public void setTabSnippet(String[] tabSnippet)
  {
    this.tabSnippet = tabSnippet;
  }

  /**
   * Set the position in the part list
   *
   * @param index the Part's index in the parts array
   */
  public void setIndex(int index)
  {
    this.index = index;
  }

  /**
   * Get the part name
   *
   * @return the part name
   */
  public String getName()
  {
    return name;
  }

  /**
   * Get the length in bars
   *
   * @return the length of the part, in bars
   */
  public int getLengthInBars()
  {
    return lengthInBars;
  }

  /**
   * Get the part notes
   *
   * @return the part noteSs
   */
  public String getNotes()
  {
    return notes;
  }

  /**
   * Get the sheet music snippet
   *
   * @return the sheet music snippet
   */
  public Image getSheetSnippet()
  {
    return sheetSnippet;
  }

  /**
   * Get the tabulature snippet
   *
   * @return the tabulature snippet
   */
  public String[] getTabSnippet()
  {
    return tabSnippet;
  }

  /**
   * Get the position in the part list
   *
   * @return the Part's index in the parts array
   */
  public int getIndex()
  {
    return index;
  }
}

