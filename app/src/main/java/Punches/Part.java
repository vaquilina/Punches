package Punches;

import java.awt.Image;
/**
 * @author Vince Aquilina
 * @version Wed 19 Jan 2022 04:54:33 PM
 */
public class Part
{
  private String name;
  private int lengthInBars;
  private String notes;
  private Image sheetSnippet;
  private String[] tabSnippet;

  /**
   * Constructs a default Part
   */
  public Part()
  {
    name = "intro";
    lengthInBars = 4;
    this.notes = "";
  }

  /**
   * @param name - name of section
   * @param lengthInBars - length of the part, in bars
   * @param notes - sidenotes about the part
   * Constructs a Part with the given metadata
   */
  public Part(String name, int lengthInBars, String notes)
  {
    this.name = name;
    this.lengthInBars = lengthInBars;
    this.notes = notes;
  }
}
