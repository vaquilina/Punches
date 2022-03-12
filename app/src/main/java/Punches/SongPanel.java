package Punches;

import org.softsmithy.lib.swing.customizer.JCustomizerPane;
/**
 * An extended JPanel designed to contain the parts of a Song.
 *
 * @author Vince Aquilina
 * @version 03/11/22
 *
 * TODO: write tests
 */
public class SongPanel extends JCustomizerPane
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
