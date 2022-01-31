package Punches;

import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import net.miginfocom.swing.MigLayout;

/**
 * @author Vince Aquilina
 * @version Sun 30 Jan 2022 10:00:30 PM
 *
 * A Part component that can be dragged to reorder in the SongPanel.
 *
 * Adapted from tutorial @ 
 *  https://www.codeproject.com/articles/116088/draggable-components-in-java-swing
 */
public class DraggablePart extends DraggableComponent implements ImageObserver 
{
  protected Image sheetImage;                                 // sheet music snippet associated with the loaded Part
  private Dimension size;                                     // size of part component
  private Part part;                                          // Part data for this component
  private PartNotePane notePane;                              // pane in which Part notes are contained

  /**
   * Constructs the component with the given Part data
   *
   * @param part - the Part object assigned to the component
   */
  public DraggablePart(Part part)
  {
    this.part = part;
    size = new Dimension(100, 100);
    notePane = new PartNotePane();
    this.add(notePane);
    setLayout(new MigLayout());
    setBackground(new Color(0xEEEEEE));
  }

  /**
   * Defines how the component is to be painted.
   *
   * @param g
   */
  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2d = (Graphics2D)g;
    g2d.clearRect(0, 0, getWidth(), getHeight());

    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, getWidth(), getHeight());
  }

  /**
   * Checks if the image is fully loaded
   *
   * @param img - target image
   * @param infoflags - is equal to ALLBITS when image is loaded
   * @param x - x coordinate
   * @param y - y coordinate
   * @param w - width
   * @param h - height
   * @return TRUE if image can generate events, otherwise FALSE
   */
  @Override
  public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h)
  {
    if (infoflags == ALLBITS) {
      repaint();
      return false;
    }
    return true;
  }

  /**
   * Get the part assigned to this component
   *
   * @return the value of part
   */
  public Part getPart()
  {
    return part;
  }

  /**
   * Assign a new part to this component
   *
   * @param part - the new part to be assigned
   */
  public void setPart(Part part)
  {
    this.part = part;
  }
}
