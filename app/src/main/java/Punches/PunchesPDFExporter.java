package Punches;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
/**
 * Allows for exporting of Punches data in PDF format.
 *
 * @author Vince Aquilina
 * @version 03/17/22
 *
 * TODO: write tests
 */
public class PunchesPDFExporter
{
  /** The Song data */
  Song song;
  /** The raw HTML */
  String rawHTML;

  /**
   * Construct an initialized PunchesPDFExporter
   *
   * @param song the Song data
   */
  public PunchesPDFExporter(Song song)
  {
    this.song = song;
  }

  /**
   * Prepare HTML data from Song data
   *
   * @throws IOException
   */
  public void prepare() throws IOException
  {
    String outline = Files.readString(
        Paths.get(PunchesPDFExporter.
          class.
          getResource("/templates/outline.html").getPath()));
    String rowOutline = Files.readString(
        Paths.get(PunchesPDFExporter.
          class.
          getResource("/templates/row.html").getPath()));

    String songTitle = song.getTitle();
    String timeSignature = song.getSignature().toString();
    String tempo = String.valueOf(song.getBpm());

    outline = outline.replace("songTitle", songTitle);
    outline = outline.replace("timeSignature", timeSignature);
    outline = outline.replace("tempo", tempo);

    StringBuilder sbParts = new StringBuilder("");
    for (Part part : song.getParts()) {
      String partName = part.getName();
      String partLength = String.valueOf(part.getLengthInBars());

      String sheetSnippet = "";
      if (part.getSheetSnippet() != null) {
        //TODO decide on path for snippets
        sheetSnippet = "<img src='" + part.getIndex() + "-sheet.png'>";
      }

      StringBuilder sbTabSnippet = new StringBuilder("");
      if (part.getTabSnippet() != null) {
        String[] tabSnippetLines = part.getTabSnippet();

        for (String line : tabSnippetLines) {
          sbTabSnippet.append(line + "\n");
        }
      }
      String tabSnippet = new String(sbTabSnippet);

      String notes = part.getNotes();

      // construct part
      String row = new String(rowOutline);
      row = row.replace("partName", partName);
      row = row.replace("partLength", partLength);
      row = row.replace("sheetSnippet", sheetSnippet);
      row = row.replace("tabSnippet", tabSnippet);
      row = row.replace("notes", notes);

      sbParts.append(row);
    }
    String parts = new String(sbParts);
    rawHTML = outline.replace("parts", parts);

  }

  /**
   * Convert HTML to PDF data and write to disk
   *
   * @param file the file to write to
   * @throws IOException
   * @throws HTMLNotRenderedException if a call to prepare() is not made first
   * @throws Exception in case of parsing errors
   *
   * TODO: image processing
   */
  public void exportPDF(File file) throws IOException, HTMLNotRenderedException,
         Exception
  {
    if (rawHTML == null) {
      throw new HTMLNotRenderedException("must call prepare() first");
    }

    try (OutputStream os = new FileOutputStream(file)) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withHtmlContent(rawHTML, file.getPath());
      builder.toStream(os);
      builder.run();
    }
  }

  public class HTMLNotRenderedException extends Exception
  {
    public HTMLNotRenderedException(String errorMessage) {
      super(errorMessage);
    }
  }
}
