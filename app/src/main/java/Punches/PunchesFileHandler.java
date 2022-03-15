package Punches;

import java.io.Serializable;

import java.util.List;
/**
 * Encapsulates Song and layout data to be saved/loaded to/from files.
 *
 * @author Vince Aquilina
 * @version 03/15/22
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
   *
   * @param songdata - the Song object
   * @param cellData - the collection of cells
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
   *
   * @param songData - the Song object 
   */
  public void setSongData(Song songData)
  {
    this.songData = songData;
  }

  /**
   * Set the cell bounds
   *
   * @param cellData - the collection of cell bounds
   */
  public void setCellData(List<CellBounds> cellBounds)
  {
    this.cellBounds = cellBounds;
  }

  /**
   * Set the divider locations
   *
   * @param dividerLocations - the collection of divider locations
   */
  public void setDividerLocations(List<Integer> dividerLocations)
  {
    this.dividerLocations = dividerLocations;
  }

  /**
   * Get the Song object
   *
   * @return the Song object
   */
  public Song getSongData()
  {
    return songData;
  }

  /**
   * Get the cell bounds
   *
   * @return the cell bounds
   */
  public List<CellBounds> getCellBounds()
  {
    return cellBounds;
  }

  /**
   * Get the divider locations
   *
   * @return the divider locations
   */
  public List<Integer> getDividerLocations()
  {
    return dividerLocations;
  }
}

