package Punches;

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import net.miginfocom.swing.MigLayout;

/**
 * @author Vince Aquilina
 * @version 03/05/22
 *
 * A Part component that represents a cell in the SongPanel.
 *
 */
public class PartPanel extends JPanel
{
  protected Image sheetImage;           // sheet music snippet
  private Part part;                    // Part data for this component
  private PartNotePane notePane;        // notes pane
  private JPanel musicPanel;            // panel for sheet music/tab snippets
  private JPanel metaPanel;             // panel for Part metadata

  // metaPanel fields
  private JTextField txtPartName;          // Part name field
  private JTextField txtPartLength;        // Part length field

  // Colours
  Color panelGray = new Color(0xDDDDDD);

  /**
   * Constructs the component with the given Part data
   *
   * @param part - the Part object assigned to the component
   */
  public PartPanel(Part part)
  {
    this.part = part;

    /* Panel properties */
    setLayout(new MigLayout("Insets 5, fill"));
    setBackground(panelGray);
    setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

    /* musicPanel -- left section of split pane */
    musicPanel = new JPanel(new MigLayout("Insets 0"));
    musicPanel.setBackground(panelGray);

    /* notePane -- right section of split pane */
    notePane = new PartNotePane();
    notePane.setBorder(new EtchedBorder(EtchedBorder.RAISED));

    /* Fields section */
    // TODO implement delete part, assign values to fields
    metaPanel = new JPanel(new MigLayout("Insets 0"));
    metaPanel.setBackground(panelGray);
    metaPanel.setBorder(BorderFactory.createTitledBorder(
          new EtchedBorder(EtchedBorder.LOWERED), "Fields"));

    txtPartName = new JTextField(part.getName());
    txtPartLength = new JTextField(String.valueOf(part.getLengthInBars()));

    ImageIcon deleteIcon = new ImageIcon(
        PartPanel.class.getResource("/icons/delete.png"));
    JButton btnDelete = new JButton("delete", deleteIcon);

    // Punches button
    ImageIcon fistIcon = new ImageIcon(
        PartPanel.class.getResource("/icons/punch.png"));
    ImageIcon fistPressedIcon = new ImageIcon(
        PartPanel.class.getResource("/icons/punch-pressed.png"));
    JButton btnPunches = new JButton(fistIcon);
    btnPunches.setPressedIcon(fistPressedIcon);
    btnPunches.setBorderPainted(false);
    btnPunches.setFocusPainted(false);
    btnPunches.setContentAreaFilled(false);

    metaPanel.add(new JLabel("Name:"));
    metaPanel.add(txtPartName, "growx, wrap");
    metaPanel.add(new JLabel("# of bars:"));
    metaPanel.add(txtPartLength, "w 30!, split");
    metaPanel.add(btnDelete, "h 20!, wrap");
    metaPanel.add(btnPunches, "gaptop 10, alignx 50%, span");

    /* Split pane */
    // TODO ensure split sizes are retained on part resize, window resize
    JSplitPane split = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT, musicPanel, notePane);
    split.setBorder(BorderFactory.createTitledBorder(
          new EtchedBorder(EtchedBorder.LOWERED), "Notes"));
    split.setBackground(panelGray);

    // add components to part panel
    add(metaPanel, "gapleft 30, gapbottom 5, w 20%, growy, dock west");
    add(split, "growy, dock center");
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
