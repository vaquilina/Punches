package Punches;

import javax.swing.JTextPane;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import com.github.rjeschke.txtmark.Processor;
/**
 * @author Vince Aquilina
 * @version Thu 03 Feb 2022 08:57:00 PM
 *
 * A JTextPane designed to contain Part notes.
 *
 * TODO: attribute txtmark
 *
 */
public class PartNotePane extends JTextPane
{
  private String plainText;

  /**
   * Constructs a default PartNotePane
   */
  public PartNotePane()
  {
    plainText = "";

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
   * Renders markdown text
   *
   * @param md - the text to render
   */
  public void renderMarkdown(String md)
  {
    plainText = md;
    String html = Processor.process(md);
    setContentType("text/html");
    setText(html);
    setEditable(false);
    getCaret().setVisible(false);
    
  }
}
