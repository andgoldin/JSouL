package jsoul.midi;
import java.io.*;
import java.util.ArrayList;
import javax.sound.midi.*;

/**
 * Represents a MIDI file format, and provides an interface to read from
 * and write to .mid files.
 * @author Andrew Goldin
 */
public class MidiFile {

	private String name;

	/**
	 * Default constructor: Defines a MIDI file named "output.mid".
	 */
	public MidiFile() {
		name = "output.mid";
	}

	/**
	 * Constructs a new MIDI file object with the given filename.
	 * @param fileName the desired filename
	 */
	public MidiFile(String fileName) {
		name = fileName;
		if (!name.endsWith(".mid")) name += ".mid";
	}

	/**
	 * Set the desired file name of the MIDI file.
	 * @param fileName the desired file name
	 */
	public void setFileName(String fileName) {
		name = fileName;
		if (!name.endsWith(".mid")) name += ".mid";
	}

	/**
	 * Get the object's file name.
	 * @return the file name
	 */
	public String getFileName() {
		return name;
	}

	/**
	 * Writes a single note to a MIDI file, using the file name specified in
	 * the MidiFile's constructor.
	 * @param n the Note to be written
	 */
	public void writeToFile(Note n) {
		writeToFile(new Sequence(new Track(n)));
	}

	/**
	 * Writes a single chord to a MIDI file, using the file name specified in
	 * the MidiFile's constructor.
	 * @param c the Chord to be written
	 */
	public void writeToFile(Chord c) {
		writeToFile(new Sequence(new Track(c)));
	}
	
	/**
	 * Writes a single jsoul track to a MIDI file, using the file name specified in
	 * the MidiFile's constructor.
	 * @param t the Track to be written
	 */
	public void writeToFile(Track t) {
		writeToFile(new Sequence(t));
	}

	/**
	 * Writes a jsoul Sequence to a MIDI file, using the file name specified in
	 * the MidiFile's constructor.
	 * @param s the Sequence to be written
	 */
	public void writeToFile(Sequence s) {
		javax.sound.midi.Sequence seq = s.createMidiSequence();
		try {
			MidiSystem.write(seq, MidiSystem.getMidiFileTypes(seq)[0], new File(name));
		} catch (IOException e) {
			System.err.println("Error: (Midi) failure to write sequence to file " + name);
			//e.printStackTrace();
		}
	}
	
	/**
	 * Clears the MIDI file of all content, if the file exists in the system.
	 */
	public void clear() {
		Track[] tracks = null;
		try {
			tracks = new Track[MidiSystem.getSequence(new File(name)).getTracks().length];
		} catch (InvalidMidiDataException e) {
			System.err.println("Error: (Midi) cannot clear MIDI file " + name);
			//e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error: (Midi) cannot clear MIDI file " + name);
			//e.printStackTrace();
		}
		for (int i = 0; i < tracks.length; i++) tracks[i] = new Track();
		Sequence s = new Sequence(tracks);
		writeToFile(s);
	}

	// this method will only work on files created with jsoul
	/**
	 * Will convert the file (if it exists in the system) into a jsoul-compatible
	 * Sequence that can be modified using jsoul's API. Currently is only guaranteed
	 * to work on files that were originally created with jsoul.
	 * @return the jsoul Sequence generated from the file
	 */
	public Sequence getSequence() {
		javax.sound.midi.Sequence inputSequence = null;
		try {
			inputSequence = MidiSystem.getSequence(new File(name));
		} catch (InvalidMidiDataException e) {
			System.err.println("Error: (Midi) error retrieving java sequence from file " + name);
			//e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error: (Midi) error retrieving java sequence from file " + name);
			//e.printStackTrace();
		}
		javax.sound.midi.Track[] inputTracks = inputSequence.getTracks();
		Track[] outputTracks = new Track[inputTracks.length];
		float tempo = 120;
		for (int i = 0; i < inputTracks.length; i++) {
			System.out.println(inputTracks[i].size());
			javax.sound.midi.Track currentTrack = inputTracks[i];
			outputTracks[i] = new Track();
			int velocity = 0, chordSize = 0;
			ArrayList<Integer> pitchList = new ArrayList<Integer>();
			boolean chord = false;
			long currentTick = 0;
			for (int j = 0; j < currentTrack.size(); j++) {
				MidiEvent event = currentTrack.get(j);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage) {
					ShortMessage sMessage = (ShortMessage) message;
					if (sMessage.getCommand() == ShortMessage.PROGRAM_CHANGE) {
						outputTracks[i].setInstrument(sMessage.getData1());
					}
					else if (sMessage.getCommand() == ShortMessage.NOTE_ON) {
						chord = true;
						velocity = sMessage.getData2();
						currentTick = event.getTick();
						chordSize++;
					}
					else if (sMessage.getCommand() == ShortMessage.NOTE_OFF) {
						pitchList.add(sMessage.getData1());
						chordSize--;
						if (chordSize == 0 && chord) {
							if (pitchList.size() == 1) {
								outputTracks[i].add(new Note(pitchList.get(0),
										velocity, (int) (event.getTick() - currentTick)));
							}
							else {
								int[] list = new int[pitchList.size()];
								for (int k = 0; k < list.length; k++)
									list[k] = pitchList.get(k).intValue();
								outputTracks[i].add(new Chord(list, velocity,
										(int) (event.getTick() - currentTick)));
							}
							chord = false;
							pitchList.clear();
						}
					}
				}
				else if (message instanceof MetaMessage) {
					MetaMessage mMessage = (MetaMessage) message;
					if (mMessage.getType() == 81) { // check for set tempo message
						String hexString = new java.math.BigInteger(mMessage.getData()).toString(16);
						tempo = 60000000 / Integer.parseInt(hexString, 16);
					}
				}
			}
		}
		Sequence s = new Sequence();
		s.setTracks(outputTracks);
		s.setTempoInBPM(tempo);
		return s;
	}
	
	/**
	 * Appends a given Sequence to the end of an existing MIDI file, and writing the result
	 * to a new file. Will only work if the original file and provided sequence have the
	 * same number of tracks.
	 * @param s The sequence to append.
	 */
	public void append(Sequence s) {
		Track[] sourceTracks = this.getSequence().getTracks();
		Track[] inputTracks = s.getTracks();
		if (sourceTracks.length == inputTracks.length) {
			for (int i = 0; i < inputTracks.length; i++) {
				sourceTracks[i].add(inputTracks[i]);
			}
		}
		else {
			System.err.println("Error: (Midi) could not append sequence: mismatching track numbers");
			return;
		}
		String fname = name.substring(0, name.length() - 4) + "_appended.mid";
		javax.sound.midi.Sequence seq = s.createMidiSequence();
		try {
			MidiSystem.write(seq, MidiSystem.getMidiFileTypes(seq)[0], new File(fname));
		} catch (IOException e) {
			System.err.println("Error: (Midi) failure to write appended sequence to file");
			//e.printStackTrace();
		}
	}

}