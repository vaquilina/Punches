package Punches;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
//import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import java.util.Iterator;
import java.util.LinkedHashMap; // maintains order of keys
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

//import org.softsmithy.lib.swing.customizer.JCustomizer;
import org.softsmithy.lib.swing.customizer.layout.InfiniteTableLayout;
import org.softsmithy.lib.swing.customizer.layout.RelativeTableConstraints;

/**
 * @author Vince Aquilina
 * @version 03/09/22
 *
 * Punches Desktop GUI.
 *
 * TODO: Write tests
 */
public class PunchesFrame extends JFrame implements ComponentListener
{
  //TODO: implement clipboard
  //private Clipboard internalClipboard;      // for yank/put Part
  //private Clipboard externalClipboard;      // for yank/put text or image

  private SongPanel panSong;                  // panel containing parts
  private InfiniteTableLayout itl;            // layout manager for panSong
  private JScrollPane scroller;               // scrollpane containing panSong

  private List<PartPanelCustomizer> wrappers;         // part panel wrappers
  private List<PartPanel> panels;             // part panels

  // Flags
  private boolean unsavedChanges;
  private boolean initialized;
  private boolean debugging;

  //DEBUG {{{
  private int step = 0;
  //////////// }}}

  // Colors
  Color panelGray = new Color(0xDDDDDD);
  Color apricot = new Color(0xFFCCB3);

