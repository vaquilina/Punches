package Punches;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Launches Punches app.
 *
 * @author Vince Aquilina
 * @version 03/11/22
 *
 * TODO: implement SLF4J simple logger
 */
public class App 
{
  /**
   * The main method
   *
   * @param args command line arguments
   */
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

