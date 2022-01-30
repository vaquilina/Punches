package Punches;

import javax.swing.JPanel;

/**
 * @author Vince Aquilina
 * @version Wed 19 Jan 2022 08:00:08 PM
 *
 * A JPanel designed to contain the parts of a Song.
 */
public class SongPanel extends JPanel
{
  public Song loadedSong;

  public SongPanel(Song song)
  {
    loadedSong = song;
  }

  public void setSong(Song song)
  {
    loadedSong = song;
  }

  public Song getSong()
  {
    return loadedSong;
  }
}
