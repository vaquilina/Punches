package Punches;

import java.io.File;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Used to capture input from the keyboard and write it to a MIDI file for 
 * processing.
 *
 * @author Vince Aquilina
 * @version 03/26/22
 */
public class Recorder implements Receiver
{
  private final Logger logger = LoggerFactory.getLogger(Recorder.class);

  /** Flag to indicate recording status */
  private boolean recording;

  /**
   * Construct a Recorder
   */
  public Recorder()
  {

  }

  /**
   * Start the recording
   */
  public void record()
  {
    recording = true;
    // TODO
  }

  /**
   * Stop the recording
   */
  public void stop()
  {
    recording = false;
    // TODO
  }

  /**
   * Get the recording status
   *
   * @return the recording status
   */
  public boolean isRecording()
  {
    return recording;
  }

  /**
   * Write the MIDI data to a Standard MIDI file
   */
  private void writeMidiData(File file)
  {
    // TODO
  }

  //////////////////////
  // Receiver Methods //
  //////////////////////

  @Override
  public void close() {}

  @Override
  public void send(MidiMessage message, long timeStamp) {}
}
