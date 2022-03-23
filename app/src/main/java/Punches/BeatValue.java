package Punches;
/**
 * Possible beat values
 *
 * @author Vince Aquilina
 * @version 03/17/22
 */
public enum BeatValue 
{
  /** Whole note */
  WHOLE(1),
  /** Half note */
  HALF(2),
  /** Quarter note */
  QUARTER(4),
  /** Eighth note */
  EIGHTH(8),
  /** Sixteenth note */
  SIXTEENTH(16),
  /** Thirty-second note */
  THIRTY_SECOND(32),
  /** Sixty-fourth note */
  SIXTY_FOURTH(64);

  /** The numeric beat value */
  private int value;

  /**
   * Binds the name of each BeatValue to its actual value
   *
   * @param value the numeric beat value
   */
  private BeatValue(int value) 
  {
    this.value = value;
  }

  /**
   * Get the actual value
   *
   * @return the numeric beat value 
   */
  public int getValue() 
  {
    return this.value;
  }
}

