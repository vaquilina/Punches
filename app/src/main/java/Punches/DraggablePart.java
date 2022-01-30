package Punches;

import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * @author Vince Aquilina
 * @version Wed 19 Jan 2022 04:58:32 PM
 */
public class DraggablePart extends DraggableComponent implements ImageObserver 
{
  protected Image sheetImage;
  private boolean autosize;
  private Dimension autosizeDimension;
  private Part part;
  private PartNotePane notePane;

  public DraggablePart(Part part)
  {
    this.part = part;
    autosize = false;
    autosizeDimension = new Dimension(0, 0);
  }

  protected void paintComponent(Graphics g)
  {
  }

  public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h)
  {
    return false;
  }
}
