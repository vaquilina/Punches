package Punches;
/**
 * Possible beat values as per JFugue.
 *
 * @author Vince Aquilina
 * @version 04/11/22
 */
public enum BeatValue 
{
  /** Whole note */
  WHOLE(1.0),
  /** Half note */
  HALF(0.5),
  /** Quarter note */
  QUARTER(0.25),
  /** Eighth note */
  EIGHTH(0.125),
  /** Sixteenth note */
  SIXTEENTH(0.0625),
  /** Thirty-second note */
  THIRTY_SECOND(0.03125),
  /** Sixty-fourth note */
  SIXTY_FOURTH(0.015625),
  /** One-twenty-eighth note */
  ONE_TWENTY_EIGHTH(0.0078125);

  /** The numeric beat value */
  private double value;

  /**
   * Binds the name of each BeatValue to its actual value
   * @param value the numeric beat value
   */
  private BeatValue(double value) 
  {
    this.value = value;
  }

  /**
   * Get the actual value
   * @return the numeric beat value 
   */
  public double getValue() 
  {
    return this.value;
  }
}

