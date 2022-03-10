package Punches;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import org.softsmithy.lib.swing.customizer.JCustomizer;
import org.softsmithy.lib.swing.customizer.event.CustomizerListener;
import org.softsmithy.lib.swing.customizer.event.CustomizerEvent;
/**
 * @author Vince Aquilina
 * @version 03/09/22
 *
 * Extended JCustomizer with custom behaviour.
 */
class PartPanelCustomizer extends JCustomizer implements CustomizerListener
{
  public static final int XPOS = 0;     // parent SongPanel uses a single column

  private int yPos;
  private int width;
  private int height;

  // Flags
  private boolean overlapping;
  private boolean debugging;

  /**
   * Construct a default PartPanelCustomizer
   */
  public PartPanelCustomizer(JComponent component) {
    super(component);

    //DEBUG
    debugging = false;

    yPos = 0;
    width = getWidth();
    height = getHeight();

    addCustomizerListener(this);

    //registerPropertyChangeListener();
  }

  /**
   * Set the value for stored y position
   */
  public void setStoredYPos(int yPos) {
    this.yPos = yPos;
  }

  /**
   * Set the value for stored width
   */
  public void setStoredWidth(int width) {
    this.width = width;
  }

  /**
   * Set the value for stored width
   */
  public void setStoredHeight(int height) {
    this.height = height;
  }

  /**
   * Get the stored y position
   */
  public int getStoredYPos() {
    return yPos;
  }

  /**
   * Get the stored width
   */
  public int getStoredWidth() {
    return width;
  }

  /**
   * Get the stored height
   */
  public int getStoredHeight() {
    return height;
  }

  ////////////////////
  // HELPER METHODS //
  ////////////////////
  
  /**
   * Register a PropertyChangeEvent
   */
  private void registerPropertyChangeListener()
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
    //DEBUG {{{
    if (debugging) {
      System.out.println("\npps: " + "[" + getX() + ", " + getY() + "]");
      System.out.println("intersect? " +
        getParentCustomizerPane().
        getIntersectedCustomizers(getVisibleRect()).length);
      System.out.println("dragging? " +
          getStateManager().getMoveState().isDragging());
    }
    //////////// }}}
  }

  @Override
  public void customizerReshapeRel(CustomizerEvent e) {
    setX(XPOS);
    //DEBUG {{{
    if (debugging) {
      System.out.println("\nx:" + getX());
      System.out.println("y:" + getY());
      System.out.println("w:" + getWidth());
      System.out.println("h:" + getHeight());
    }
    //////////// }}}
  }
}
