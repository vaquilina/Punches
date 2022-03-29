package Punches;

//import java.awt.datatransfer.Clipboard;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.util.LinkedHashMap; // maintains order of keys
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane; // for prompts
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.softsmithy.lib.swing.customizer.event.CustomizerEvent;
import org.softsmithy.lib.swing.customizer.layout.InfiniteTableLayout;
import org.softsmithy.lib.swing.customizer.layout.RelativeTableConstraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Punches Desktop GUI.
 *
 * @author Vince Aquilina
 * @version 03/25/22
 *
 * <b>Icons: </b>
 * <a href="https://www.famfamfam.com/lab/icons/silk">famfamfam</a>
 */
public class PunchesFrame extends JFrame implements ComponentListener
{
  /*
   * TODO: Write tests
   * TODO: implement clipboard
   * TODO: implement command history
   * TODO: add manual reordering  
   * TODO: migrate to FlatLaf L&F? formdev.com/flatlaf
   */

  private static final Logger logger =
    LoggerFactory.getLogger(PunchesFrame.class);

  //private Clipboard internalClipboard;      // for yank/put Part
  //private Clipboard externalClipboard;      // for yank/put text or image

  /** The Song title text field */
  private JTextField txtSongTitle;
  /** The "beats per bar" field */
  private JTextField txtBeatsPerBar;
  /** The "value of a beat" dropdown */
  private JComboBox<ImageIcon> cmbValueOfABeat;
  /** The tempo text field */
  private JTextField txtBpm;
  /** Panel containing parts */
  private SongPanel panSong;
  /** Layout manager for panSong */
  private InfiniteTableLayout itl;
  /** Scroll pane for panSong */
  private JScrollPane scroller;

  /** Bound Part cells */
  List<PartPanelWrapper> cells;

  /** Flags that there are unsaved changes */
  private boolean unsavedChanges;
  /** Flags that the song panel has been initialized */
  private boolean initialized;

  /** Row height */
  private static final int ROWHEIGHT = 200;
  /** The column in which the panel will be drawn */
  public static final int COLUMN = 0;

  // Colors
  private Color panelGray = new Color(0xDDDDDD);
  private Color apricot = new Color(0xFFCCB3);

  /**
   * Construct the main content pane
   *
   * @param title the window title
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
      logger.error(ex.getMessage(), ex.getClass());
    }

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    /*
     * Toolbar
     */

    // Toolbar Buttons
    Map<String, ImageIcon> toolbarIcons = initToolbarIcons();

    Map<String, JButton> toolbarButtons = new LinkedHashMap<>();
    for (Map.Entry<String, ImageIcon> entry : toolbarIcons.entrySet()) {
      toolbarButtons.put(entry.getKey(), new JButton(entry.getValue()));
    }

    for (Map.Entry<String, JButton> btn : toolbarButtons.entrySet()) {
      btn.getValue().setRolloverEnabled(true);
      btn.getValue().setToolTipText(btn.getKey());
    }

    // "Add Part" Button
    toolbarButtons.get("Add Part").setText("add part");
    toolbarButtons.get("Add Part").setMargin(new Insets(2, 2, 2, 2));
    toolbarButtons.get("Add Part").setMnemonic(KeyEvent.VK_A);

    // "Song Title" Text Field
    txtSongTitle = new JTextField("Song Title");
    txtSongTitle.setToolTipText("Song Title");

    JLabel lblSongTitle = new JLabel(""); // not visible; for mnemonic
    lblSongTitle.setLabelFor(txtSongTitle);
    lblSongTitle.setDisplayedMnemonic(KeyEvent.VK_T);

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

    // Time Signature Section
    // TODO validate beats per bar
    txtBeatsPerBar = new JTextField("4", 2);
    txtBeatsPerBar.setToolTipText("Beats per Bar");
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
    JLabel lblTimeSignature = new JLabel("time signature:");
    lblTimeSignature.setLabelFor(txtBeatsPerBar);
    lblTimeSignature.setDisplayedMnemonic(KeyEvent.VK_S);

