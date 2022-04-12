package Punches;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

//import java.io.File;
//import java.io.IOException;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.miginfocom.swing.MigLayout;

//import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <p>
 * The "Punches Interface" - A dialog in which the user can key in a rhythm.
 * </p>
 * <pre>
 * +------------------------------------------+
 * |  Intro:         4 bars of 4/4 @ 120bpm   |
 * |  [PLAY] [REC] [STOP] ||||||||||||||---   |
 * |   ____________________________________   |
 * |  |                 |                  |  |
 * |  |    CRASH (C)    |      RIDE (R)    |  |
 * |  |                 |                  |  |
 * |  |-----------------+------------------|  |
 * |  |                 |                  |  |
 * |  |   HI-HAT (H)    |    RACK TOM (T)  |  |
 * |  |                 |                  |  |
 * |  |-----------------+------------------|  |
 * |  |                 |                  |  |
 * |  |    SNARE (S)    |   FLOOR TOM (F)  |  |
 * |  |                 |                  |  |
 * |  |-----------------+------------------|  |
 * |  |                                    |  |
 * |  |          BASS DRUM (SPACE)         |  |
 * |  |                                    |  |
 * |  '------------------------------------'  |
 * |            [TO TAB] [TO SHEET] [CANCEL]  |
 * +------------------------------------------+
 * </pre>
 * <hr />
 *
 * @author Vince Aquilina
 * @version 04/12/22
 */
public class PunchesDialog extends JDialog implements KeyListener
{
  /* TODO: playback ends one 16th too early
   */

  private final static Logger logger =
    LoggerFactory.getLogger(PunchesDialog.class);

  KeyboardFocusManager kfMgr =
    KeyboardFocusManager.getCurrentKeyboardFocusManager();

  /** The Part to which the result will be assigned */
  private Part relevantPart;
  /** The Song that the Part belongs to */
  private Song partOwner;

  /** The "play" button */
  private final JButton btnPlay;
  /** The "record" button */
  private final JButton btnRec;
  /** The "stop button */
  private final JButton btnStop;
  /** The "to tab" button */
  private final JButton btnToTab;
  /** The "to sheet" button */
  private final JButton btnToSheet;
  /** Map of voice buttons */
  private final Map<String, VoiceLabel> voices;
  /** The voice panel */
  private final JPanel pnlVoices;
  /** The metronome */
  private Metronome metronome;
  /** The metronome's progress bar */
  private static final JProgressBar prgMetronome =
    new JProgressBar(JProgressBar.HORIZONTAL);

  /** The set of keys currently depressed */
  private final Set<Character> pressed;

  /** The JFugue Player that will playback the voices */
  private Player player;
  /** Manages the sequence as it is constructed */
  private Recorder recorder;

