package jsoul.midi;
import java.io.*;
import javax.sound.midi.*;

// static class for playing midi files or sequences and writing midi information to midi files
/**
 * A class which provides static methods to play Notes, Chords, Tracks, Sequences,
 * and MIDI files. Not advised for dynamic sound in a program, as there will
 * be a bit of latency between calling a "play" method and hearing the sound.
 * Additionally, if a play method is called in a program, the program will pause
 * until the playing has ceased.
 * @author Andrew Goldin
 */
public class Player {

	private static Sequencer sequencer = null;

	/**
	 * Plays the given note.
	 * @param n the Note to play
	 */
	public static void play(Note n) {
		play(new Track(n));
	}

	/**
	 * Plays the given chord.
	 * @param c the Chord to play
	 */
	public static void play(Chord c) {
		play(new Track(c));
	}

	/**
	 * Plays the given Track.
	 * @param t the Track to play
	 */
	public static void play(Track t) {
		play(new Sequence(t));
	}

	/**
	 * Plays the given Sequence.
	 * @param s the Sequence to play
	 */
	public static void play(Sequence s) {
		initSequencer(s.createMidiSequence(), s.getTempo());
	}

	/**
	 * Plays the given MIDI file.
	 * @param m the MIDI file to play
	 */
	public static void play(MidiFile m) {
		play(m.getFileName());
	}

	/**
	 * Plays the given MIDI file given the file path name.
	 * @param fileName the name of the file to play
	 */
	public static void play(String fileName) {
		try {
			initSequencer(MidiSystem.getSequence(new File(fileName)), 0);
		} catch (InvalidMidiDataException e) {
			System.err.println("Error: (play) failure to retrieve midi data from " + fileName);
			//e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error: (play) failure to read file " + fileName);
			//e.printStackTrace();
		}
	}

	// starts the sequencer
	private static void initSequencer(javax.sound.midi.Sequence s, float t) {
		if (sequencer != null) {
			// if the sequencer is already running, wait for it to stop
			while (sequencer.isRunning()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.err.println("Error: (play) failure waiting to play next sequence");
					//e.printStackTrace();
				}
			}
		}
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
		} catch (MidiUnavailableException e) {
			System.err.println("Error: (play) cannot get MIDI Sequencer");
		}
		try {
			sequencer.setSequence(s);
		} catch (InvalidMidiDataException e) {
			System.err.println("Error: (play) cannot set sequence for MIDI sequencer");
		}
		if (t > 0) {
			sequencer.setTempoInBPM(t); // set tempo if specified
		}
		sequencer.addMetaEventListener(new MetaEventListener() {
			public void meta(MetaMessage m) {
				if (m.getType() == 47) {
					sequencer.stop();
					sequencer.close();
				}
			}
		});
		sequencer.start();
	}

}