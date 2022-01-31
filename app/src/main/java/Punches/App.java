package Punches;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

/**
 * @author Vince Aquilina
 * @version Sun 30 Jan 2022 03:43:31 PM
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
    app.setSize(2000, 1000);
    app.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    app.setVisible(true);
  }
}
