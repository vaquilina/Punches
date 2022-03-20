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
 * @version 03/19/22
 */
public class PunchesDialog extends JDialog implements KeyListener
{
  /*
   * TODO: keybindings (multi-key simulataneous input)
   * TODO: metronome (run in separate thread)
   */
  private final Logger logger = LoggerFactory.getLogger(PunchesDialog.class);

  KeyboardFocusManager kfMdr =
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

    final JLabel lblPartName = new JLabel("[" + relevantPart.getName() + "]");

    final JLabel lblInfo =
      new JLabel(relevantPart.getLengthInBars() + " bars of " +
            partOwner.getSignature().toString() + 
            " @  " + partOwner.getBpm() + " bpm");

    final JButton btnPlay = new JButton("PLAY/REC");
    btnPlay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO: start metronome/recording
      }
    });

    final JButton btnStop = new JButton("STOP");
    btnStop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO: stop metronome, discard recording
      }
    });

    // TODO: progress bar fills as metronome plays
    final JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);

    final JButton btnToTab = new JButton("TO TAB");
    btnToTab.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO tab is created, registered to part
        toTab(rhythm);
      }
    });

    final JButton btnToSheet = new JButton("TO SHEET");
    btnToSheet.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //TODO sheet is created, registered to part
        //toSheet(rhythm);
      }
    });

    final JButton btnCancel = new JButton("CANCEL");
    btnCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    final JPanel pnlMeta = new JPanel(new MigLayout(
          "Insets 0, fillx",
          "[fill][fill]",
          "[fill][fill]"));
    pnlMeta.add(lblPartName, "cell 0 0");
    pnlMeta.add(lblInfo,     "cell 1 0");
    pnlMeta.add(btnPlay,     "cell 0 1");
    pnlMeta.add(btnStop,     "cell 0 1");
    pnlMeta.add(progressBar, "cell 1 1");

    final Map<String, VoiceButton> voices = new LinkedHashMap<>() {{
      put("crash",    new VoiceButton("CRASH (C)",         crashHitAction));
      put("ride",     new VoiceButton("RIDE (R)",          rideHitAction));
      put("hihat",    new VoiceButton("HI-HAT (H)",        hihatHitAction));
      put("racktom",  new VoiceButton("RACK TOM (T)",      racktomHitAction));
      put("snare",    new VoiceButton("SNARE (S)",         snareHitAction));
      put("floortom", new VoiceButton("FLOOR TOM (F)",     floortomHitAction));
      put("kickdrum", new VoiceButton("KICK DRUM (SPACE)", kickdrumHitAction));
    }};

    //TODO register keybindings

    final JPanel pnlVoices = new JPanel(new MigLayout(
          "Insets 0, gap 0, wrap 2", "[fill][fill]", "fill"));
    pnlVoices.add(voices.get("crash"),    "w 100%, h 100%, grow");
    pnlVoices.add(voices.get("ride"),     "w 100%");
    pnlVoices.add(voices.get("hihat"),    "w 100%");
    pnlVoices.add(voices.get("racktom"),  "w 100%");
    pnlVoices.add(voices.get("snare"),    "w 100%");
    pnlVoices.add(voices.get("floortom"), "w 100%");
    pnlVoices.add(voices.get("kickdrum"), "span");

    final JPanel pnlButtons = new JPanel(new MigLayout("Insets 0, align right"));
    pnlButtons.add(btnToTab);
    pnlButtons.add(btnToSheet);
    pnlButtons.add(btnCancel);

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

  /////////////
  // ACTIONS //
  /////////////

  Action crashHitAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      logger.debug("hit crash");
    }
  };

  Action rideHitAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      logger.debug("hit ride");
    }
  };

  Action hihatHitAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      logger.debug("hit hihat");
    }
  };

  Action racktomHitAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      logger.debug("hit racktom");
    }
  };

  Action floortomHitAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      logger.debug("hit floortom");
    }
  };

  Action snareHitAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      logger.debug("hit snare");
    }
  };

  Action kickdrumHitAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      logger.debug("hit kickdrum");
    }
  };

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
   * Add a keybind to a frame or panel
   *
   * @param contentPane the content pane
   * @param key the key to bind
   * @param action the action to perform
   */
  private void addKeyBind()
  {
  }

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

