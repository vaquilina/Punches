package Punches;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import java.util.LinkedHashMap;
import java.util.Map;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The "Punches Interface" - A dialog in which the user can key in a rhythm.
 *
 * <code>
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
 * </code>
 *
 * TODO: keybindings (multi-key simulataneous input)
 *
 * @author Vince Aquilina
 * @version 03/19/22
 */
public class PunchesDialog extends JDialog
{
  private final Logger logger = LoggerFactory.getLogger(PunchesDialog.class);

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
      new JLabel(relevantPart.getLengthInBars() + " bars " +
            partOwner.getSignature().toString() + 
            " @  " + partOwner.getBpm() + " bpm");

    final JButton btnPlay = new JButton("PLAY/REC");
    final JButton btnStop = new JButton("STOP");
    final JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
    final JButton btnToTab = new JButton("TO TAB");
    final JButton btnToSheet = new JButton("TO SHEET");

    final JButton btnCancel = new JButton("CANCEL");
    btnCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    final Map<String, VoiceButton> voices = new LinkedHashMap<>() {{
      put("crash", new VoiceButton("CRASH (C)"));
      put("ride", new VoiceButton("RIDE (R)"));
      put("hihat", new VoiceButton("HI-HAT (H)"));
      put("racktom", new VoiceButton("RACK TOM (T)"));
      put("snare", new VoiceButton("SNARE (S)"));
      put("floortom", new VoiceButton("FLOOR TOM (F)"));
      put("kickdrum", new VoiceButton("KICK DRUM (SPACE)"));
    }};

    final JPanel pnlMeta = new JPanel(new MigLayout(
          "Insets 0, fillx",
          "[fill][fill]",
          "[fill][fill]"));
    pnlMeta.add(lblPartName, "cell 0 0");
    pnlMeta.add(lblInfo,     "cell 1 0");
    pnlMeta.add(btnPlay,     "cell 0 1");
    pnlMeta.add(btnStop,     "cell 0 1");
    pnlMeta.add(progressBar, "cell 1 1");

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
   * JButton that represents the voices on the drum kit
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

