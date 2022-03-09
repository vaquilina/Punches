package Punches;

import javax.swing.JFrame;

import java.awt.EventQueue;
/**
 * @author Vince Aquilina
 * @version 03/09/22
 *
 * Launches Punches app.
 *
 */

/**
 * App launch point
 */
public class App 
{
  public static void main(String[] args)
  {
    EventQueue.invokeLater(() -> {
      PunchesFrame app = new PunchesFrame("PUNCHES");
      app.setLocationRelativeTo(null);
      app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      app.setVisible(true);
    });
  }
}
