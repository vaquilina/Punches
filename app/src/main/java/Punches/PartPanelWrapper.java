package Punches;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;

import java.io.Serializable;
/**
 * Binds a Part to PartPanel to a PartPanelCustomizer.
 *
 * @author Vince Aquilina
 * @version 03/17/22
 *
 * TODO write tests
 */
public class PartPanelWrapper
{
  /** The PartPanel */
  private PartPanel panel;
  /** The PartPanelCustomizer */
  private PartPanelCustomizer customizer;  
  /** The Part */
  private Part part;
  /** The components bounds */
  private Rectangle bounds;

  private static final int X = 0;

  // Colors
  private Color panelGray = new Color(0xDDDDDD);
  private Color apricot = new Color(0xFFCCB3);

  /**
   * Constructs a bound PartPanelWrapper
   *
   * @param part the Part to wrap
   */
  public PartPanelWrapper(Part part)
  {
    this.part = part;
    panel = new PartPanel(part);
    customizer = new PartPanelCustomizer(panel);

    makeEditable();
    customizer.registerPropertyChangeListener();
  }

  /**
   * <p>Add "double-click to edit" functionality to PartPanels.<br />
   * "Editable" panels change to an apricot color.</p>
   */
  private void makeEditable()
  {
    customizer.addActionListener(new ActionListener() {
      boolean editing;
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!editing) {
          customizer.getComponent().setBackground(apricot);
          customizer.setComponentZOrder(customizer.getComponent(1), 0);
          customizer.getStateManager().setStateNormal();
          editing = true;
        }
        else {
          customizer.getComponent().setBackground(panelGray);
          customizer.setComponentZOrder(customizer.getComponent(1), 0);
          customizer.getStateManager().setStateMove();
          editing = false;
        }
      }
    });
  }

  /**
   * Capture the panel's current position in the SongPanel
   */
  public void storePosition()
  {
    int y = customizer.getY();
    int w = customizer.getWidth();
    int h = customizer.getHeight();

    customizer.setStoredYPos(y);
    customizer.setStoredWidth(w);
    customizer.setStoredHeight(h);

    bounds = new Rectangle(X, y, w, h);
  }

  /**
   * Set the PartPanel
   *
   * @param panel the PartPanel
   */
  public void setPartPanel(PartPanel panel)
  {
    this.panel = panel;
  }

  /**
   * Set the PartPanelCustomizer
   *
   * @param customizer the PartPanelCustomizer
   */
  public void setPartPanelCustomizer(PartPanelCustomizer customizer)
  {
    this.customizer = customizer;
  }

  /**
   * Set the Part
   *
   * @param part the Part
   */
  public void setPart(Part part)
  {
    this.part = part;
  }

  /**
   * Get the PartPanel
   *
   * @return the PartPanel
   */
  public PartPanel getPartPanel()
  {
    return panel;
  }

  /**
   * Get the PartPanelCustomizer
   *
   * @return the PartPanelCustomizer
   */
  public PartPanelCustomizer getPartPanelCustomizer()
  {
    return customizer;
  }

  /**
   * Get the Part
   *
   * @return the Part
   */
  public Part getPart()
  {
    return part;
  }

  /**
   * Get the stored bounds
   *
   * @return the stored bounds
   */
  public Rectangle getStoredPosition()
  {
    return bounds;
  }
}

