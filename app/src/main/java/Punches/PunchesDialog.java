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
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
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
 * |                                          |
 * |            [TO TAB] [TO SHEET] [CANCEL]  |
 * |                                          |
 * +------------------------------------------+
 * </pre>
 * <hr />
 *
 * @author Vince Aquilina
 * @version 03/21/22
 */
public class PunchesDialog extends JDialog implements KeyListener
{
  /*
   * TODO: keybindings (multi-key simulataneous input)
   * TODO: metronome (run in separate thread)
   */
  private final Logger logger = LoggerFactory.getLogger(PunchesDialog.class);

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

  /** Map of voice buttons */
  private final Map<String, VoiceButton> voices;
  /** The metronome */
  private Metronome metronome;

  ///////////////////
  // VOICE ACTIONS //
  ///////////////////

  Action disableAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      /* do nothing */
    }
  };

  Action crashHitAction;
  Action rideHitAction;
  Action hihatHitAction;
  Action racktomHitAction;
  Action floortomHitAction;
  Action snareHitAction;
  Action kickdrumHitAction;

  private class CrashHitAction extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      voices.get("crash").doClick(20);
      logger.debug("hit crash");
      kfMgr.clearFocusOwner();
    }
  }

  private class RideHitAction extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      voices.get("ride").doClick(20);
      logger.debug("hit ride");
      kfMgr.clearFocusOwner();
    }
  }

  private class HihatHitAction extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      voices.get("hihat").doClick(20);
      logger.debug("hit hihat");
      kfMgr.clearFocusOwner();
    }
  }

  private class RacktomHitAction extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      voices.get("racktom").doClick(20);
      logger.debug("hit racktom");
      kfMgr.clearFocusOwner();
    }
  }

  private class FloortomHitAction extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      voices.get("floortom").doClick(20);
      logger.debug("hit floortom");
      kfMgr.clearFocusOwner();
    }
  }

  private class SnareHitAction extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      voices.get("snare").doClick(20);
      logger.debug("hit snare");
      kfMgr.clearFocusOwner();
    }
  }

  private class KickdrumHitAction extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      voices.get("kickdrum").doClick(20);
      logger.debug("hit kickdrum");
      kfMgr.clearFocusOwner();
    }
  }

  /**
   * Construct a PunchesDialog
   *
   * @param owner the Frame owner of this dialog
   * @param partOwner the Song to which the Part belongs to
   * @param relevantPart the Part to which the result will be assigned
   */
  public PunchesDialog(Frame owner, Song partOwner, Part relevantPart)
  {
    super(owner);

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

    final JButton btnPlay = new JButton("PLAY/REC");
    btnPlay.setFocusable(false);
    btnPlay.setMnemonic(KeyEvent.VK_P);
    btnPlay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO: start recording
        play();
      }
    });

    final JButton btnStop = new JButton("STOP");
    btnStop.setFocusable(false);
    btnStop.setMnemonic(KeyEvent.VK_S);
    btnStop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO: discard recording
        stop();
      }
    });

    // TODO: progress bar fills as metronome plays
    final JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);

    final JPanel pnlMeta = new JPanel(new MigLayout(
          "Insets 0, fillx",
          "[fill][fill]",
          "[fill][fill]"));
    pnlMeta.add(lblPartName, "cell 0 0");
    pnlMeta.add(lblInfo,     "cell 1 0");
    pnlMeta.add(btnPlay,     "cell 0 1");
    pnlMeta.add(btnStop,     "cell 0 1");
    pnlMeta.add(progressBar, "cell 1 1");

    /*
     * Voices Panel
     */

    voices = new LinkedHashMap<>() {{
      put("crash",    new VoiceButton("CRASH (C)",         crashHitAction));
      put("ride",     new VoiceButton("RIDE (R)",          rideHitAction));
      put("hihat",    new VoiceButton("HI-HAT (H)",        hihatHitAction));
      put("racktom",  new VoiceButton("RACK TOM (T)",      racktomHitAction));
      put("snare",    new VoiceButton("SNARE (S)",         snareHitAction));
      put("floortom", new VoiceButton("FLOOR TOM (F)",     floortomHitAction));
      put("kickdrum", new VoiceButton("KICK DRUM (SPACE)", kickdrumHitAction));
    }};

    crashHitAction = new CrashHitAction();
    rideHitAction = new RideHitAction();
    hihatHitAction = new HihatHitAction();
    racktomHitAction = new RacktomHitAction();
    snareHitAction = new SnareHitAction();
    floortomHitAction = new FloortomHitAction();
    kickdrumHitAction = new KickdrumHitAction();

    KeyStroke key;
    InputMap inputMap = ((JPanel) getContentPane()).
      getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = ((JPanel) getContentPane()).getActionMap();

    key = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
    inputMap.put(key, "crashHit");
    actionMap.put("crashHit", crashHitAction);

    key = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0);
    inputMap.put(key, "rideHit");
    actionMap.put("rideHit", rideHitAction);

    key = KeyStroke.getKeyStroke(KeyEvent.VK_H, 0);
    inputMap.put(key, "hihatHit");
    actionMap.put("hihatHit", hihatHitAction);

    key = KeyStroke.getKeyStroke(KeyEvent.VK_T, 0);
    inputMap.put(key, "racktomHit");
    actionMap.put("racktomHit", racktomHitAction);

    key = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
    inputMap.put(key, "snareHit");
    actionMap.put("snareHit", snareHitAction);

    key = KeyStroke.getKeyStroke(KeyEvent.VK_F, 0);
    inputMap.put(key, "floortomHit");
    actionMap.put("floortomHit", floortomHitAction);

    key = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
    inputMap.put(key, "kickdrumHit");
    actionMap.put("kickdrumHit", kickdrumHitAction);

    final JPanel pnlVoices = new JPanel(new MigLayout(
          "Insets 0, gap 0, wrap 2", "[fill][fill]", "fill"));
    pnlVoices.add(voices.get("crash"),    "w 100%, h 100%, grow");
    pnlVoices.add(voices.get("ride"),     "w 100%");
    pnlVoices.add(voices.get("hihat"),    "w 100%");
    pnlVoices.add(voices.get("racktom"),  "w 100%");
    pnlVoices.add(voices.get("snare"),    "w 100%");
    pnlVoices.add(voices.get("floortom"), "w 100%");
    pnlVoices.add(voices.get("kickdrum"), "span");

    //TODO figure out focus condition situation

    /*
     * Button Panel
     */

    final JButton btnToTab = new JButton("TO TAB");
    btnToTab.setFocusable(false);
    btnToTab.setMnemonic(KeyEvent.VK_T);
    btnToTab.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO tab is created, registered to part
        toTab(rhythm);
      }
    });

    final JButton btnToSheet = new JButton("TO SHEET");
    btnToSheet.setFocusable(false);
    btnToSheet.setMnemonic(KeyEvent.VK_H);
    btnToSheet.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO sheet is created, registered to part
        //toSheet(rhythm);
      }
    });

    final JButton btnCancel = new JButton("CANCEL");
    btnCancel.setFocusable(false);
    btnCancel.setMnemonic(KeyEvent.VK_C);
    btnCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
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
  }

  /**
   * Stop the metronome
   */
  private void stop()
  {
    metronome.end();
    metronome = null;
  }

  /**
   * Get a sheet music representation of a Rhythm
   */
  //private Image toSheet(Rhythm rhythm)
  //{
  //}

  /////////////////////////
  // KeyListener Methods //
  /////////////////////////

  /** The set of keys that are currently being pressed */
  private final Set<Character> pressed = new HashSet<Character>();

  @Override
  public synchronized void keyPressed(KeyEvent e)
  {
    pressed.add(e.getKeyChar());
    if (pressed.size() > 1) {
      // multiple keys are being pressed
      // TODO iterate over set to get the keys
    }
  }

  @Override
  public synchronized void keyReleased(KeyEvent e)
  {
    pressed.remove(e.getKeyChar());
  }

  @Override
  public void keyTyped(KeyEvent e) {}

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
    public VoiceButton(String text, Action action) 
    {
      super(action);

      setText(text);

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

