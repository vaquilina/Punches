package Punches;

import javax.swing.JFrame;
/**
 * @author Vince Aquilina
 * @version Tue 01 Feb 2022 05:25:45 PM
 *
 * Punches launch app.
 *
 * TODO: swap out existing placeholder music note icons
 *      with vectors (attribute rawpixel.com)
 */

/**
 * App launch point
 */
public class App 
{
  public static void main(String[] args)
  {
    PunchesFrame app = new PunchesFrame("PUNCHES");

    //app.setSize(2000, 1000);
    //app.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

    app.setLocationRelativeTo(null);
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    app.setVisible(true);
  }
}
