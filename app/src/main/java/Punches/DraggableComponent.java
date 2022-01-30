package Punches;

import javax.swing.JComponent;
import java.awt.Point;
import java.awt.Cursor;

/**
 * @author Vince Aquilina
 * @version Wed 19 Jan 2022 04:54:12 PM
 *
 */
public class DraggableComponent extends JComponent
{
  protected boolean draggable;
  protected boolean overbearing;
  protected Point anchorPoint;
  protected Cursor draggingCursor;

  public DraggableComponent()
  {
    draggable = true;
    overbearing = false;
    draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
  }
}
