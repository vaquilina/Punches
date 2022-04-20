package Punches;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * A Part component that represents a cell in the Song.
 *
 * @author Vince Aquilina
 * @version 04/19/22
 */
public class PartPanel extends JPanel
{
  private final Logger logger = LoggerFactory.getLogger(PartPanel.class);

  /** The sheet music snippet */
  protected Image sheetImage;

  /** The location of the split pane divider */
  private Integer dividerLocation = 10;

  /** The panel containing Part fields and metadata */
  private JPanel fieldsPanel;
  /** The panel containing sheet music/tab snippets */
  private JPanel musicPanel;
  /** The text pane containing Part notes */
  private PartNotePane notePane;
  /** The split pane containing the musicPanel and notePane */
  private JSplitPane split;
  /** The Part data */
  private Part part;

  /** The part length field */
  private JTextField txtPartLength;
  /** The part name field */
  private JTextField txtPartName;
  /** The Label part's position in the song */
  private JLabel lblName;
  /** The delete button */
  private JButton btnDelete;
  /** The Punches button */
  private JButton btnPunches;

  /** The default background color for panel components, per L&amp;F */
  private Color defaultBgColor =
    UIManager.getLookAndFeelDefaults().getColor("Panel.background");

  /**
   * Construct the component with the given Part data
   * @param part the Part object assigned to the component
   */
  public PartPanel(Part part)
  {
    this.part = part;

    KeyboardFocusManager kfMgr = 
      KeyboardFocusManager.getCurrentKeyboardFocusManager();

    /* Panel properties */
    setLayout(new MigLayout("Insets 5, fill"));
    setBackground(defaultBgColor);
    setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

    /* musicPanel -- left section of split pane */
    musicPanel = new JPanel(new MigLayout("Insets 0"));
    musicPanel.setBackground(defaultBgColor);

    /* notePane -- right section of split pane */
    notePane = new PartNotePane();

    notePane.addKeyListener(new KeyListener() {
      @Override
      public void keyReleased(KeyEvent e) {
        part.setNotes(notePane.getPlainText());
      }
      @Override
      public void keyPressed(KeyEvent e) {}
      @Override
      public void keyTyped(KeyEvent e) {}
    });

    /* Fields section */
    fieldsPanel = new JPanel(new MigLayout("Insets 0"));
    fieldsPanel.setBackground(defaultBgColor);
    fieldsPanel.setBorder(BorderFactory.createTitledBorder(
          new EtchedBorder(EtchedBorder.LOWERED), "Fields"));

    lblName = new JLabel("Name: (" + part.getIndex() + ")");

    txtPartName = new JTextField(part.getName());
    txtPartName.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e)
      {
        txtPartName.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        txtPartName.setForeground(Color.BLACK);
      }
      @Override
      public void focusLost(FocusEvent e)
      {
        if (! txtPartName.getText().equals(part.getName()))  {
          txtPartName.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
          txtPartName.setForeground(Color.RED);
        }
        else {
          txtPartName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
          txtPartName.setForeground(Color.BLACK);
        }
      }
    });
    txtPartName.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            part.setName(txtPartName.getText());

            kfMgr.clearGlobalFocusOwner();
          }
        });

    txtPartLength = new JTextField(String.valueOf(part.getLengthInBars()));
    txtPartLength.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e)
      {
        txtPartLength.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        txtPartLength.setForeground(Color.BLACK);
      }
      @Override
      public void focusLost(FocusEvent e)
      {
        if (! txtPartLength.getText().equals
            (String.valueOf(part.getLengthInBars())))  {
          txtPartLength.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
          txtPartLength.setForeground(Color.RED);
        }
        else {
          txtPartLength.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
          txtPartLength.setForeground(Color.BLACK);
        }

        System.out.println(part.getLengthInBars());
      }
    });
    txtPartLength.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            part.setLengthInBars(Integer.valueOf(txtPartLength.getText()));
          }
        });

    ImageIcon deleteIcon = new ImageIcon(
        PartPanel.class.getResource("/icons/delete.png"));
    btnDelete = new JButton("delete", deleteIcon);

    // Punches button
    ImageIcon fistIcon = new ImageIcon(
        PartPanel.class.getResource("/icons/punch.png"));
    ImageIcon fistPressedIcon = new ImageIcon(
        PartPanel.class.getResource("/icons/punch-pressed.png"));
    btnPunches = new JButton(fistIcon);
    btnPunches.setPressedIcon(fistPressedIcon);
    btnPunches.setBorderPainted(false);
    btnPunches.setFocusPainted(false);
    btnPunches.setContentAreaFilled(false);


    fieldsPanel.add(lblName);
    fieldsPanel.add(txtPartName, "growx, wrap");
    fieldsPanel.add(new JLabel("# of bars:"));
    fieldsPanel.add(txtPartLength, "w 30!, split");
    fieldsPanel.add(btnDelete, "h 20!, wrap");
    fieldsPanel.add(btnPunches, "gaptop 10, alignx 50%, span");

    /* Split pane */
    split = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT, musicPanel, notePane);
    split.setBorder(BorderFactory.createTitledBorder(
          new EtchedBorder(EtchedBorder.LOWERED), "Notes"));
    split.setBackground(defaultBgColor);
    split.setContinuousLayout(true);

    // if user re-positions divider, remember new location
    split.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
          setSplitDividerLocation(split.getDividerLocation());

          //DEBUG {{{
          logger.debug(" div moved: pos {}", getSplitDividerLocation());
          //////////// }}}
        }
      }
    });

    // add components to part panel
    add(fieldsPanel, "gapleft 30, gapbottom 5, w 20%, growy, dock west");
    add(split, "growy, dock center");
  }

  /**
   * Get the part assigned to this component
   * @return the part assigned to this component
   */
  public Part getPart()
  {
    return part;
  }

  /**
   * Get the split pane's divider location
   * @return the spit pane divider's location
   */
  public Integer getSplitDividerLocation()
  {
    return dividerLocation;
  }

  /**
   * Get the panel's split pane
   * @return the panel's split pane
   */
  public JSplitPane getSplitPane()
  {
    return split;
  }

  /**
   * Get the panel's note pane
   * @return the panel's note pane
   */
  public PartNotePane getNotePane()
  {
    return notePane;
  }

  /**
   * Get the panel's musicPanel
   * @return the panel's musicPanel
   */
  public JPanel getMusicPanel()
  {
    return musicPanel;
  }

  /**
   * Assign a new part to this component
   * @param part the new part to be assigned
   */
  public void setPart(Part part)
  {
    this.part = part;
  }

  /**
   * Set the split pane divider location
   * @param dividerLocation the new divider location
   */
  public void setSplitDividerLocation(int dividerLocation)
  {
    this.dividerLocation = dividerLocation;
    positionDivider();
  }

  /**
   * Position the split pane divider
   */
  public void positionDivider()
  {
    split.setDividerLocation(dividerLocation);
    revalidate();
  }

  /**
   * Update the index in the fields panel
   * @param index the part index
   */
  public void updateIndex(int index)
  {
    lblName.setText("Name: (" + index + ") ");
  }

  /**
   * Get the delete button
   * @return the delete button
   */
  public JButton getDeleteButton()
  {
    return btnDelete;
  }

  /**
   * Get the punches button
   * @return the punches button
   */
  public JButton getPunchesButton()
  {
    return btnPunches;
  }

  /**
   * Define how the component is to be painted.
   * @param g graphics
   */
  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2d = (Graphics2D)g;
    g2d.clearRect(0, 0, getWidth(), getHeight());

    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, getWidth(), getHeight());
  }
}

