package Punches;
/**
 * Possible beat values
 *
 * @author Vince Aquilina
 * @version 03/11/22
 */
public enum BeatValue 
{
  WHOLE(1),
  HALF(2),
  QUARTER(4),
  EIGHTH(8),
  SIXTEENTH(16),
  THIRTY_SECOND(32),
  SIXTY_FOURTH(64);

  /** The numeric beat value */
  private int value;

  /**
   * Binds the name of each BeatValue to its actual value
   */
  private BeatValue(int value) 
  {
    this.value = value;
  }

  /**
   * Get the actual value
   */
  public int getValue() 
  {
    return this.value;
  }
}

