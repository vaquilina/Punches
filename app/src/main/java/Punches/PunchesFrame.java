package Punches;

import java.util.Collections;
import java.util.LinkedHashMap; // maintains order of keys
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
//import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;

import java.awt.Color;
//import java.awt.datatransfer.Clipboard;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.KeyboardFocusManager;

import net.miginfocom.swing.MigLayout;

import org.softsmithy.lib.swing.customizer.JCustomizer;
import org.softsmithy.lib.swing.customizer.layout.InfiniteTableLayout;
import org.softsmithy.lib.swing.customizer.layout.RelativeTableConstraints;

/**
 * @author Vince Aquilina
 * @version 03/06/22
 *
 * Punches Desktop GUI.
 *
 * TODO: Write tests
 * TODO: adjust scrollbar speed (implement scrollable)
 */
public class PunchesFrame extends JFrame implements ComponentListener
{
  //TODO: implement clipboard
  //private Clipboard internalClipboard;      // for yank/put Part
  //private Clipboard externalClipboard;      // for yank/put text or image

  private SongPanel panSong;         // panel containing parts
  private InfiniteTableLayout itl;    // layout manager for panSong
  private JScrollPane scroller;       // scrollpane containing panSong

  // Flags
  private boolean unsavedChanges;

  // Colors
  Color panelGray = new Color(0xDDDDDD);
  Color apricot = new Color(0xFFCCB3);

  @Override
  public void componentHidden(ComponentEvent e) {};
  @Override
  public void componentShown(ComponentEvent e) {};
  @Override
  public void componentMoved(ComponentEvent e) {};

  /**
   * Dynamically resizes Part cells when frame is resized
   *
   * @param e the resize event
   */
  @Override
  public void componentResized(ComponentEvent e) {
    adjustCellWidth();
    panSong.removeAll();
    populateParts();
  };

  /**
   * @param title - the window title
   *
   * Constructs the main content pane
   */
  public PunchesFrame(String title)
  {
    super(title);

    KeyboardFocusManager kfMgr = 
      KeyboardFocusManager.getCurrentKeyboardFocusManager();

    unsavedChanges = false;

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception ex) {
      System.out.println("error setting system look and feel");
      ex.printStackTrace();
    }
    this.setLayout(new MigLayout("Insets 5"));

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    /* Toolbar
     * TODO: link to source (http://www.famfamfam.com/) on about dialog
     */

    Map<String, ImageIcon> toolbarIcons = new LinkedHashMap<>();
    toolbarIcons.put("New Song" ,
        new ImageIcon(PunchesFrame.class.getResource("/icons/page_add.png")));
    toolbarIcons.put("Load Song" ,
        new ImageIcon(PunchesFrame.class.getResource("/icons/folder.png")));
    toolbarIcons.put("Save Song" ,
        new ImageIcon(PunchesFrame.class.getResource("/icons/disk.png")));
    toolbarIcons.put("Export to PDF directly" ,
        new ImageIcon(PunchesFrame.class.getResource(
            "/icons/page_white_acrobat.png")));
    toolbarIcons.put("Cut Selection",
        new ImageIcon(PunchesFrame.class.getResource("/icons/cut.png")));
    toolbarIcons.put("Copy Selection",
        new ImageIcon(PunchesFrame.class.getResource("/icons/page_copy.png")));
    toolbarIcons.put("Paste Selection",
        new ImageIcon(PunchesFrame.class.getResource("/icons/page_paste.png")));
    toolbarIcons.put("Undo Last Action",
        new ImageIcon(PunchesFrame.class.getResource("/icons/arrow_undo.png")));
    toolbarIcons.put("Redo Last Action",
        new ImageIcon(PunchesFrame.class.getResource("/icons/arrow_redo.png")));
    toolbarIcons.put("Add Part",
        new ImageIcon(PunchesFrame.class.getResource("/icons/add.png")));
    toolbarIcons.put("About",
        new ImageIcon(PunchesFrame.class.getResource("/icons/help.png")));
    toolbarIcons.put("Quit",
        new ImageIcon(PunchesFrame.class.getResource("/icons/door_out.png")));