  /**
   * @param title - the window title
   *
   * Constructs the main content pane
   */
  public PunchesFrame(String title)
  {
    super(title);

    //DEBUG {{{
    debugging = true;
    //////////// }}}

    KeyboardFocusManager kfMgr = 
      KeyboardFocusManager.getCurrentKeyboardFocusManager();

    unsavedChanges = false;

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
    this.setLayout(new MigLayout("Insets 5"));

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    /*
     * Toolbar
     * TODO: link to source (http://www.famfamfam.com/) on about dialog
     */

    // Toolbar Buttons
    Map<String, ImageIcon> toolbarIcons = initToolbarIcons();

    JPanel toolbar = new JPanel(new MigLayout("Insets 0"));

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
    JTextField txtSongTitle = new JTextField("Song Title", 30);

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
    JLabel lblTimeSignature = new JLabel("time signature:");
    lblTimeSignature.setLabelFor(txtBeatsPerBar);
    lblTimeSignature.setDisplayedMnemonic(KeyEvent.VK_S);

    JLabel lblSlash = new JLabel("/");

    // Music Note Icons
    Map<String, ImageIcon> musicNotes = initMusicNoteIcons();

    // load into vector for use with JComboBox
    Vector<ImageIcon> musicNoteIcons = new Vector<>(musicNotes.values());

    JComboBox<ImageIcon> cmbValueOfABeat = new JComboBox<>(musicNoteIcons);
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
    JTextField txtBpm = new JTextField("120", 3);

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
    toolbar.add(lblSongTitle);
    toolbar.add(txtSongTitle, "w 150!, h 24!");
    toolbar.add(lblTimeSignature);
    toolbar.add(txtBeatsPerBar, "w 24!, h 24!");
    toolbar.add(lblSlash);
    toolbar.add(cmbValueOfABeat, "w 48!, h 24!");
    toolbar.add(lblBpm);
    toolbar.add(txtBpm, "w 30!, h 24!");
    toolbar.add(new JSeparator(JSeparator.VERTICAL), "h 24!");
    toolbar.add(toolbarButtons.get("About"), "w 24!, h 24!"); 
    toolbar.add(toolbarButtons.get("Quit"), "w 24!, h 24!, wrap");

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
            /* TODO: launch about dialog (my credits,
             * icon credits, lib credits, adobe logo, donate button) */
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
    if (debugging) {
      step++;
      System.out.println(step + ":PunchesFrame()\n" +
          "parts: " + panSong.getSong().getParts().size() + "\n");
    }
    //////////// }}}

    scroller = new JScrollPane(
        panSong,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroller.getVerticalScrollBar().setUnitIncrement(15);

    // add major components to frame
    getContentPane().add(toolbar, "span");
    getContentPane().add(scroller, "grow, w 100%, h 100%");

    // resize window to size of contents
    pack();    // necessary in order to get size of frame

    int cellWidth = (int) (getContentPane().getSize().getWidth() - 
        ((Integer)(UIManager.get("ScrollBar.width"))).intValue());
    cellWidth -= panSong.getInsets().left + panSong.getInsets().right;
    cellWidth -= getInsets().left + getInsets().right;

    itl = new InfiniteTableLayout(cellWidth, 200, panSong);
    panSong.setCustomizerLayout(itl);

    initPartPanels(panSong.getSong().getParts());

    // adjust initial window height
    this.setBounds(getX(), getY(), getWidth(), 800); 

    // listen for resize events
    addComponentListener(this);
  }

  //////////////////
  // INIT METHODS //
  //////////////////

  /**
   * Load toolbar icon resources
   *
   * @return - A Map containing toolbar icon resources
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
   * @return - A Map containing music note icon resources
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
   * TODO: store dimensions, restore when repainted
   *
   * @param parts - a List containing the Song's Parts
   */
  private void initPartPanels(List<Part> parts)
  {
    panels = new LinkedList<>();
    wrappers = new LinkedList<>();

    panSong.removeAll();

    panSong.getSong().refreshIndices();

    final Iterator<Part> itParts = parts.listIterator();
    for(int i = 0; itParts.hasNext(); i++) {
      PartPanel panel = new PartPanel(itParts.next());
      panels.add(panel);
      panel.setParentFrame(this);
      wrappers.add(new PartPanelCustomizer(panel));

      PartPanelCustomizer wrapper = wrappers.get(i);

      makeEditable(wrapper);

      panSong.addCustomizer(wrapper, 
          new RelativeTableConstraints(0, i, 1, 1, wrapper, itl));

      panel.positionDivider();
    }

    initialized = true;

    //DEBUG {{{
    if (debugging) {
      step++;
      System.out.println(step + ":initPartPanels()\n"     +
                             "parts:\t" + parts.size()    + "\n" +
                             "panels:\t" + panels.size()  + "\n" +
                             "wrprs:\t" + wrappers.size() + "\n" +
                             "\n*initialized*\n");
    }
    //////////// }}}
  }

  ////////////////////
  // HELPER METHODS //
  ////////////////////

  /**
   * Process beat value combo box selection
   *
   * @return - beat value associated with selection
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
   * Adjust Part cell width to fit window
   */
  private void adjustCellWidth()
  {
    int cellWidth = (int) (getContentPane().getSize().getWidth() - 
        ((Integer)(UIManager.get("ScrollBar.width"))).intValue());
    cellWidth -= panSong.getInsets().left + panSong.getInsets().right;
    cellWidth -= getInsets().left + getInsets().right;

    itl.setColumnWidth(0, cellWidth - 7);
  }

  /**
   * Add "double-click to edit" functionality to PartPanels.<br />
   * "Editable" panels change to an apricot color. 
   *
   * @param customizer - the PartPanelCustomizer wrapper object
   */
  private void makeEditable(PartPanelCustomizer cell)
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

        //DEBUG {{{
        if (debugging) {
          step++;
          System.out.println(step + ":makeEditable()");
          System.out.println("editing? " + editing + "\n");
        }
        //////////// }}}
      }
    });
  }

  /**
   * Provide a clean exit when quit button is clicked
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

  ////////////////////
  // BUTTON METHODS //
  ////////////////////

  /**
   * Create a new Song
   *
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
      if (debugging) {
        step++;
        System.out.println(step + ":createSong()\n");
        printPartList(); 
      }
      //////////// }}}

      initPartPanels(panSong.getSong().getParts());

      panSong.repaint();
      panSong.revalidate();
    }
  }

  /**
   * Add a new Part to the Song
   */
  public void addPart()
  {
    //DEBUG {{{
    if (debugging) {
      step++;
      System.out.println(step + ":addPart()");
      printPartList(); 
    }
    //////////// }}}
    Song song = panSong.getSong();

    Part part = new Part();
    song.addPart(part);
    PartPanel panel = new PartPanel(part);
    panels.add(panel);
    panel.setParentFrame(this);
    wrappers.add(new PartPanelCustomizer(panel));

    song.refreshIndices();

    int rowIndex = wrappers.size() - 1;
    makeEditable(wrappers.get(rowIndex));
    panSong.addCustomizer(wrappers.get(rowIndex),
        new RelativeTableConstraints(wrappers.get(rowIndex), itl));

    wrappers.get(rowIndex).setY(wrappers.get(rowIndex - 1).getY() + 
        wrappers.get(rowIndex - 1).getHeight());

    panSong.revalidate();
  }

  /**
   * Removes a Part from the Song
   *
   * @param part - the Part to remove
   */
  public void removePart(int index)
  {
    Song song = panSong.getSong();

    song.getParts().remove(index);
    song.refreshIndices();

    panSong.removeAll();

    initPartPanels(song.getParts());

    panSong.repaint();
    panSong.revalidate();

    //DEBUG {{{
    step++;
    if (debugging) {
      System.out.println(step + ":removePart()\n");
      printPartList(); 
    }
    //////////// }}}
  }

  //DEBUG {{{
  public void printPartList() 
  {
    System.out.println("\n----");
    for (Part part : panSong.getSong().getParts()) {
      System.out.println(part.getIndex() + ": " + part.getName());
    }
    System.out.println("----\n");
  }
  //////////// }}}

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
  public  void componentResized(ComponentEvent e) {
    //DEBUG {{{
    if (debugging) {
      step++;
      System.out.println(step + ": !! resize event\n"); 
    }
    //////////// }}}

    if (initialized) {
      adjustCellWidth();
    }

    panSong.revalidate();
  };
}