    JLabel lblSlash = new JLabel("/");

    // Music Note Icons
    Map<String, ImageIcon> musicNotes = initMusicNoteIcons();

    // load into vector for use with JComboBox
    Vector<ImageIcon> musicNoteIcons = new Vector<>(musicNotes.values());

    cmbValueOfABeat = new JComboBox<>(musicNoteIcons);
    cmbValueOfABeat.setSelectedIndex(2); // defaults to quarter note

    DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
    listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
    cmbValueOfABeat.setRenderer(listRenderer);
    cmbValueOfABeat.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

            BeatValue value = getBeatValue(cmbValueOfABeat.getSelectedIndex());
            panSong.getSong().getSignature().setValueOfABeat(value);

            kfMgr.clearGlobalFocusOwner(); // clear focus on selection
          }
        });

    // Tempo Section
    /* TODO validate bpm */
    txtBpm = new JTextField("120", 3);

    JLabel lblBpm = new JLabel("bpm:");
    lblBpm.setLabelFor(txtBpm);
    lblBpm.setDisplayedMnemonic(KeyEvent.VK_B);

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

    // Layout Toobar
    JPanel toolbar =
      new JPanel(new MigLayout("Insets 0",
            "[][][][]0[][][]", 
            "[]"));

    toolbar.add(toolbarButtons.get("New Song"),  "cell 0 0, w 24!,  h 24!");
    toolbar.add(toolbarButtons.get("Load Song"), "cell 0 0, w 24!,  h 24!");
    toolbar.add(toolbarButtons.get("Save Song"), "cell 0 0, w 24!,  h 24!");
    toolbar.add(toolbarButtons.get(
                      "Export to PDF directly"), "cell 0 0, w 24!,  h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL),
                                                 "cell 0 0,         h 24!");
    toolbar.add(toolbarButtons.get("Cut Selection"),
                                                 "cell 1 0, w 24!,  h 24!");
    toolbar.add(toolbarButtons.get("Copy Selection"),
                                                 "cell 1 0, w 24!,  h 24!");
    toolbar.add(toolbarButtons.get("Paste Selection"),
                                                 "cell 1 0, w 24!,  h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL),
                                                 "cell 1 0,         h 24!");
    toolbar.add(toolbarButtons.get("Undo Last Action"),
                                                 "cell 2 0, w 24!,  h 24!");
    toolbar.add(toolbarButtons.get("Redo Last Action"),
                                                 "cell 2 0, w 24!,  h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL),
                                                 "cell 2 0,         h 24!");
    toolbar.add(toolbarButtons.get("Add Part"),  "cell 3 0, w 100!, h 24!");
    toolbar.add(lblSongTitle,                    "cell 4 0, w 0!,   h 24!");
    toolbar.add(txtSongTitle,                    "cell 4 0, w 150!, h 24!");
    toolbar.add(lblTimeSignature,                "cell 5 0,         h 24!");
    toolbar.add(txtBeatsPerBar,                  "cell 5 0, w 24!,  h 24!");
    toolbar.add(lblSlash,                        "cell 5 0,         h 24!");
    toolbar.add(cmbValueOfABeat,                 "cell 5 0,         h 24!");
    toolbar.add(lblBpm,                          "cell 5 0,         h 24!");
    toolbar.add(txtBpm,                          "cell 5 0, w 48!,  h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL), 
                                                 "cell 5 0,         h 24!");
    toolbar.add(toolbarButtons.get("About"),     "cell 6 0, w 24!,  h 24!"); 
    toolbar.add(toolbarButtons.get("Quit"),      "cell 6 0, w 24!,  h 24!");

    // ActionListeners for Buttons
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
            loadSong();
          }
        });
    toolbarButtons.get("Save Song").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            saveSong();
          }
        });
    toolbarButtons.get("Export to PDF directly").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            exportSong();
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
            showAboutDialog();
          }
        });
    toolbarButtons.get("Quit").addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            cleanExit();
          }
        });

    // TODO Clipboard
    toolbarButtons.get("Cut Selection").setEnabled(false);
    toolbarButtons.get("Copy Selection").setEnabled(false);
    toolbarButtons.get("Paste Selection").setEnabled(false);

    // TODO Command history
    toolbarButtons.get("Undo Last Action").setEnabled(false);
    toolbarButtons.get("Redo Last Action").setEnabled(false);

    /*
     * Song Panel
     */
    panSong = new SongPanel(new Song());
    panSong.setBackground(new Color(0xFFFFFF));

    // DEBUG {{{
    logger.debug("Song panel initialized. parts: ", 
        panSong.getSong().getParts().size());
    //////////// }}}

    scroller = new JScrollPane(
        panSong,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroller.getVerticalScrollBar().setUnitIncrement(15);

    // add major components to frame
    this.setLayout(new MigLayout("Insets 5"));

    getContentPane().add(toolbar, "span, w 100%");
    getContentPane().add(scroller, "grow, w 100%, h 100%");

    // resize window to size of contents
    pack();    // necessary in order to get size of frame

    int cellWidth = (int) (getContentPane().getSize().getWidth() - 
        ((Integer)(UIManager.get("ScrollBar.width"))).intValue());
    cellWidth -= panSong.getInsets().left + panSong.getInsets().right;
    cellWidth -= getInsets().left + getInsets().right;

    itl = new InfiniteTableLayout(cellWidth, ROWHEIGHT, panSong);
    panSong.setCustomizerLayout(itl);

    // adjust initial window height
    setBounds(getX(), getY(), getWidth(), 1080); 

    setMinimumSize(new Dimension(getWidth(),
          toolbar.getHeight() + 237));

    // listen for resize events
    addComponentListener(this);

    initSongPanel();
  }

  //////////////////
  // INIT METHODS //
  //////////////////

  /**
   * Load toolbar icon resources
   *
   * @return A Map containing toolbar icon resources
   */
  private Map<String, ImageIcon> initToolbarIcons()
  {
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

    return toolbarIcons;
  }

  /**
   * Load music note icon resources 
   *
   * @return A Map containing music note icon resources
   */
  private Map<String, ImageIcon> initMusicNoteIcons()
  {
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

    return musicNotes;
  }

  /**
   * Initialize PartPanels
   *
   * @param parts a List containing the Song's Parts
   */
  private void initSongPanel()
  {
    Song song = panSong.getSong();

    cells = new ArrayList<>();

    PartPanelWrapper cell = new PartPanelWrapper(new Part());
    cell.getPartPanel().getDeleteButton().
      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          removePart(cell.getPart().getIndex());
        }
      });

    JFrame parent = this;
    cell.getPartPanel().getPunchesButton().
      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          PunchesDialog punchesDialog =
            new PunchesDialog(parent, song, cell.getPart());
          punchesDialog.setVisible(true);
        }
      });
	  
	txtSongTitle.setText(song.getTitle());

    txtBeatsPerBar.setText(
        String.valueOf(song.getSignature().getBeatsPerBar()));

    TimeSignature signature = song.getSignature();
    cmbValueOfABeat.setSelectedIndex(
        switch (signature.getValueOfABeat()) {
          case WHOLE     -> 0;
          case HALF      -> 1;
          case QUARTER   -> 2;
          case EIGHTH    -> 3;
          case SIXTEENTH -> 4;
          default        -> 2;
        }
        );

    txtBpm.setText(String.valueOf(song.getBpm()));

    cells.add(cell);

    // make Parts aware of their position in the Song
    song.refreshIndices();
    cell.getPartPanel().updateIndex(cell.getPart().getIndex());

    panSong.addCustomizer(cell.getPartPanelCustomizer(), 
        new RelativeTableConstraints(COLUMN, 0,
          1, 1, cell.getPartPanelCustomizer(), itl));

    PartPanelCustomizer customizer = cell.getPartPanelCustomizer();
    customizer.registerComponentListener();
    customizer.registerPropertyChangeListener();

    int width = adjustCellWidth();

    // fire a CustomizerEvent to make cell aware of its position
    customizer.customizerReshapeRel(
        new CustomizerEvent(
          customizer, COLUMN, 0, width, ROWHEIGHT
          ));

    // fire a CustomizerEvent to make cell aware of its size
    customizer.customizerResetBoundsRel(new CustomizerEvent(
          customizer, COLUMN, 0, width, ROWHEIGHT
          ));

    panSong.repaint();
    panSong.revalidate();

    cell.storePosition();

    //DEBUG {{{
    logger.info("cell added");
    logger.debug("parts: {}, cells: {}",
        song.getParts().size(), cells.size());
    logger.debug("part: ", cell.getPartPanel().getPart().getName());
    logger.debug("initial position: " + cell.getStoredPosition().y +
        ", w: " + cell.getStoredPosition().width +
        ", h: " + cell.getStoredPosition().height +
        ", r: " + cell.getPartPanelCustomizer().getRow() +
        ", s: " + cell.getPartPanelCustomizer().getRowSpan());

    logger.info("song panel initialized");
    //////////// }}}

    initialized = true;
  }

  ////////////////////
  // HELPER METHODS //
  ////////////////////

  /**
   * Show the About dialog
   */
  public void showAboutDialog()
  {
    AboutDialog aboutDialog = new AboutDialog(this);

    aboutDialog.setLocationRelativeTo(null);
    aboutDialog.setVisible(true);
  }

  /**
   * Re-layout Part cells
   *
   * @param bounds the cells' bounds
   * @param dividerLocations the cell's divider locations
   */
  private void refreshTable(
      List<CellBounds> bounds, List<Integer> dividerLocations)
  {
    panSong.removeAll();
    panSong.repaint();

    int cellWidth = adjustCellWidth();
    itl = new InfiniteTableLayout(cellWidth, ROWHEIGHT, panSong);
    panSong.setCustomizerLayout(itl);

    Song song = panSong.getSong();
    txtSongTitle.setText(song.getTitle());
    txtBeatsPerBar.setText(
        String.valueOf(song.getSignature().getBeatsPerBar()));

    BeatValue beatValue = song.getSignature().getValueOfABeat();
    cmbValueOfABeat.setSelectedIndex(
        switch (beatValue) {
          case WHOLE     -> 0;
          case HALF      -> 1;
          case QUARTER   -> 2;
          case EIGHTH    -> 3;
          case SIXTEENTH -> 4;
          default        -> 3;
        }
    );

    txtBpm.setText(String.valueOf(song.getBpm()));

    cells = new ArrayList<>();

    ListIterator<Part> itParts = panSong.getSong().getParts().listIterator();
    ListIterator<CellBounds> itBounds = bounds.listIterator();

	JFrame parent = this;
    int i = 0;
    while (itParts.hasNext()) {

      Part part = itParts.next(); 
      PartPanelWrapper cell = new PartPanelWrapper(part);
      cell.getPartPanel().getDeleteButton().
        addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            removePart(cell.getPart().getIndex());
          }
        });
		
	  cell.getPartPanel().getPunchesButton().
      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          PunchesDialog punchesDialog =
            new PunchesDialog(parent, song, cell.getPart());
          punchesDialog.setVisible(true);
        }
      });

      PartNotePane notePane = cell.getPartPanel().getNotePane();
      notePane.setText(part.getNotes());
      notePane.renderMarkdown(notePane.getText());

      cells.add(cell);
      cell.getPartPanel().updateIndex(cell.getPart().getIndex());

      //cell.getPartPanelCustomizer().reshapeRel(
      //    cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height);

      PartPanelCustomizer customizer = cell.getPartPanelCustomizer();
      customizer.registerComponentListener();
      customizer.registerPropertyChangeListener();

      RelativeTableConstraints constraints = 
        new RelativeTableConstraints(customizer, itl);

      CellBounds cellBounds = itBounds.next();
      Rectangle rect = new Rectangle(
          cellBounds.x,
          cellBounds.y,
          cellBounds.width,
          cellBounds.height);

      constraints.setAbsoluteBounds(rect);
      constraints.setRow(i);

      panSong.addCustomizer(cell.getPartPanelCustomizer(), constraints);

      customizer.setBounds(rect);

      cell.storePosition();

      i += cellBounds.height / 200;

      //DEBUG {{{
      logger.info(part.getName() + ": " + cellBounds.toString());
      //////////// }}}
    }
    repositionDividers(dividerLocations);

    repaint();
    revalidate();
  }

  /**
   * Re-position the split pane divider of each cell
   *
   * @param dividerLocations the list of divider locations
   */
  private void repositionDividers(List<Integer> dividerLocations) 
  {
    ListIterator<PartPanelWrapper> itCells = cells.listIterator();

    for (int i = 0; itCells.hasNext(); i++) {
      PartPanelWrapper cell = itCells.next();

      cell.getPartPanel().setSplitDividerLocation(dividerLocations.get(i).intValue());
    }
  }

  /**
   * Process beat value combo box selection
   *
   * @param selectedIndex the index of the selected item from the combo box
   * @return beat value associated with selection
   */
  private BeatValue getBeatValue(int selectedIndex)
  {
    BeatValue value;
    switch (selectedIndex) {
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
    return value;
  }

  /**
   * Adjust Part cells width to fill horizontal space
   *
   * @return the new cell width
   */
  private int adjustCellWidth()
  {
    int cellWidth = (int) (getContentPane().getSize().getWidth() - 
        ((Integer)(UIManager.get("ScrollBar.width"))).intValue());
    cellWidth -= panSong.getInsets().left + panSong.getInsets().right;
    cellWidth -= getInsets().left + getInsets().right;

    // offset
    cellWidth -= 7;

    itl.setColumnWidth(0, cellWidth);

    return cellWidth;
  }

  /**
   * Provide a clean exit when quit button is clicked
   */
  private void cleanExit()
  {
    if (hasUnsavedChanges()) {
      // TODO prompt();
    }
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
    // TODO compare data
    if (unsavedChanges) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Write Song to selected file
   *
   * @param file the file to write to
   */
  private void writeSongToFile(File file, PunchesFileHandler handler) 
  {
    // append .pnc extension
    String filePath = file.getAbsolutePath();
    if (!filePath.endsWith(".pnc")) {
      file = new File(filePath + ".pnc");
    }

    try (ObjectOutputStream out = 
        new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(handler);
        }
    catch (IOException e) {
      //TODO handle
      logger.error(e.getMessage(), e.getClass());
    }
  }

  /**
   * Read Song from File
   *
   * @param file the file to read from
   */
  private void readSongFromFile(File file) 
  {
    logger.info("loading song from file");

    try (ObjectInputStream in = 
        new ObjectInputStream(new FileInputStream(file))) {
      PunchesFileHandler handler = (PunchesFileHandler) in.readObject();

      panSong.setSong(handler.getSongData());
      refreshTable(handler.getCellBounds(), handler.getDividerLocations());
    }
    catch (IOException e) {
      //TODO handle
      logger.error(e.getMessage(), e.getClass());
    }
    catch (ClassNotFoundException e) {
      //TODO handle
      logger.error(e.getMessage(), e.getClass());
    }
    catch (ClassCastException e) {
      //TODO handle
      logger.error(e.getMessage(), e.getClass());
    }
  }

  /**
   * Write Song to PDF file
   *
   * @param pdfFile the Adobe PDF file to write to
   */
  private void writeToPDF(File pdfFile) 
  {
    // append .pdf extension
    String filePath = pdfFile.getAbsolutePath();
    if (!filePath.endsWith(".pdf")) {
      pdfFile = new File(filePath + ".pdf");
    }

    // clone song object to preserve notes
    Song newSong = new Song(panSong.getSong());

    ListIterator<Part> itParts = newSong.getParts().listIterator();
    ListIterator<PartPanelWrapper> itCells = cells.listIterator();
    while (itParts.hasNext() && itCells.hasNext()) {
      Part part = itParts.next();
      PartPanelWrapper cell = itCells.next();

      // set part notes to their raw html
      part.setNotes(
          cell.getPartPanel().getNotePane().getHTML());
    }
    try {
      PunchesPDFExporter pdfExporter = new PunchesPDFExporter(newSong);
      pdfExporter.prepare();
      pdfExporter.exportPDF(pdfFile);
    }
    catch (PunchesPDFExporter.HTMLNotRenderedException ex) {
      logger.error(ex.getMessage(), ex.getClass());
    }
    catch (IOException ex) {
      logger.error(ex.getMessage(), ex.getClass());
      //TODO handle
    }
    catch (Exception ex) {
      logger.error(ex.getMessage(), ex.getClass());
      //TODO handle
    }
  }

  ////////////////////
  // BUTTON METHODS //
  ////////////////////

  /**
   * Create a new Song
   */
  public void createSong()
  {
    if (hasUnsavedChanges()) {
      // TODO prompt();
    }
    else {
      panSong.setSong(new Song());
      panSong.removeAll();

      //DEBUG {{{
      logger.info("new song created");
      //////////// }}}

      initSongPanel();

      panSong.repaint();
      panSong.revalidate();
    }
  }

  /**
   * Load a Song from a file
   */
  public void loadSong()
  {
    JFileChooser chooser = new JFileChooser();

    chooser.setCurrentDirectory(
        chooser.getFileSystemView().getDefaultDirectory());
    chooser.setFileFilter(new FileNameExtensionFilter("Punches Files", "pnc"));
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    int choice = chooser.showOpenDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      File selectedFile = chooser.getSelectedFile();
      if (hasUnsavedChanges()) {
        //TODO prompt();
      }
      readSongFromFile(selectedFile);
    }
    else if (choice == JFileChooser.ERROR_OPTION) {
      //TODO: handle
    }
  }

  /**
   * Save the current Song to a file
   */
  public void saveSong()
  {
    ListIterator<PartPanelWrapper> itCells = cells.listIterator();
    while (itCells.hasNext()) {
      PartPanelWrapper cell = itCells.next();
      cell.storePosition();

      //DEBUG {{{
      logger.info("stored cell position: {}",
          cell.getStoredPosition().toString());
      //////////// }}}
    }

    JFileChooser chooser = new JFileChooser();

    chooser.setCurrentDirectory(
        chooser.getFileSystemView().getDefaultDirectory());
    chooser.setFileFilter(new FileNameExtensionFilter("Punches Files", "pnc"));
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    int choice = chooser.showSaveDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      File selectedFile = chooser.getSelectedFile();
      //TODO check if file exists, prompt to overwrite

      List<CellBounds> bounds = new ArrayList<>();
      List<Integer> dividerLocations = new ArrayList<>();

      itCells = cells.listIterator();
      while (itCells.hasNext()) {
        PartPanelWrapper cell = itCells.next();

        Rectangle storedPosition = cell.getStoredPosition();
        CellBounds cellBounds = new CellBounds(
            storedPosition.x,
            storedPosition.y,
            storedPosition.width,
            storedPosition.height);

        bounds.add(cellBounds);
        dividerLocations.add(
            Integer.valueOf(cell.getPartPanel().getSplitDividerLocation()));
      }

      PunchesFileHandler handler =
        new PunchesFileHandler(panSong.getSong(), bounds, dividerLocations);

      writeSongToFile(selectedFile, handler);
    }
    else if (choice == JFileChooser.ERROR_OPTION) {
      //TODO: handle
    }
  }

  /**
   * Export the song as a PDF
   */
  public void exportSong()
  {
    JFileChooser chooser = new JFileChooser();

    chooser.setCurrentDirectory(
        chooser.getFileSystemView().getDefaultDirectory());
    chooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    int choice = chooser.showSaveDialog(this);
    if (choice == JFileChooser.APPROVE_OPTION) {
      File selectedFile = chooser.getSelectedFile();
      writeToPDF(selectedFile);
    }
    else if (choice == JFileChooser.ERROR_OPTION) {
      //TODO: handle
    } 
  }

  /**
   * Add a new Part to the Song
   */
  public void addPart()
  {
    Song song = panSong.getSong();
    Part part = new Part();

    song.addPart(part);

    PartPanelWrapper cell = new PartPanelWrapper(part);

    cell.getPartPanel().getDeleteButton().
      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          removePart(cell.getPart().getIndex());
        }
      });

    cell.getPartPanelCustomizer().registerComponentListener();
    cell.getPartPanelCustomizer().registerPropertyChangeListener();

    cells.add(cell);

    song.refreshIndices();
    cell.getPartPanel().updateIndex(cell.getPart().getIndex());

    // calculate row to add cell to
    int row = 0;

    int y = 0;
    if (cells.size() > 1) {
      PartPanelCustomizer prevCell =
        cells.get(cells.size() - 2).getPartPanelCustomizer();

      y = prevCell.getY() + prevCell.getHeight();
    }

    panSong.addCustomizer(cell.getPartPanelCustomizer(),
        new RelativeTableConstraints(COLUMN, row, 1, 1,
          cell.getPartPanelCustomizer(), itl));

    PartPanelCustomizer customizer = cell.getPartPanelCustomizer();

    customizer.setY(y);

    panSong.repaint();
    panSong.revalidate();

    cell.storePosition();

    //DEBUG {{{
    logger.info("part added: ", cell.getPartPanel().getPart().getName());
    logger.info("initial pos: y=" +
        cell.getPartPanelCustomizer().getStoredYPos() +
        ", w=" +
        cell.getPartPanelCustomizer().getStoredWidth() + 
        ", h=" +
        cell.getPartPanelCustomizer().getStoredHeight());
    //////////// }}}
  }

  /**
   * Remove a Part from the Song
   *
   * @param index the index of the Part to remove
   */
  public void removePart(int index)
  {
    Song song = panSong.getSong();

    song.getParts().remove(index);
    panSong.remove(cells.get(index).getPartPanelCustomizer());
    cells.remove(index);

    song.refreshIndices();


    ListIterator<PartPanelWrapper> itCells = cells.listIterator();
    while (itCells.hasNext()) {
      PartPanelWrapper cell = itCells.next();
      cell.getPartPanel().updateIndex(cell.getPart().getIndex());
    }

    panSong.updateUI();
    repaint();
    revalidate();

    //DEBUG {{{
    logger.info("part removed");
    logger.debug("parts: {}, cells {}",
        panSong.getSong().getParts().size(), cells.size());
    //////////// }}}
  }

  ///////////////////////////////
  // ComponentListener Methods //
  ///////////////////////////////

  @Override
  public void componentHidden(ComponentEvent e) {};
  @Override
  public void componentShown(ComponentEvent e)  {};
  @Override
  public void componentMoved(ComponentEvent e)  {};

  /**
   * Dynamically resizes Part cells when frame is resized
   *
   * @param e the resize event
   */
  @Override
  /**
   * Fire a ComponentEvent when the frame is resized
   *
   * @param e - the ComponentEvent
   */
  public  void componentResized(ComponentEvent e) {
    //DEBUG {{{
    logger.debug("!! frame resize event"); 
    //////////// }}}

    if (initialized) {
      adjustCellWidth();
    }

    panSong.revalidate();
  };
}

