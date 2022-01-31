package Punches;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
/**
 * @author Vince Aquilina
 * @version Mon 31 Jan 2022 05:56:52 PM
 *
 * A JPanel designed to contain the parts of a Song.
 */
public class SongPanel extends JPanel
{
  public Song loadedSong;       // the currently loaded song

  /**
   * Constructs a SongPanel with the given Song
   *
   * @param song - the song to be loaded
   */
  public SongPanel(Song song)
  {
    loadedSong = song;
    setLayout(new MigLayout("Insets 10"));
  }

  /**
   * Set the song to assign to this panel
   *
   * @param song - the song to be assigned
   */
  public void setSong(Song song)
  {
    loadedSong = song;
  }

  /**
   * Get the song assigned to this panel
   *
   * @param - the song assigned to this panel
   */
  public Song getSong()
  {
    return loadedSong;
  }
}
