package Punches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class PartTest
{
  ClassLoader loader = getClass().getClassLoader();
  Part part = new Part();

  @Test
  void defaultPartShouldBeCreated()
  {
    assertEquals("intro", part.getName());
    assertEquals(4, part.getLengthInBars());
    assertEquals("", part.getNotes());
    assertNull(part.getSheetSnippet());
    assertNull(part.getTabSnippet());
  }

  @Test
  void customPartWithoutSnippetShouldBeCreated()
  {
    String name = "verse";
    int lengthInBars = 8;
    String notes = "keep it cool";

    Part newPart = new Part(name, lengthInBars, notes);

    assertEquals("verse", newPart.getName());
    assertEquals(8, newPart.getLengthInBars());
    assertEquals("keep it cool", newPart.getNotes());
    assertNull(newPart.getSheetSnippet());
    assertNull(newPart.getTabSnippet());
  }

  @Test
  void customPartWithSheetSnippetShouldBeCreated() throws IOException
  {
    BufferedImage sheetSnippet = 
      ImageIO.read(
          new File(loader.getResource("samples/sample_sheet.png").getFile()));
    String name = "verse";
    int lengthInBars = 8;
    String notes = "keep it cool";

    Part newPart = new Part(name, lengthInBars, notes, sheetSnippet);

    assertEquals("verse", newPart.getName());
    assertEquals(8, newPart.getLengthInBars());
    assertEquals("keep it cool", newPart.getNotes());
    assertNotNull(newPart.getSheetSnippet());
    assertNull(newPart.getTabSnippet());
  }

  @Test
  void customPartWithTabSnippetShouldBeCreated() throws IOException
  {
    List<String> tabLines = new ArrayList<>();
    Scanner scn = 
      new Scanner(
          new File(loader.getResource("samples/sample_tab.txt").getFile()));
    while (scn.hasNextLine()) {
      tabLines.add(scn.nextLine());
    }

    String[] tabSnippet = new String[] {};
    tabSnippet = tabLines.toArray(tabSnippet);

    String name = "verse";
    int lengthInBars = 8;
    String notes = "keep it cool";

    Part newPart = new Part(name, lengthInBars, notes, tabSnippet);

    assertEquals("verse", newPart.getName());
    assertEquals(8, newPart.getLengthInBars());
    assertEquals("keep it cool", newPart.getNotes());
    assertNotNull(newPart.getTabSnippet());
    assertNull(newPart.getSheetSnippet());
  }
}
