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
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <p>
 * The "Punches Interface" - A dialog in which the user can key in a rhythm.
 * </p>
 * <pre>
 * +------------------------------------------+
 * | Intro: 4 bars of 4/4 @ 120bpm            |
 * | [PLAY] [STOP] |||||||||||||||||||||----  |
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
 * @version 03/26/22
 */
public class PunchesDialog extends JDialog implements KeyListener
{
  /*
   * TODO: keybindings (multi-key simulataneous input)
   * TODO: capture
   * TODO: conversion
   */
  private final static Logger logger =
    LoggerFactory.getLogger(PunchesDialog.class);

  KeyboardFocusManager kfMgr =
    KeyboardFocusManager.getCurrentKeyboardFocusManager();

  /** The MIDI sequence */
  private Sequence sequence;
  /** The MIDI track; */
  private Track track;
  /** The quantized rhythm */
  private Rhythm rhythm;
  /** The Part to which the rhythm will be assigned */
  private Part relevantPart;
  /** The Song that the Part belongs to */
  private Song partOwner;

  /** The "play" button */
  private final JButton btnPlay;
  /** The "stop button */
  private final JButton btnStop;
  /** The "to tab" button */
  private final JButton btnToTab;
  /** The "to sheet" button */
  private final JButton btnToSheet;
  /** Map of voice buttons */
  private final Map<String, VoiceButton> voices;
  /** The voice panel */
  private final JPanel pnlVoices;
  /** The metronome */
  private Metronome metronome;
  /** The metronome's progress bar */
  private static final JProgressBar prgMetronome =
    new JProgressBar(JProgressBar.HORIZONTAL);

  /** The set of keys currently depressed */
  private final Set<Character> pressed;


  /**
   * Construct a PunchesDialog
   *
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

    /*
     * Meta Panel
     */

    final JLabel lblPartName = new JLabel("[" + relevantPart.getName() + "]");

    final JLabel lblInfo =
      new JLabel(relevantPart.getLengthInBars() + " bars of " +
            partOwner.getSignature().toString() + 
            " @  " + partOwner.getBpm() + " bpm");

    btnPlay = new JButton("PLAY/REC");
    btnPlay.setFocusable(false);
    btnPlay.setMnemonic(KeyEvent.VK_P);
    btnPlay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO: start recording
        play();
      }
    });

    btnStop = new JButton("STOP");
    btnStop.setFocusable(false);
    btnStop.setMnemonic(KeyEvent.VK_S);
    btnStop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO: discard recording
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
    pnlMeta.add(btnStop,      "cell 0 1");
    pnlMeta.add(prgMetronome, "cell 1 1");

    /*
     * Voices Panel
     */

    voices = new LinkedHashMap<>() {{
      put("crash",    new VoiceButton("CRASH (C)"));
      put("ride",     new VoiceButton("RIDE (R)"));
      put("hihat",    new VoiceButton("HI-HAT (H)"));
      put("racktom",  new VoiceButton("RACK TOM (T)"));
      put("snare",    new VoiceButton("SNARE (S)"));
      put("floortom", new VoiceButton("FLOOR TOM (F)"));
      put("kickdrum", new VoiceButton("KICK DRUM (SPACE)"));
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

    for (VoiceButton voice : voices.values()) {
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
        toTab(rhythm);
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
        if (metronome != null) {
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

    setLocationRelativeTo(null);
  }

  /**
   * Update the progress bar
   *
   * @param progress the progress value
   */
  public static synchronized void updateProgress(int progress, int totalBeats)
  {
    double temp = (float) progress / (float) totalBeats;
    progress = (int) (temp * 100);

    prgMetronome.setValue(progress);

    logger.debug("temp: " + temp);
    logger.debug("progress %: " + progress);
  }

  /**
   * Toggle progress bar text for metronome count-in
   *
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
   * Create a Rhythm from a MIDI sequence
   *
   * @return the newly created rhythm
   */
  //private Rhythm generateRhythm()
  //{
  //  // TODO implement method
  //  return new Rhythm();
  //}

  /**
   * Capture a sequence
   *
   * @return the captured sequence
   */
  //private Sequence captureSequence()
  //{
  //  // TODO implement method
  //  // PPQ indicates tempo-based timing (pulses per quarter)

  //  return new Sequence(Sequence.PPQ, 4);
  //  // will want to use higher resolution, then quantize down
  //}

  /**
   * Get a tabulature representation of a Rhythm
   */
  private String[] toTab(Rhythm rhythm)
  {
    //TODO: implement method
    return new String[] {};
  }
  
  /**
   * Get a sheet music representation of a Rhythm
   */
  //private Image toSheet(Rhythm rhythm)
  //{
  //}

  /**
   * Initialize and start the metronome
   */
  private void play()
  {
    metronome = new Metronome(
          (double) partOwner.getBpm(),
          partOwner.getSignature().getBeatsPerBar(),
          relevantPart.getLengthInBars());

    Thread t = new Thread(metronome);
    t.start();

    btnPlay.setEnabled(false);
    btnStop.setEnabled(true);
  }

  /**
   * Stop the metronome
   */
  private void stop()
  {
    metronome.end();

    prgMetronome.setValue(0);

    btnPlay.setEnabled(true);
    btnStop.setEnabled(false);
  }

  /**
   * Process keyboard input
   *
   * @param pressed the set of keys that were pressed
   */
  private void processKeys(Set<Character> pressed)
  {
    //TODO construct the appropriate messages

    //timecode variable -> same for all keys in list
  }

  /**
   * Visually indicate note input (light up the button)
   *
   * @param pressed the set of keys that were pressed
   */
  private void highlightKeys(Set<Character> pressed) 
  {

  }

  /////////////////////////
  // KeyListener Methods //
  /////////////////////////

  private final String VALID_CHARS = "crhtsf CRHTSF";

  @Override
  public void keyPressed(KeyEvent e)
  {
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
      processKeys(pressed);

      logger.debug("keys entered: " + pressed.toString());
    }

    pressed.clear();
  }

  @Override
  public void keyTyped(KeyEvent e) { /* not used */ }

  /////////////////
  // VoiceButton //
  /////////////////

  /**
   * JButton that represents the voices on the drum kit.
   */
  private class VoiceButton extends JButton
  {
    /** The normal background color */
    private Color normalColor = Color.LIGHT_GRAY;
    /** The background color when pressed */
    private Color pressedColor = Color.BLACK;

    /**
     * Construct a VoiceButton
     * @param text the button text
     */
    public VoiceButton(String text) 
    {
      super(text);

      setMinimumSize(new Dimension(200, 100));

      setBorder(new EtchedBorder(EtchedBorder.LOWERED));
      setBorderPainted(true);
      setFocusPainted(false);

      setContentAreaFilled(false);
      setOpaque(true);

      setBackground(normalColor);
      setForeground(Color.BLACK);

      addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
          if (getModel().isPressed()) {
            setBackground(pressedColor);
            setForeground(Color.WHITE);
          } else {
            setBackground(normalColor);
            setForeground(Color.BLACK);
          }
        }
      });
    }
  }
}

