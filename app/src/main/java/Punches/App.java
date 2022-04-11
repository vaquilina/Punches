package Punches;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Launches Punches app.
 *
 * @author Vince Aquilina
 * @version 04/11/22
 */
public class App 
{
  private static final Logger logger = LoggerFactory.getLogger(App.class);

  /**
   * The app's launchpoint
   * @param args command line arguments
   */
  public static void main(String[] args)
  {
    EventQueue.invokeLater(() -> {
      PunchesFrame app = new PunchesFrame("PUNCHES");
      app.setLocationRelativeTo(null);
      app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      app.setVisible(true);

      logger.debug("window initialized");
    });
  }
}