    JPanel toolbar = new JPanel(new MigLayout("Insets 0"));

    Map<String, JButton> toolbarButtons = new LinkedHashMap<>();
    for (Map.Entry<String, ImageIcon> entry : toolbarIcons.entrySet()) {
      toolbarButtons.put(entry.getKey(), new JButton(entry.getValue()));
    }

    for (Map.Entry<String, JButton> btn : toolbarButtons.entrySet()) {
      btn.getValue().setRolloverEnabled(true);
      btn.getValue().setToolTipText(btn.getKey());
    }

    toolbarButtons.get("Add Part").setText("add part");
    toolbarButtons.get("Add Part").setMargin(new Insets(2, 2, 2, 2));

    JTextField txtSongTitle = new JTextField("Song Title", 30);
    txtSongTitle.setFont(new Font(Font.SERIF, Font.ITALIC, 12));
    txtSongTitle.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e)
      {
        if (txtSongTitle.getText().equals("Song Title")) {
          txtSongTitle.setText("");
          txtSongTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        }
      }
      @Override
      public void focusLost(FocusEvent e)
      {
        Song song = panSong.getSong();

        if (txtSongTitle.getText().equals("")) {
          txtSongTitle.setText("Song Title");
          txtSongTitle.setFont(new Font(Font.SERIF, Font.ITALIC, 12));
        }
        else { 
          if (! txtSongTitle.getText().equals(song.getTitle()))  {
            txtSongTitle.setFont(new Font(Font.SERIF, Font.ITALIC, 12));
            txtSongTitle.setForeground(Color.RED);
          }
          else {
            txtSongTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
            txtSongTitle.setForeground(Color.BLACK);
          }
        }
      }
    });
    txtSongTitle.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Song song = panSong.getSong();
        song.setTitle(txtSongTitle.getText());

        kfMgr.clearGlobalFocusOwner();
      }
    });

    Map<String, ImageIcon> musicNotes = new LinkedHashMap<>();
    musicNotes.put("whole", 
        new ImageIcon(PunchesFrame.class.getResource(
            "/icons/music-note-1_16px.png")));
    musicNotes.put("half",
        new ImageIcon(PunchesFrame.class.getResource(
            "/icons/music-note-2_16px.png")));
    musicNotes.put("quarter",
        new ImageIcon(PunchesFrame.class.getResource(
            "/icons/music-note-4_16px.png")));
    musicNotes.put("eighth",
        new ImageIcon(PunchesFrame.class.getResource(
            "/icons/music-note-8_16px.png")));
    musicNotes.put("sixteenth",
        new ImageIcon(PunchesFrame.class.getResource(
            "/icons/music-note-16_16px.png")));
    musicNotes.put("thirty-second",
        new ImageIcon(PunchesFrame.class.getResource(
            "/icons/music-note-32_16px.png")));
    Vector<ImageIcon> musicNoteIcons = new Vector<>(musicNotes.values());

    JLabel lblTimeSignature = new JLabel("time signature:");

    // TODO validate beats per bar
    JTextField txtBeatsPerBar = new JTextField("4", 2);
    txtBeatsPerBar.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e)
      {
        txtBeatsPerBar.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        txtBeatsPerBar.setForeground(Color.BLACK);
      }
      @Override
      public void focusLost(FocusEvent e)
      {
        Song song = panSong.getSong();

        if (! Integer.valueOf(txtBeatsPerBar.getText()).equals(
              song.getSignature().getBeatsPerBar()))  {
          txtBeatsPerBar.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
          txtBeatsPerBar.setForeground(Color.RED);
              }
        else {
          txtBeatsPerBar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
          txtBeatsPerBar.setForeground(Color.BLACK);
        }
      }
    });
    txtBeatsPerBar.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            Song song = panSong.getSong();
            song.getSignature().setBeatsPerBar(
                Integer.valueOf(txtBeatsPerBar.getText()));

            kfMgr.clearGlobalFocusOwner();
          }
        });

    JLabel lblSlash = new JLabel("/");

    JComboBox<ImageIcon> cmbValueOfABeat = new JComboBox<>(musicNoteIcons);
    cmbValueOfABeat.setSelectedIndex(2); // defaults to quarter note
    DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
    listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
    cmbValueOfABeat.setRenderer(listRenderer);
    cmbValueOfABeat.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            BeatValue value;

            switch (cmbValueOfABeat.getSelectedIndex()) {
              case 0:
                value = BeatValue.WHOLE;
                break;
              case 1:
                value = BeatValue.HALF;
                break;
              case 2:
                value = BeatValue.QUARTER;
                break;
              case 3:
                value = BeatValue.EIGHTH;
                break;
              case 4:
                value = BeatValue.SIXTEENTH;
                break;
              case 5:
                value = BeatValue.THIRTY_SECOND;
                break;
              default:
                value = BeatValue.QUARTER;
            }

            panSong.getSong().getSignature().setValueOfABeat(value);

            kfMgr.clearGlobalFocusOwner();
          }
        });

    JLabel lblBpm = new JLabel("bpm:");
    // TODO validate bpm
    JTextField txtBpm = new JTextField("120", 3);
    txtBpm.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e)
      {
        txtBpm.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        txtBpm.setForeground(Color.BLACK);
      }
      @Override
      public void focusLost(FocusEvent e)
      {
        if (! Integer.valueOf(txtBpm.getText()).equals(
              panSong.getSong().getBpm()))  {
          txtBpm.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
          txtBpm.setForeground(Color.RED);
              }
        else {
          txtBpm.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
          txtBpm.setForeground(Color.BLACK);
        }
      }
    });
    txtBpm.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            panSong.getSong().setBpm(Integer.valueOf(txtBpm.getText()));

            kfMgr.clearGlobalFocusOwner();
          }
        });

    toolbar.add(toolbarButtons.get("New Song"), "w 24!, h 24!");
    toolbar.add(toolbarButtons.get("Load Song"), "w 24!, h 24!");
    toolbar.add(toolbarButtons.get("Save Song"), "w 24!, h 24!");
    toolbar.add(toolbarButtons.get("Export to PDF directly"), "w 24!, h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL), "h 24!");
    toolbar.add(toolbarButtons.get("Cut Selection"), "w 24!, h 24!");
    toolbar.add(toolbarButtons.get("Copy Selection"), "w 24!, h 24!");
    toolbar.add(toolbarButtons.get("Paste Selection"), "w 24!, h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL), "h 24!");
    toolbar.add(toolbarButtons.get("Undo Last Action"), "w 24!, h 24!");
    toolbar.add(toolbarButtons.get("Redo Last Action"), "w 24!, h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL), "h 24!");
    toolbar.add(toolbarButtons.get("Add Part"), "w 100!, h 24!");
    toolbar.add(txtSongTitle, "w 150!, h 24!");
    toolbar.add(lblTimeSignature);
    toolbar.add(txtBeatsPerBar, "w 24!, h 24!");
    toolbar.add(lblSlash);
    toolbar.add(cmbValueOfABeat, "w 48!, h 24!");
    toolbar.add(lblBpm);
    toolbar.add(txtBpm, "w 30!, h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL), "h 24!");
    toolbar.add(toolbarButtons.get("About"), "w 24!, h 24!"); 
    /* TODO: launch about dialog (my credits, icon credits, lib credits,
       adobe logo, donate button) */
    toolbar.add(toolbarButtons.get("Quit"), "w 24!, h 24!, wrap");

    toolbarButtons.get("New Song").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            createSong();
          }
        });
    toolbarButtons.get("Load Song").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO loadSong();
          }
        });
    toolbarButtons.get("Save Song").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            //TODO saveSong();
          }
        });
    toolbarButtons.get("Export to PDF directly").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO exportToPDF();
          }
        });
    toolbarButtons.get("Cut Selection").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO cutSelection();
          }
        });
    toolbarButtons.get("Copy Selection").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO copySelection();
          }
        });
    toolbarButtons.get("Paste Selection").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO pasteSelection;
          }
        });
    toolbarButtons.get("Undo Last Action").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO undo();
          }
        });
    toolbarButtons.get("Redo Last Action").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO redo();
          }
        });
    toolbarButtons.get("Add Part").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            addPart();
          }
        });
    toolbarButtons.get("About").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // TODO launchAboutDialog();
          }
        });
    toolbarButtons.get("Quit").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            cleanExit();
          }
        });

    /*
     * Song Panel
     */
    panSong = new SongPanel(new Song());
    panSong.setBackground(new Color(0xFFFFFF));
    scroller = new JScrollPane(
        panSong,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    /*
     * initialize layout
     */
    getContentPane().add(toolbar, "span");
    getContentPane().add(scroller, "grow, w 100%, h 100%");
    pack();    // necessary in order to get size of frame

    /*
     * Song Panel (cont'd)
     */
    int cellWidth = (int) (getContentPane().getSize().getWidth() - 
        ((Integer)(UIManager.get("ScrollBar.width"))).intValue());
    cellWidth -= panSong.getInsets().left + panSong.getInsets().right;
    cellWidth -= getInsets().left + getInsets().right;

    // TODO: Attribute softsmithy
    itl = new InfiniteTableLayout(cellWidth - 10, 200, panSong);
    panSong.setCustomizerLayout(itl);

    // adjust initial window height
    this.setBounds(getX(), getY(), getWidth(), 800); 
    addComponentListener(this); // listen for resize events
  }

  /////////////////////
  // HELPER METHODS //
  ////////////////////

  /**
   * Adjusts Part cell width to fit window
   */
  private void adjustCellWidth()
  {
    int cellWidth = (int) (getContentPane().getSize().getWidth() - 
        ((Integer)(UIManager.get("ScrollBar.width"))).intValue());
    cellWidth -= panSong.getInsets().left + panSong.getInsets().right;
    cellWidth -= getInsets().left + getInsets().right;

    itl = new InfiniteTableLayout(cellWidth - 10, 200, panSong);
    panSong.setCustomizerLayout(itl);

    panSong.invalidate();
    panSong.validate();
  }

  /**
   * Adds "double-click to edit" functionality to PartPanels
   *
   * @param customizer - the JCustomizer wrapper object
   */
  private void makeEditable(JCustomizer cell)
  {
    cell.addActionListener(new ActionListener() {
      boolean editing;
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!editing) {
          cell.getComponent().setBackground(apricot);
          cell.setComponentZOrder(cell.getComponent(1), 0);
          cell.getStateManager().setStateNormal();
          editing = true;
        }
        else {
          cell.getComponent().setBackground(panelGray);
          cell.setComponentZOrder(cell.getComponent(1), 0);
          cell.getStateManager().setStateMove();
          editing = false;
        }
      }
    });
  }

  /**
   * Provides a clean exit when quit button is clicked
   */
  private void cleanExit()
  {
    // TODO: check for unsaved changes, running threads; prompt
    System.exit(0);
  }

  /**
   * Check for unsaved changes
   *
   * TODO implement function
   *
   * @return TRUE if there are unsaved changes; otherwise FALSE
   */
  private boolean hasUnsavedChanges()
  {
    return false;
  }

  /**
   * Populate Song panel
   */
  private void populateParts()
  {
    for (int i = 0; i < panSong.getSong().getParts().size(); i++) {
      JCustomizer cell = new JCustomizer(
          new PartPanel(panSong.getSong().getParts().get(i)));
      makeEditable(cell);
      panSong.addCustomizer(cell,
          new RelativeTableConstraints(0, i, 1, 1, cell, itl));
    }

    panSong.revalidate();
    scroller.revalidate();
  }

  ////////////////////
  // BUTTON METHODS //
  ////////////////////

  /**
   * Create a new Song
   */
  public void createSong()
  {
    // TODO
    // if (hasUnsavedChanges()) {
    //   prompt();
    // }
    panSong.getSong().clearParts();
    panSong.removeAll();

    panSong.setSong(new Song());
    populateParts();
  }

  /**
   * Add a new Part to the Song
   */
  public void addPart()
  {
    panSong.getSong().addNewPart();
    panSong.removeAll();
    populateParts();
  }
}
