package Punches;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.softsmithy.lib.swing.customizer.JCustomizer;
import org.softsmithy.lib.swing.customizer.event.CustomizerListener;
import org.softsmithy.lib.swing.customizer.event.CustomizerEvent;
/**
 * Extended JCustomizer with custom behaviour.
 *
 * @author Vince Aquilina
 * @version 03/13/22
 *
 * TODO: Logic for re-ordering parts 
 */
class PartPanelCustomizer extends JCustomizer implements CustomizerListener
{
  /** y position */
  private int yPos;
  /** Last known width of rectangle */
  private int width;
  /** Last known height of rectangle */
  private int height;
  /** Last known row */
  private int row;
  /** Last known rowSpan */
  private int rowSpan;
  /** The wrapped PartPanel */
  private PartPanel partPanel;

  //DEBUG {{{
  private boolean debugging = false;
  //////////// }}}

  /**
   * Construct a default PartPanelCustomizer
   *
   * @param partPanel - the PartPanel to wrap
   */
  public PartPanelCustomizer(PartPanel partPanel) 
  {
    super(partPanel);

    this.partPanel = partPanel;

    yPos = 0;
    width = 0;
    height = 0;
    row = 0;
    rowSpan = 1;

    // listen for move/resize events
    addCustomizerListener(this);
  }

  /**
   * Set the value for stored y position
   *
   * @param yPos - the last known y position of the
   * upper left corner of the component
   */
  public void setStoredYPos(int yPos) 
  {
    this.yPos = yPos;
  }

  /**
   * Set the value for stored width
   *
   * @param width - the last known width of the component
   */
  public void setStoredWidth(int width) 
  {
    this.width = width;
  }

  /**
   * Set the value for stored width
   *
   * @param height - the last known height of the component
   */
  public void setStoredHeight(int height) 
  {
    this.height = height;
  }

  /**
   * Set the row span
   *
   * @param rowSpan - the number of rows this component occupies
   */
  public void setRowSpan(int rowSpan)
  {
    this.rowSpan = rowSpan;
  }

  /**
   * Get the stored y position
   *
   * @return the last known y position
   */
  public int getStoredYPos() 
  {
    return yPos;
  }

  /**
   * Get the stored width
   *
   * @return the last known width of the component
   */
  public int getStoredWidth() 
  {
    return width;
  }

  /**
   * Get the stored height
   *
   * @return the last known height of the component
   */
  public int getStoredHeight() 
  {
    return height;
  }

  /**
   * Get the row
   *
   * @return the first row on which this component resides
   */
  public int getRow()
  {
    return row;
  }

  /**
   * Get the row span
   * 
   * @return the number of rows this component occupies
   */
  public int getRowSpan()
  {
    return rowSpan;
  }
  
  ///**
  // * Get the PartPanel
  // *
  // * @return the PartPanel this component wraps
  // */
  //public PartPanel getPartPanel()
  //{
  //  return partPanel;
  //}

  /**
   * Register a PropertyChangeListener to this component
   */
  public void registerPropertyChangeListener()
  {
    this.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent e) {}
    });
  }

  /**
   * Register a ComponentListener to this component
   */
  public void registerComponentListener()
  {
    this.addComponentListener(new ComponentListener() {
      @Override
      public void componentHidden(ComponentEvent e) {}
      @Override
      public void componentShown(ComponentEvent e) {}
      @Override
      public void componentMoved(ComponentEvent e) {}
      @Override
      public void componentResized(ComponentEvent e) {}
    });
  }

  ////////////////////////////////
  // CustomizerListener Methods //
  ////////////////////////////////
  
  @Override
  public void customizerResetBoundsRel(CustomizerEvent e) {
    if (getStateManager().getMoveState().isDragging()) {
      getParent().setComponentZOrder(this, 0);
    }

    width = getWidth();
    height = getHeight();

    //DEBUG {{{
    if (debugging) {
      System.out.println("dragging \"" + partPanel.getPart().getName() + "\"");
      System.out.println("\tpos: " + "[" + getX() + ", " + getY() + "]");
      System.out.println("\tdim: " + "[" + width + " x " + height + "]");
      System.out.println("\tintersects: " +
        getParentCustomizerPane().
        getIntersectedCustomizers(getVisibleRect()).length);
    }
    //////////// }}}
  }

  @Override
  public void customizerReshapeRel(CustomizerEvent e) {
    setX(0);
    rowSpan = getHeight() / 200;

    int intersects = getParentCustomizerPane().
        getIntersectedCustomizers(getVisibleRect()).length;
    if (intersects > 1 || row == 0 && intersects > 0) {
      setY(yPos);
    }
    yPos = getY();
    row = yPos / 200;

    //DEBUG {{{
    if (debugging) {
      System.out.println(this.getClass());
      System.out.println("\t" + partPanel.getPart().getName());
      System.out.println("\ty:" + getY());
      System.out.println("\tw:" + getWidth());
      System.out.println("\th:" + getHeight());
      System.out.println("\tr:" + row);
      System.out.println("\ts:" + rowSpan);
    }
    //////////// }}}
  }
}

