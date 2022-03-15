package Punches;

import java.io.File;
import java.io.IOException;

import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLWriter;
/**
 * Allows for exporting of Punches data in PDF format.
 *
 * @author Vince Aquilina
 * @version 03/15/22
 *
 * TODO: write tests
 */
public class PunchesPDFExporter
{
  /** The Song data */
  Song song;
  /** The prepared HTML */
  // some html container

  /**
   * Construct an initialized PunchesPDFExporter
   *
   * @param song - the Song data
   */
  public PunchesPDFExporter(Song song)
  {
    this.song = song;
  }

  /**
   * Prepare HTML data from Song data
   */
  public void prepare()
  {
    //TODO layout page, generate html
  }

  /**
   * Convert HTML to PDF data and write to disk
   *
   * @param file - the file to write to
   */
  public void exportPDF(File file) throws IOException
  {
    //TODO render html as pdf and write to disk
  }
}
