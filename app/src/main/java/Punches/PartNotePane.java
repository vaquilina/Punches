package Punches;

import javax.swing.JTextPane;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.github.rjeschke.txtmark.Processor;
/**
 * A JTextPane designed to contain Part notes.
 *
 * @author Vince Aquilina
 * @version 03/17/22
 *
 * TODO: attribute txtmark
 *
 */
public class PartNotePane extends JTextPane
{
  /** The notes as raw text */
  private String plainText;
  /** The notes as raw html */
  private String html;

  /**
   * Construct a default PartNotePane
   */
  public PartNotePane()
  {
    plainText = "";
    html = "";

    /*
     * Press ENTER to render markdown
     */
    this.addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          if (e.getModifiersEx() != 
              KeyEvent.SHIFT_DOWN_MASK && getContentType() == "text/plain") {
            renderMarkdown(getText());
          }
          else if (getContentType() != "text/html") {
            int pos = getCaretPosition();
            StringBuffer buf = new StringBuffer(getText());
            buf.insert(pos, '\n');
            plainText = buf.toString();
            setText(plainText);
            setCaretPosition(pos + 1);
          }
        }
      }
      @Override
      public void keyReleased(KeyEvent e) {}
      @Override
      public void keyTyped(KeyEvent e) {}
    });

    /*
     * LEFT CLICK to switch to plaintext entry
     */
    this.addMouseListener(new MouseListener() {
      @Override
      public void mouseEntered(MouseEvent e) {}
      @Override
      public void mouseExited(MouseEvent e) {}
      @Override
      public void mousePressed(MouseEvent e) {}
      @Override
      public void mouseReleased(MouseEvent e) {}
      @Override
      public void mouseClicked(MouseEvent e) {
        if (getContentType() == "text/html") {
          setContentType("text/plain");
          setText(plainText);
          setEditable(true);
          getCaret().setVisible(true);
        }
      }
    });
  }

  /**
   * Render markdown/html text
   *
   * @param md - the text to render
   */
  public void renderMarkdown(String md)
  {
    plainText = md;
    html = Processor.process(md);
    setContentType("text/html");
    setText(html);
    setEditable(false);
    getCaret().setVisible(false);
  }

  /**
   * Get the part notes
   *
   * @return the part notes
   */
  public String getPlainText()
  {
    return plainText;
  }

  /**
   * Get the processed html code
   *
   * @return the html code
   */
  public String getHTML()
  {
    return html;
  }
}

