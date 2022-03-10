package Punches;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;
/**
 * @author Vince Aquilina
 * @version 03/10/22
 *
 * A Part component that represents a cell in the Song.
 */
public class PartPanel extends JPanel
{
  /** The sheet music snippet */
  protected Image sheetImage;

  /** Reference to the parent frame */
  private PunchesFrame parentFrame;
  /** The location of the split pane divider */
  private Integer dividerLocation = 10;

  /** The panel containing Part fields and metadata */
  private JPanel fieldsPanel;
  /** The panel containing sheet music/tab snippets */
  private JPanel musicPanel;
  /** The split pane containing the fieldsPanel and musicPanel */
  private JSplitPane split;
  /** The Part data */
  private Part part;
  /** The text pane containing Part notes */
  private PartNotePane notePane;

  /** The part length field */
  private JTextField txtPartLength;        // Part length field
  /** the part name field */
  private JTextField txtPartName;          // Part name field

  private Color panelGray = new Color(0xDDDDDD);

  //DEBUG
  private boolean debugging;
  private int step = 0;

  /**
   * Constructs the component with the given Part data
   *
   * @param part - the Part object assigned to the component
   */
  public PartPanel(Part part)
  {
    this.part = part;

    KeyboardFocusManager kfMgr = 
      KeyboardFocusManager.getCurrentKeyboardFocusManager();

    debugging = true;

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
    fieldsPanel = new JPanel(new MigLayout("Insets 0"));
    fieldsPanel.setBackground(panelGray);
    fieldsPanel.setBorder(BorderFactory.createTitledBorder(
          new EtchedBorder(EtchedBorder.LOWERED), "Fields"));

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
    JButton btnDelete = new JButton("delete", deleteIcon);
    btnDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        parentFrame.removePart(part.getIndex());
      }
    });

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

    fieldsPanel.add(new JLabel("Name:"));
    fieldsPanel.add(txtPartName, "growx, wrap");
    fieldsPanel.add(new JLabel("# of bars:"));
    fieldsPanel.add(txtPartLength, "w 30!, split");
    fieldsPanel.add(btnDelete, "h 20!, wrap");
    fieldsPanel.add(btnPunches, "gaptop 10, alignx 50%, span");

    /* Split pane */
    // TODO ensure split sizes are retained on part resize, window resize
    split = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT, musicPanel, notePane);
    split.setBorder(BorderFactory.createTitledBorder(
          new EtchedBorder(EtchedBorder.LOWERED), "Notes"));
    split.setBackground(panelGray);

    // if user re-positions divider, remember new location
    split.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
          dividerLocation = split.getDividerLocation();

          //DEBUG {{{
          if (debugging) {
            step++;
            System.out.println("PARTPANEL:" + step + " !! div moved:" + 
                "pos " + dividerLocation);
          }
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
   *
   * @return the part assigned to this component
   */
  public Part getPart()
  {
    return part;
  }

  /**
   * Get the split pane's divider location
   */
  public Integer getSplitDividerLocation()
  {
    return dividerLocation;
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

  /**
   * Assign reference to parent PunchesFrame
   *
   * @param parentFrame - reference to the parent PunchesFrame
   */
  public void setParentFrame(PunchesFrame parentFrame)
  {
    this.parentFrame = parentFrame;
  }

  /**
   * Position the split pane divider
   */
  public void positionDivider()
  {
    split.setDividerLocation(dividerLocation);
    split.revalidate();
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
}

