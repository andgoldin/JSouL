package jsoul.midi;
import java.io.*;
import javax.sound.midi.*;

// static class for playing midi files or sequences and writing midi information to midi files
public class Player {

	private static Sequencer sequencer = null;

	/* PLAYING MIDI */

	public static void play(Note n) {
		play(new Track(n));
	}

	public static void play(Chord c) {
		play(new Track(c));
	}

	public static void play(Track t) {
		play(new Sequence(t));
	}

	public static void play(Sequence s) {
		initSequencer(s.createMidiSequence(), s.getTempo());
	}

	public static void play(Midi m) {
		play(m.getFileName());
	}

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
			//e.printStackTrace();
		}
		try {
			sequencer.setSequence(s);
		} catch (InvalidMidiDataException e) {
			System.err.println("Error: (play) cannot set sequence for MIDI sequencer");
			//e.printStackTrace();
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