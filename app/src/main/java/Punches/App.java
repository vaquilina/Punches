package Punches;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

/**
 * @author Vince Aquilina
 * @version Sun 30 Jan 2022 12:13:27 AM
 *
 * Punches launch app.
 */

/**
 * App launch point
 */
public class App 
{
  public static void main(String[] args)
  {
    PunchesFrame app = new PunchesFrame("PUNCHES");

    // start maximized; set initial size for windowed mode
    app.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    app.setSize(2000, 1000);

    app.setLayout(new MigLayout());
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    app.setVisible(true);
  }
}
