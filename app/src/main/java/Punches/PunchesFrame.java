package Punches;

import java.util.Map;
import java.util.LinkedHashMap; // maintains order of keys
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JSeparator;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
//import java.awt.datatransfer.Clipboard;

import net.miginfocom.swing.MigLayout;
import org.softsmithy.lib.swing.customizer.JCustomizer;
import org.softsmithy.lib.swing.customizer.layout.InfiniteTableLayout;
import org.softsmithy.lib.swing.customizer.layout.RelativeTableConstraints;

/**
 * @author Vince Aquilina
 * @version Wed 02 Feb 2022 11:02:56 PM
 *
 * The main JFrame containing the app.
 */
public class PunchesFrame extends JFrame implements ComponentListener
{
  //TODO: implement clipboard
  //private Clipboard internalClipboard;      // for yank/put Part
  //private Clipboard externalClipboard;      // for yank/put text or image
  SongPanel panSong;
  InfiniteTableLayout itl;

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
    int cellWidth = (int) (getContentPane().getSize().getWidth() - ((Integer)(UIManager.get("ScrollBar.width"))).intValue());
    cellWidth -= panSong.getInsets().left + panSong.getInsets().right;
    cellWidth -= getInsets().left + getInsets().right;

    itl = new InfiniteTableLayout(cellWidth - 1, 200, panSong);
    panSong.setCustomizerLayout(itl);
    panSong.validate();

    JCustomizer[] cells = panSong.getCustomizers();
    for (JCustomizer cell : cells) {
      panSong.addCustomizer(cell, new RelativeTableConstraints(0, 0, 1, 1, cell, itl));
    }
  };

  /**
   * @param title - the window title
   *
   * Constructs the main content pane
   */
  public PunchesFrame(String title)
  {
    super(title);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception ex) {
      System.out.println("error setting look and feel");
      ex.printStackTrace();
    }
    this.setLayout(new MigLayout("Insets 5"));

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    /* Toolbar
     * TODO: link to source (http://www.famfamfam.com/) on about dialog
     */
    Map<String, ImageIcon> toolbarIcons = new LinkedHashMap<>();
    toolbarIcons.put("New Song", new ImageIcon(PunchesFrame.class.getResource("/icons/page_add.png")));
    toolbarIcons.put("Load Song", new ImageIcon(PunchesFrame.class.getResource("/icons/folder.png")));
    toolbarIcons.put("Save Song", new ImageIcon(PunchesFrame.class.getResource("/icons/disk.png")));
    toolbarIcons.put("Export to PDF directly", new ImageIcon(PunchesFrame.class.getResource("/icons/page_white_acrobat.png")));
    toolbarIcons.put("Cut Selection", new ImageIcon(PunchesFrame.class.getResource("/icons/cut.png")));
    toolbarIcons.put("Copy Selection", new ImageIcon(PunchesFrame.class.getResource("/icons/page_copy.png")));
    toolbarIcons.put("Paste Selection", new ImageIcon(PunchesFrame.class.getResource("/icons/page_paste.png")));
    toolbarIcons.put("Undo Last Action", new ImageIcon(PunchesFrame.class.getResource("/icons/arrow_undo.png")));
    toolbarIcons.put("Redo Last Action", new ImageIcon(PunchesFrame.class.getResource("/icons/arrow_redo.png")));
    toolbarIcons.put("Add Part", new ImageIcon(PunchesFrame.class.getResource("/icons/add.png")));
    toolbarIcons.put("About", new ImageIcon(PunchesFrame.class.getResource("/icons/help.png")));

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
      public void focusGained(FocusEvent e)
      {
        if (txtSongTitle.getText().equals("Song Title")) {
          txtSongTitle.setText("");
          txtSongTitle.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        }
      }
      public void focusLost(FocusEvent e)
      {
        if (txtSongTitle.getText().equals("")) {
          txtSongTitle.setText("Song Title");
          txtSongTitle.setFont(new Font(Font.SERIF, Font.ITALIC, 12));
        }
      }
    });
    // TODO: listen for enter key & focus lost, assign text to songTitle property

    Map<String, ImageIcon> musicNotes = new LinkedHashMap<>();
    musicNotes.put("whole", new ImageIcon(PunchesFrame.class.getResource("/icons/music-note-1_16px.png")));
    musicNotes.put("half", new ImageIcon(PunchesFrame.class.getResource("/icons/music-note-2_16px.png")));
    musicNotes.put("quarter", new ImageIcon(PunchesFrame.class.getResource("/icons/music-note-4_16px.png")));
    musicNotes.put("eighth", new ImageIcon(PunchesFrame.class.getResource("/icons/music-note-8_16px.png")));
    musicNotes.put("sixteenth", new ImageIcon(PunchesFrame.class.getResource("/icons/music-note-16_16px.png")));
    musicNotes.put("thirty-second", new ImageIcon(PunchesFrame.class.getResource("/icons/music-note-32_16px.png")));
    Vector<ImageIcon> musicNoteIcons = new Vector<>(musicNotes.values());

    JLabel lblTimeSignature = new JLabel("time signature:");

    JTextField txtBeatsPerBar = new JTextField("4", 2);
    // TODO: assign to beatsPerBar property on enter or focus lost

    JLabel lblSlash = new JLabel("/");

    JComboBox<ImageIcon> cmbValueOfABeat = new JComboBox<>(musicNoteIcons);
    cmbValueOfABeat.setSelectedIndex(2); // defaults to quarter note
    DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
    listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
    cmbValueOfABeat.setRenderer(listRenderer);
    // TODO: assign to beatsPerBar property on selection

    JLabel lblBpm = new JLabel("bpm:");
    JTextField txtBpm = new JTextField("120", 3);
    // TODO: assign to bpm property on enter or focus lost

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
    toolbar.add(toolbarButtons.get("About"), "w 24!, h 24!, wrap"); 
    // TODO: launch about dialog (my credits, icon credits, lib credits, adobe logo donate button)

    /*
     * Song Panel
     */
    panSong = new SongPanel(new Song());
    panSong.setBackground(new Color(0xFFFFFF));
    JScrollPane scroller = new JScrollPane(panSong, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    /*
     * initialize layout
     */
    getContentPane().add(toolbar, "span");
    getContentPane().add(scroller, "grow, w 100%, h 100%");
    pack();    // necessary in order to get size of frame

    /*
     * Song Panel (cont'd)
     */
    int cellWidth = (int) (getContentPane().getSize().getWidth() - ((Integer)(UIManager.get("ScrollBar.width"))).intValue());
    cellWidth -= panSong.getInsets().left + panSong.getInsets().right;
    cellWidth -= getInsets().left + getInsets().right;

    // TODO: Attribute softsmithy
    itl = new InfiniteTableLayout(cellWidth - 1, 200, panSong);
    panSong.setCustomizerLayout(itl);

    this.setBounds(getX(), getY(), getWidth(), 800); // adjust initial window height
    addComponentListener(this); // listen for resize events

    JCustomizer testPart = new JCustomizer(new PartPanel(new Part()));
    panSong.addCustomizer(testPart, new RelativeTableConstraints(0, 0, 1, 1, testPart, itl));
  }
}
