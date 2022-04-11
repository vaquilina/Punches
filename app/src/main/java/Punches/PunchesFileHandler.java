package Punches;

import java.io.Serializable;

import java.util.List;
/**
 * Punches file handler.
 *
 * @author Vince Aquilina
 * @version 04/11/22
 */
public class PunchesFileHandler implements Serializable
{
  /** The Song to be saved/loaded */
  private Song songData;
  /** The bounds of the part cells */
  List<CellBounds> cellBounds;
  /** The divider locations of each part cell */
  List<Integer> dividerLocations;

  /**
   * Constructs a complete PunchesFileHandler
   * @param songData the Song object
   * @param cellBounds the collection of cell bounds
   * @param dividerLocations the collection of cell divider locations
   */
  public PunchesFileHandler(Song songData,
      List<CellBounds> cellBounds, List<Integer> dividerLocations)
  {
    this.songData = songData;
    this.cellBounds = cellBounds;
    this.dividerLocations = dividerLocations;
  }

  /**
   * Set the Song object
   * @param songData the Song object 
   */
  public void setSongData(Song songData)
  {
    this.songData = songData;
  }

  /**
   * Set the cell bounds
   * @param cellBounds the collection of cell bounds
   */
  public void setCellData(List<CellBounds> cellBounds)
  {
    this.cellBounds = cellBounds;
  }

  /**
   * Set the divider locations
   * @param dividerLocations the collection of divider locations
   */
  public void setDividerLocations(List<Integer> dividerLocations)
  {
    this.dividerLocations = dividerLocations;
  }

  /**
   * Get the Song object
   * @return the Song object
   */
  public Song getSongData()
  {
    return songData;
  }

  /**
   * Get the cell bounds
   * @return the cell bounds
   */
  public List<CellBounds> getCellBounds()
  {
    return cellBounds;
  }

  /**
   * Get the divider locations
   * @return the divider locations
   */
  public List<Integer> getDividerLocations()
  {
    return dividerLocations;
  }
}

