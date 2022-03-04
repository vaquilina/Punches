package Punches;

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import net.miginfocom.swing.MigLayout;

import org.softsmithy.lib.swing.customizer.JCustomizer;

// TODO track cell size
//import org.softsmithy.lib.swing.customizer.layout.RelativeTableConstraints; 

/**
 * @author Vince Aquilina
 * @version 03/03/22
 *
 * A Part component that can be dragged to reorder in the SongPanel.
 *
 */
public class PartPanel extends JCustomizer
{
  protected Image sheetImage;           // sheet music snippet
  private Part part;                    // Part data for this component
  private PartNotePane notePane;        // notes pane
  private JPanel musicPanel;            // panel for sheet music/tab snippets

  /**
   * Constructs the component with the given Part data
   *
   * @param part - the Part object assigned to the component
   */
  public PartPanel(Part part)
  {
    this.part = part;
    setLayout(new MigLayout("Insets 5"));
    setBackground(new Color(0xDDDDDD));
    setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
  // TODO add delete part button

    musicPanel = new JPanel(new MigLayout("Insets 0"));
    notePane = new PartNotePane();
    ImageIcon fistIcon = new ImageIcon(
        PartPanel.class.getResource("/icons/punch.png"));
    ImageIcon fistPressedIcon = new ImageIcon(
        PartPanel.class.getResource("/icons/punch-pressed.png"));
    JButton btnPunches = new JButton(fistIcon);
    btnPunches.setPressedIcon(fistPressedIcon);
    btnPunches.setBorderPainted(false);
    btnPunches.setFocusPainted(false);
    btnPunches.setContentAreaFilled(false);
    // TODO: visually indicate button press

    JSplitPane split = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT, musicPanel, notePane);
    this.add(split, "pad 0 15 0 0, growx, h 100%, w 100%");
    this.add(btnPunches, "h 100%");
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
   * Get the part assigned to this component
   *
   * @return the part assigned to this component
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
