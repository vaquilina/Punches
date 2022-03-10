package Punches;

import java.awt.Color;
import java.awt.Rectangle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.softsmithy.lib.swing.customizer.JCustomizer;
import org.softsmithy.lib.swing.customizer.event.CustomizerListener;
import org.softsmithy.lib.swing.customizer.event.CustomizerEvent;
/**
 * @author Vince Aquilina
 * @version 03/10/22
 *
 * Extended JCustomizer with custom behaviour.
 */
class PartPanelCustomizer extends JCustomizer implements CustomizerListener
{
  /** This column */
  public static final int COLUMN = 0;

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

  //DEBUG
  private boolean debugging;

  /**
   * Construct a default PartPanelCustomizer
   */
  public PartPanelCustomizer(PartPanel partPanel) 
  {
    super(partPanel);

    //DEBUG {{{
    debugging = true;
    //////////// }}}

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
   */
  public void setStoredYPos(int yPos) 
  {
    this.yPos = yPos;
  }

  /**
   * Set the value for stored width
   */
  public void setStoredWidth(int width) 
  {
    this.width = width;
  }

  /**
   * Set the value for stored width
   */
  public void setStoredHeight(int height) 
  {
    this.height = height;
  }

  /**
   * Get the stored y position
   */
  public int getStoredYPos() 
  {
    return yPos;
  }

  /**
   * Set the row span
   */
  public void setRowSpan(int rowSpan)
  {
    this.rowSpan = rowSpan;
  }

  /**
   * Get the stored width
   *
   * @return the stored width
   */
  public int getStoredWidth() 
  {
    return width;
  }

  /**
   * Get the stored height
   *
   * @return the stored height
   */
  public int getStoredHeight() 
  {
    return height;
  }

  /**
   * Get the row
   *
   * @return the row
   */
  public int getRow()
  {
    return row;
  }

  /**
   * Get the row span
   * 
   * @return the row span
   */
  public int getRowSpan()
  {
    return rowSpan;
  }

  /**
   * Get the PartPanel
   */
  public PartPanel getPartPanel()
  {
    return partPanel;
  }

  ////////////////////
  // HELPER METHODS //
  ////////////////////
  
  /**
   * Register a PropertyChangeListener
   */
  public void registerPropertyChangeListener()
  {
    this.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent e) {}
    });
  }

  ////////////////////////////////
  // CustomizerListener Methods //
  ////////////////////////////////
  
  @Override
  public void customizerResetBoundsRel(CustomizerEvent e) {
    // TODO: grab indexes of overlapped customizers in order to reorder parts
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
    setX(COLUMN);
    yPos = getY();
    row = yPos / 200;
    rowSpan = getHeight() / 200;

    //DEBUG {{{
    if (debugging) {
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

