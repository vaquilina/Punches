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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.miginfocom.swing.MigLayout;

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
 * @version 04/11/22
 */
public class PunchesDialog extends JDialog implements KeyListener
{
  /* TODO: capture ends one 16th too early
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
  /** The collection of assembled rhythmic layers */
  private Map<String, Rhythm> layers;

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
        //TODO tab is created, registered to part
        //toTab(rhythm);
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

  ///**
  // * Get a tabulature representation of a Pattern
  // */
  //private String[] toTab(Rhythm rhythm)
  //{
  //  return new String[] {};
  //}
  
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
    // TODO save to midi file for inspection
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
    btnStop.setEnabled(false);

    btnPlay.setEnabled(true);
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

