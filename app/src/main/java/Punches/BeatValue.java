package Punches;
/**
 * Possible beat values
 *
 * @author Vince Aquilina
 * @version 03/17/22
 */
public enum BeatValue 
{
  WHOLE(1),           /** Whole note */
  HALF(2),            /** Half note */
  QUARTER(4),         /** Quarter note */
  EIGHTH(8),          /** Eighth note */
  SIXTEENTH(16),      /** Sixteenth note */
  THIRTY_SECOND(32),  /** Thirty-second note */
  SIXTY_FOURTH(64);   /** Sixty-fourth note */

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