  /**
   * Construct a PunchesDialog
   * @param owner the Frame owner of this dialog
   * @param partOwner the Song to which the Part belongs to
   * @param relevantPart the Part to which the result will be assigned
   */
  public PunchesDialog(Frame owner, Song partOwner, Part relevantPart)
  {
    super(owner, ModalityType.APPLICATION_MODAL);

    this.partOwner = partOwner;
    this.relevantPart = relevantPart;

    setTitle("Add Punches");

    player = new Player();

    /*
     * Meta Panel
     */

    final JLabel lblPartName = new JLabel("[" + relevantPart.getName() + "]");

    final JLabel lblInfo =
      new JLabel(relevantPart.getLengthInBars() + " bars of " +
            partOwner.getSignature().toString() + 
            " @  " + partOwner.getBpm() + " bpm");

    btnPlay = new JButton("PLAY");
    btnPlay.setFocusable(false);
    btnPlay.setMnemonic(KeyEvent.VK_P);
    btnPlay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        play();
      }
    });
    btnPlay.setEnabled(false);

    btnRec = new JButton("REC");
    btnRec.setFocusable(false);
    btnRec.setMnemonic(KeyEvent.VK_R);
    btnRec.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        record();
      }
    });

    btnStop = new JButton("STOP");
    btnStop.setFocusable(false);
    btnStop.setMnemonic(KeyEvent.VK_S);
    btnStop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        stop();
      }
    });
    btnStop.setEnabled(false); // disabled until metronome is started

    prgMetronome.setStringPainted(true);

    final JPanel pnlMeta = new JPanel(new MigLayout(
          "Insets 0, fillx",
          "[fill][fill]",
          "[fill][fill]"));
    pnlMeta.add(lblPartName,  "cell 0 0");
    pnlMeta.add(lblInfo,      "cell 1 0");
    pnlMeta.add(btnPlay,      "cell 0 1");
    pnlMeta.add(btnRec,       "cell 0 1");
    pnlMeta.add(btnStop,      "cell 0 1");
    pnlMeta.add(prgMetronome, "cell 1 1");

    /*
     * Voices Panel
     */

    voices = new LinkedHashMap<>() {{
      put("crash",    new VoiceLabel("CRASH (C)"));
      put("ride",     new VoiceLabel("RIDE (R)"));
      put("hihat",    new VoiceLabel("HI-HAT (H)"));
      put("racktom",  new VoiceLabel("RACK TOM (T)"));
      put("snare",    new VoiceLabel("SNARE (S)"));
      put("floortom", new VoiceLabel("FLOOR TOM (F)"));
      put("kickdrum", new VoiceLabel("KICK DRUM (SPACE)"));
    }};

    pnlVoices = new JPanel(new MigLayout(
          "Insets 0, gap 0, wrap 2", "[fill][fill]", "fill"));
    pnlVoices.add(voices.get("crash"),    "w 100%, h 100%, grow");
    pnlVoices.add(voices.get("ride"),     "w 100%");
    pnlVoices.add(voices.get("hihat"),    "w 100%");
    pnlVoices.add(voices.get("racktom"),  "w 100%");
    pnlVoices.add(voices.get("snare"),    "w 100%");
    pnlVoices.add(voices.get("floortom"), "w 100%");
    pnlVoices.add(voices.get("kickdrum"), "span");

    pressed = new HashSet<Character>();

    Action disableAction = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) { /* do nothing */ }
    };

    for (VoiceLabel voice : voices.values()) {
      voice.addKeyListener(this);
      voice.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "disable");
      voice.getActionMap().put("disable", disableAction);
    }


    /*
     * Button Panel
     */

    btnToTab = new JButton("TO TAB");
    btnToTab.setFocusable(false);
    btnToTab.setMnemonic(KeyEvent.VK_T);
    btnToTab.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        toTab();
        dispose();
      }
    });
    btnToTab.setEnabled(false); // disabled until Sequence captured

    btnToSheet = new JButton("TO SHEET");
    btnToSheet.setFocusable(false);
    btnToSheet.setMnemonic(KeyEvent.VK_H);
    btnToSheet.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO sheet is created, registered to part
        //toSheet(rhythm);
      }
    });
    btnToSheet.setEnabled(false); // disabled until Sequence captured

    final JButton btnCancel = new JButton("CANCEL");
    btnCancel.setFocusable(false);
    btnCancel.setMnemonic(KeyEvent.VK_C);
    btnCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (! Objects.isNull(metronome)) {
          metronome.end();
          player.getManagedPlayer().reset();
        }
        dispose();
      }
    });
    final JPanel pnlButtons = new JPanel(new MigLayout("Insets 0, align right"));

    pnlButtons.add(btnToTab);
    pnlButtons.add(btnToSheet);
    pnlButtons.add(btnCancel);

    /*
     * Frame
     */

    setLayout(new MigLayout(
          "Insets 10, align 50% 50%",
          "[fill]",
          "[][][]"));
    add(pnlMeta,    "cell 0 0");
    add(pnlVoices,  "cell 0 1");
    add(pnlButtons, "cell 0 2");

    pack();

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(getWidth(), getHeight()));
    setAlwaysOnTop(true);
    setLocationRelativeTo(null);
  }

  /**
   * Update the progress bar
   * @param progress the progress value
   * @param totalBeats the total number of beats in the part
   */
  public static synchronized void updateProgress(int progress, int totalBeats)
  {
    double temp = (float) progress / (float) totalBeats;
    progress = (int) (temp * 100);

    prgMetronome.setValue(progress);

    logger.debug("progress %: " + progress);
  }

  /**
   * Toggle progress bar text for metronome count-in
   * @param countingIn whether the metronome is counting in
   */
  public static synchronized void setProgressMode(boolean countingIn)
  {
    if (countingIn) {
      prgMetronome.setString("counting in");
    } else {
      prgMetronome.setString(null);
    }
  }

  ////////////////////
  // HELPER METHODS //
  ////////////////////

  /**
   * Generate a tabulature representation of a Rhythm.
   *
   * Tabulature Format
   *
   * 0 - count guide
   * 1 - crash    - CR
   * 2 - ride     - RD
   * 3 - hihat    - HH
   * 4 - racktom  - RT
   * 5 - snare    - SN
   * 6 - floortom - FT
   * 8 - kick     - BD
   */
  private void toTab()
  {
    String[] tabSnippet = new String[8];
    StringBuilder[] layers = recorder.getLayerBuilders();

    // HACK ------------------------------
    // where is the extra beat coming from?
    for (StringBuilder line : layers) {
      line.delete(line.length() - 4, line.length());
    }
    // -----------------------------------

    // count guide
    int numOfBars = relevantPart.getLengthInBars();
    StringBuilder countGuide = new StringBuilder(" ");
    for (int i = 0; i < numOfBars; i++) {
      for (int j = 1; j <= partOwner.getSignature().getBeatsPerBar(); j++) {
        countGuide.append(Character.forDigit(j, 10)); // 10=decimal radix
        countGuide.append("e&a");
      }
      if (i < numOfBars - 1) {
        countGuide.append(" ");
      }
    }
    countGuide.insert(0, "## ");
    tabSnippet[0] = countGuide.toString();

    /* bar lines */

    for (StringBuilder layer : layers) {
      layer.insert(0, '|');
      int j = 1;
      for (int i = 0; i < layer.length(); i++) {
        if (i > 0 &&
            i % (partOwner.getSignature().getBeatsPerBar()
             * recorder.getResolution()) == 0) {
          layer.insert(i + j, '|');
          j++;
        }
      }
    }

    // labels
    layers[3].insert(0, "CR "); // crash
    layers[4].insert(0, "RD "); // ride
    layers[2].insert(0, "HH "); // hihat
    layers[5].insert(0, "RT "); // racktom
    layers[1].insert(0, "SN "); // snare
    layers[6].insert(0, "FT "); // floortom
    layers[0].insert(0, "BD "); // kick

    /* replace symbols */

    // rests '.' -> '-'
    for (StringBuilder layer : layers) {
      while (layer.indexOf(".") > -1) {
        int cursor = layer.indexOf(".");
        layer.setCharAt(cursor, '-');
      }
    }

    // crash layer '*' -> 'X'
    while (layers[3].indexOf("*") > -1) {
      int cursor = layers[3].indexOf("*");
      layers[3].setCharAt(cursor, 'X');
    }

    // ride layer 'r' -> 'x'
    while (layers[4].indexOf("r") > -1) {
      int cursor = layers[4].indexOf("r");
      layers[4].setCharAt(cursor, 'x');
    }

    // hihat layer '`' -> 'x'
    while (layers[2].indexOf("`") > -1) {
      int cursor = layers[2].indexOf("`");
      layers[2].setCharAt(cursor, 'x');
    }

    // racktom layer 't' -> 'o'
    while (layers[5].indexOf("t") > -1) {
      int cursor = layers[5].indexOf("t");
      layers[5].setCharAt(cursor, 'o');
    }

    // snare layer 's' -> 'o'
    while (layers[1].indexOf("s") > -1) {
      int cursor = layers[1].indexOf("s");
      layers[1].setCharAt(cursor, 'o');
    }

    // floortom layer 'f' -> 'o'
    while (layers[6].indexOf("f") > -1) {
      int cursor = layers[6].indexOf("f");
      layers[6].setCharAt(cursor, 'o');
    }

    // kick layer
    // already uses correct symbol

    /* finalize strings */
    tabSnippet[1] = layers[3].toString(); // crash
    tabSnippet[2] = layers[4].toString(); // ride
    tabSnippet[3] = layers[2].toString(); // hihat
    tabSnippet[4] = layers[5].toString(); // racktom
    tabSnippet[5] = layers[1].toString(); // snare
    tabSnippet[6] = layers[6].toString(); // floortom
    tabSnippet[7] = layers[0].toString(); // kick

    // DEBUG
    logger.info("Generated tab snippet");
    for (String line : tabSnippet) {
      logger.debug(line);
    }

    /* assign to part */
    relevantPart.setTabSnippet(tabSnippet);
  }
  
  ///**
  // * Get a sheet music representation of a Rhythm
  // */
  //private Image toSheet(Rhythm rhythm)
  //{
  //}

  /**
   * Playback the sequence
   */
  private void play()
  {
    Pattern pattern = recorder.getModifiedPattern();

    //// DEBUG
    //try {
    //  File midiFile = new File("/home/vince/punches_output.mid");
    //  if (! midiFile.exists()) {
    //    midiFile.createNewFile();
    //  }
    //  MidiFileManager.savePatternToMidi(pattern, midiFile);
    //} catch (IOException ex) {
    //  logger.error(ex.getMessage());
    //  ex.printStackTrace();
    //}
    logger.debug(pattern.toString());

    Timer timer = new Timer();
    TimerTask playerTask = new TimerTask() {
      @Override
      public void run() {
        player = new Player();
        player.delayPlay(100, pattern);
      }
    };
    timer.schedule(playerTask, 0);
  }

  /**
   * Initialize and start the metronome; start recording
   */
  private void record()
  {
    TimeSignature signature = partOwner.getSignature();
    double bpm = (double) partOwner.getBpm();
    int beatsPerBar = signature.getBeatsPerBar();
    int numOfBars = relevantPart.getLengthInBars();

    metronome = new Metronome(bpm, beatsPerBar, numOfBars);

    Thread t = new Thread(metronome);
    t.start();

    btnRec.setEnabled(false);
    btnStop.setEnabled(true);

    recorder = new Recorder((int) bpm, signature, numOfBars);
    metronome.addMetronomeListener(recorder);
  }

  /**
   * Stop the metronome
   */
  private void stop()
  {
    metronome.end();
    player.getManagedPlayer().reset();

    prgMetronome.setValue(0);

    btnRec.setEnabled(true);
    btnPlay.setEnabled(true);
    btnToTab.setEnabled(true);
  }

  /**
   * Highlight the voices corresponding to the keys that were just depressed
   */
  private void processKeys()
  {
    if (recorder != null) {
      recorder.registerHit(pressed);
    }

    for (Character c : pressed) {
      c = Character.toLowerCase(c);
      switch (c) {
        case 'c':
          voices.get("crash").blink();
          logger.debug("hit crash");
          break;
        case 'r':
          voices.get("ride").blink();
          logger.debug("hit ride");
          break;
        case 'h':
          voices.get("hihat").blink();
          logger.debug("hit hihat");
          break;
        case 't':
          voices.get("racktom").blink();
          logger.debug("hit racktom");
          break;
        case 's':
          voices.get("snare").blink();
          logger.debug("hit snare");
          break;
        case 'f':
          voices.get("floortom").blink();
          logger.debug("hit floortom");
          break;
        case ' ':
          voices.get("kickdrum").blink();
          logger.debug("hit kickdrum");
          break;
      }
    }
  }

  /////////////////////////
  // KeyListener Methods //
  /////////////////////////

  private final String VALID_CHARS = "crhtsf CRHTSF";

  @Override
  public void keyPressed(KeyEvent e)
  {
    // check for character of interest
    if (! (VALID_CHARS.indexOf(String.valueOf(e.getKeyChar())) < 0))  {
      pressed.add(e.getKeyChar());
    }
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    if (pressed.isEmpty()) {
      return;
    } else {
      processKeys();

      logger.debug("keys entered: " + pressed.toString());
    }
    pressed.clear();
  }

  @Override
  public void keyTyped(KeyEvent e) { /* not used */ }

  ////////////////
  // VoiceLabel //
  ////////////////

  /**
   * JLabel that represents the voices on the drum kit.
   */
  private class VoiceLabel extends JLabel
  {
    /** The normal background color */
    private final Color normalBgColor = Color.LIGHT_GRAY;
    /** The normal foreground color */
    private final Color normalFgColor = Color.BLACK;
    /** The background color when active */
    private final Color activeBgColor = Color.BLACK;
    /** The foreground color when active */
    private final Color activeFgColor = Color.WHITE;

    /**
     * Construct a VoiceLabel
     * @param label the label text
     */
    public VoiceLabel(String label) 
    {
      super(label);

      setMinimumSize(new Dimension(200, 100));

      setBorder(new EtchedBorder(EtchedBorder.LOWERED));
      setOpaque(true);
      setFocusable(true);

      setHorizontalAlignment(SwingConstants.CENTER);
      setBackground(normalBgColor);
      setForeground(normalFgColor);
    }

    /**
     * Visually indicate that the voice is active 
     */
    public void blink()
    {
      VoiceLabel curLabel = this;
      Timer timer = new Timer();

      TimerTask activeTask = new TimerTask() {
        @Override
        public void run()
        {
          curLabel.setBackground(activeBgColor);
          curLabel.setForeground(activeFgColor);
        }
      };

      TimerTask inactiveTask = new TimerTask() {
        @Override
        public void run()
        {
          curLabel.setBackground(normalBgColor);
          curLabel.setForeground(normalFgColor);
        }
      };

      timer.schedule(activeTask, 0);
      timer.schedule(inactiveTask, 100);
    }
  }
}

