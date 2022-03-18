package Punches;

import java.io.Serializable;
/**
 * Represents a Cell's bounds.
 *
 * @author Vince Aquilina
 * @version 03/17/22
 */
public class CellBounds implements Serializable
{
  /** The x co-ordinate */
  public final int x;
  /** The y co-ordinate */
  public final int y;
  /** The width */
  public final int width;
  /** The height */
  public final int height;

  /**
   * Construct a CellBounds
   *
   * @param x the x co-ordinate
   * @param y the y co-ordinate
   * @param width the width
   * @param height the height
   */
  public CellBounds(int x, int y, int width, int height) 
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Get an array of the cell's bounds
   *
   * @return an array of the cell's bounds
   */
  public int[] getCellBounds()
  {
    return new int[] { x, y, width, height };
  }

  /**
   * Get a string representation of the cell bounds
   *
   * @return a string representation of the cell bounds
   */
  @Override
  public String toString()
  {
    return "[" + x + ", " + y + ", " + width + ", " + height + "]";
  }
}
