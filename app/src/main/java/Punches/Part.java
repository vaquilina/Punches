package Punches;

import java.awt.Image;
/**
 * @author Vince Aquilina
 * @version 03/09/22
 */
public class Part
{
  private String name;
  private String notes;
  private int index;   // position in part list
  private int lengthInBars;

  private String[] tabSnippet;
  private Image sheetSnippet;

  /**
   * Constructs a default Part
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
   * Constructs a Part with the given metadata
   *
   * @param name - name of section
   * @param lengthInBars - length of the part, in bars
   * @param notes - sidenotes about the part
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
   * Constructs a Part with the given metadata
   *
   * @param name - name of section
   * @param lengthInBars - length of the part, in bars
   * @param notes - sidenotes about the part
   * @param sheetSnippet - the sheet music snippet
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
   * Constructs a Part with the given metadata
   *
   * @param name - name of section
   * @param lengthInBars - length of the part, in bars
   * @param notes - sidenotes about the part
   * @param tabSnippet - the tabulature snippet
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
   * Set the name of the part
   *
   * @param the part name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the length of the part
   *
   * @param the length of the part, in bars
   */
  public void setLengthInBars(int lengthInBars)
  {
    this.lengthInBars = lengthInBars;
  }

  /**
   * Set the part notes
   *
   * @param the part notes
   */
  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  /**
   * Set the sheet music snippet
   *
   * @param the sheet music snippet
   */
  public void setSheetSnippet(Image sheetSnippet)
  {
    this.sheetSnippet = sheetSnippet;
  }

  /**
   * Set the tabulature snippet
   *
   * @param the tabulature snippet
   */
  public void setTabSnippet(String[] tabSnippet)
  {
    this.tabSnippet = tabSnippet;
  }

  /**
   * Set the position in the part list
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
   */
  public int getIndex()
  {
    return index;
  }
}
