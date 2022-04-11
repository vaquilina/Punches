package Punches;

import org.softsmithy.lib.swing.customizer.JCustomizerPane;
/**
 * A JCustomizerPane with extended behaviour.
 *
 * @author Vince Aquilina
 * @version 04/11/22
 */
public class SongPanel extends JCustomizerPane
{
  /*
   * TODO: write tests
   */

  /** The currently loaded Song */
  public Song loadedSong;

  /**
   * Constructs a SongPanel with the given Song
   * @param song the song to be loaded
   */
  public SongPanel(Song song)
  {
    loadedSong = song;
  }

  /**
   * Set the song to assign to this panel
   * @param song the song to be assigned
   */
  public void setSong(Song song)
  {
    loadedSong = song;
  }

  /**
   * Get the song assigned to this panel
   * @return the Song assigned to this panel
   */
  public Song getSong()
  {
    return loadedSong;
  }
}
