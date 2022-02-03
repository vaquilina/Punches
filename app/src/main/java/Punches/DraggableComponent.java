package Punches;

import javax.swing.JComponent;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
/**
 * @author Vince Aquilina
 * @version Mon 31 Jan 2022 05:35:03 PM
 *
 * A component with drag and drop capabilities.
 *
 * Adapted from tutorial @ 
 *  https://www.codeproject.com/articles/116088/draggable-components-in-java-swing
 *
 * TODO: implement grid, so drag is for reordering instead of free movement
 */
public class DraggableComponent extends JComponent
{
  private boolean draggable = true;         // draggable flag 
  protected Point anchorPoint;              // "handle" where component is grabbed; (mouse coordinates)
  protected boolean overbearing = false;    // indicates whether component is overlapping another

  // cursor while dragging
  protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

  /**
   * Constructs a default DraggableComponent
   */
  public DraggableComponent()
  {
    addDragListeners();
    setOpaque(true);
    setBackground(new Color(240, 240, 240));
  }

  /**
   * Adds a listener to MouseMotion on an object. (begin dragging)
   */
  private void addDragListeners()
  {
    final DraggableComponent handle = this; // reference saved so it can be used in following section

    addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseMoved(MouseEvent e)
      {
        anchorPoint = e.getPoint();
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
      }

      @Override
      public void mouseDragged(MouseEvent e)
      {
        int anchorX = anchorPoint.x;
        int anchorY = anchorPoint.y;

        Point parentOnScreen = getParent().getLocationOnScreen();
        Point mouseOnScreen = e.getLocationOnScreen();
        Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
        setLocation(position);

        // change z-buffer if overbearing
        if (overbearing) {
          getParent().setComponentZOrder(handle, 0);
          repaint();
        }
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
        //int anchorX = anchorPoint.x;
        int anchorY = anchorPoint.y;

        //Point parentOnScreen = getParent().getLocationOnScreen();
        Point mouseOnScreen = e.getLocationOnScreen();
        Point currentPosition = handle.getLocationOnScreen();

        Point position;
        if ((mouseOnScreen.y > anchorY + 200)) {
          position = new Point(currentPosition.x, currentPosition.y + 400);
        }
        else if ((mouseOnScreen.y < anchorY - 200)) {
          position = new Point(currentPosition.x, currentPosition.y - 400);
        }
        else {
          position = new Point(0, 0);
        }
        setLocation(position);
      }
    });
  }

  /**
   * Removes a listener to MouseMotion on an object. (freeze component)
   */
  private void removeDragListeners()
  {
    for (MouseMotionListener listener : this.getMouseMotionListeners()) {
      removeMouseMotionListener(listener);
    }
    setCursor(Cursor.getDefaultCursor());
  }

  /**
   * Get the value of draggable
   *
   * @return the value of draggable
   */
  public boolean isDraggable()
  {
    return draggable;
  }

  /**
   * Set the value of draggable
   *
   * @param draggable - the new value of draggable
   */
  public void setDraggable(boolean draggable)
  {
    this.draggable = draggable;

    if (draggable) {
      addDragListeners();
    }
    else {
      removeDragListeners();
    }
  }

  ///**
  // * Get the value of draggingCursor
  // *
  // * @return the value of draggingCursor
  // */
  //public Cursor getDraggingCursor()
  //{
  //  return draggingCursor;
  //}

  ///**
  // * Set the value of draggingCursor
  // *
  // * @param draggingCursor - the new value of draggingCursor
  // */
  //public void setDraggingCursor(Cursor draggingCursor)
  //{
  //  this.draggingCursor = draggingCursor;
  //}

  /**
   * Get the value of overbearing
   *
   * @return the value of overbearing
   */
  public boolean isOverbearing()
  {
    return overbearing;
  }

  /**
   * Set the value of overbearing
   */
  public void setOverbearing(boolean overbearing)
  {
    this.overbearing = overbearing;
  }

  /**
   * Defines how the component is to be painted.
   */
  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    if (isOpaque()) {
      g.setColor(getBackground());
      g.fillRect(0, 0, getParent().getWidth(), 200);
    }
  }
}
