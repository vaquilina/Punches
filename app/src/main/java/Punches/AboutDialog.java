package Punches;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
/**
 * A Dialog that displays copyright information and attributions.
 *
 * @author Vince Aquilina
 * @version 03/19/22
 *
 * TODO: add hyperlinks
 * TODO: add link to user guide
 * TODO: add donate button
 */
public class AboutDialog extends JDialog
{
  private final Color PUNCHES_RED = new Color(0x710028);
  private final String COPYRIGHT = Character.toString(169);

  /**
   * Construct an AboutDialog
   */
  public AboutDialog(Frame owner)
  {
    super(owner, "About");

    setModal(true);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setLayout(new MigLayout("Insets dialog, al center center"));

    // logo
    final JLabel lblLogo = new JLabel("PUNCHES");

    final Font logoFont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    lblLogo.setFont(logoFont);
    lblLogo.setForeground(PUNCHES_RED);

    // copyright/attributions
    final JLabel lblCopyright = new JLabel(
        "Copyright " + COPYRIGHT + "2022 Vince Aquilina");

    final JLabel lblAdobe = new JLabel(
        "Adobe PDF logo , Adobe PDF " + COPYRIGHT + " Adobe, Inc.");

    final JLabel lblLilyPond = new JLabel(
        "LilyPond " + COPYRIGHT + " lilypond.org");

    final JLabel lblSoftSmithy = new JLabel(
        "SoftSmithy Utility Library " + COPYRIGHT + " Florian Brunner");

    final JLabel lblTxtmark = new JLabel(
        "txtmark " + COPYRIGHT + " René Jeschke");

    final JLabel lblOpenhtmltopdf = new JLabel(
        "openhtmltopdf " + COPYRIGHT + "Dan Fickle");

    final JLabel lblFamfamfam = new JLabel(
        "Silk Icons " + COPYRIGHT + "famfamfam.com");

    // dismiss button
    final JButton btnDismiss = new JButton("Dismiss");
    btnDismiss.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    getRootPane().setDefaultButton(btnDismiss);

    // lay out components
    add(lblLogo, "center, wrap");
    add(lblCopyright, "center, wrap 20");
    add(lblAdobe, "center, wrap");
    add(lblLilyPond, "center, wrap");
    add(lblSoftSmithy, "center, wrap");
    add(lblTxtmark, "center, wrap");
    add(lblOpenhtmltopdf, "center, wrap");
    add(lblFamfamfam, "center, wrap 20");
    add(btnDismiss, "center, wrap");

    pack();
  }
}

