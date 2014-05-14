package jsoul.midi;
import javax.sound.midi.*;
import java.util.*;

/**
 * Represents a MIDI sequence, comprised of several layered Tracks, each
 * containing some subsequence of notes and chords.
 * @author Andrew Goldin
 */
public class Sequence implements Playable {

	private ArrayList<Track> tracks;
	private float tempo;

	/**
	 * Default constructor. Creates a new Sequence at 120 BPM.
	 */
	public Sequence() {
		tracks = new ArrayList<Track>();
		tempo = 120;
	}

	/**
	 * Constructs a new sequence by layering the provided Tracks, at 120 BPM.
	 * @param t the provided list of tracks
	 */
	public Sequence(Track... t) {
		setTracks(t);
		tempo = 120;
	}

	/**
	 * Adds a new Track to the Sequence.
	 * @param t the Track to add
	 */
	public void add(Track t) {
		tracks.add(t);
	}
	
	/**
	 * Sets a particular track in the Sequence.
	 * @param trackNum the index of the track to replace
	 * @param t the new track
	 */
	public void setTrack(int trackNum, Track t) {
		tracks.set(trackNum, t);
	}
	
	/**
	 * Reset all tracks in the sequence.
	 * @param t the new set of Tracks
	 */
	public void setTracks(Track... t) {
		tracks = new ArrayList<Track>();
		for (int i = 0; i < t.length; i++) {
			add(t[i]);
		}
	}
	
	/**
	 * Removes a specified track.
	 * @param trackNum the index of the track to remove
	 */
	public void removeTrack(int trackNum) {
		tracks.remove(trackNum);
	}
	
	/**
	 * Clears and deletes all tracks in the sequence, and resets the sequence to 120 BPM.
	 */
	public void clear() {
		tracks.clear();
		tempo = 120;
	}

	/**
	 * Returns the number of tracks in the sequence.
	 * @return the number of tracks
	 */
	public int getNumTracks() {
		return tracks.size();
	}
	
	/**
	 * Gets the tracks in the sequence.
	 * @return the array of Track objects
	 */
	public Track[] getTracks() {
		return tracks.toArray(new Track[tracks.size()]);
	}

	/**
	 * Gets a specifed track in the sequence.
	 * @param n the index of the desired track
	 * @return the desired track
	 */
	public Track getTrack(int n) {
		return tracks.get(n);
	}

	/**
	 * Sets the tempo of the sequence, in beats per minute.
	 * @param tempoBPM the tempo in beats per minute
	 */
	public void setTempoInBPM(float tempoBPM) {
		tempo = tempoBPM;
	}

	/**
	 * Returns the sequence tempo, in beats per minute
	 * @return the sequence tempo
	 */
	public float getTempo() {
		return tempo;
	}

	/**
	 * Transposes all notes and Chords in all tracks of the sequence.
	 * @param steps the number of steps (positive or negative) to transpose
	 */
	public void transpose(int steps) {
		for (int i = 0; i < tracks.size(); i++) {
			tracks.get(i).transpose(steps);
		}
	}

	/**
	 * Converts the jsoul sequence into a standard javax.sound.midi.Sequence.
	 * @return the converted Sequence
	 */
	public javax.sound.midi.Sequence createMidiSequence() {
		javax.sound.midi.Sequence s = null;
		try {
			s = new javax.sound.midi.Sequence(javax.sound.midi.Sequence.PPQ, 16);
			int channelNum = 0;
			for (Track t : tracks) {
				int tickCount = 0;
				javax.sound.midi.Track javaTrack = s.createTrack();
				javaTrack.add(new MidiEvent(sequenceTempo(), tickCount)); // set tempo
				ShortMessage setInst = new ShortMessage();
				setInst.setMessage(ShortMessage.PROGRAM_CHANGE, channelNum,
						t.getInstrument().getInstrumentNumber(), 0);
				javaTrack.add(new MidiEvent(setInst, tickCount)); // set instrument for track
				for (int i = 0; i < t.getNumElements(); i++) {
					if (t.getElement(i) instanceof Note) {
						Note currentNote = (Note) t.getElement(i);
						ShortMessage onMessage = new ShortMessage();
						onMessage.setMessage(ShortMessage.NOTE_ON, channelNum,
								currentNote.getPitch(), currentNote.getVelocity());
						javaTrack.add(new MidiEvent(onMessage, tickCount));
						tickCount += currentNote.getDuration();
						ShortMessage offMessage = new ShortMessage();
						offMessage.setMessage(ShortMessage.NOTE_OFF, channelNum,
								currentNote.getPitch(), 0);
						javaTrack.add(new MidiEvent(offMessage, tickCount));
					}
					else if (t.getElement(i) instanceof Chord) {
						Chord currentChord = (Chord) t.getElement(i);
						Note[] noteList = currentChord.getNotes();
						for (int j = 0; j < noteList.length; j++) {
							ShortMessage onMessage = new ShortMessage();
							onMessage.setMessage(ShortMessage.NOTE_ON, channelNum,
									noteList[j].getPitch(), noteList[j].getVelocity());
							javaTrack.add(new MidiEvent(onMessage, tickCount));
						}
						tickCount += currentChord.getDuration();
						for (int j = 0; j < noteList.length; j++) {
							ShortMessage offMessage = new ShortMessage();
							offMessage.setMessage(ShortMessage.NOTE_OFF, channelNum,
									noteList[j].getPitch(), 0);
							javaTrack.add(new MidiEvent(offMessage, tickCount));
						}
					}
				}
				channelNum++;
			}
		} catch (InvalidMidiDataException e) {
			System.err.println("Error: (Sequence) failure to generate Java midi sequence");
			//e.printStackTrace();
		}
		return s;
	}

	private MetaMessage sequenceTempo() {
		// convert tempo to a byte array to be recognized by Midi system
		String hexString = Integer.toHexString(60000000 / (int) tempo);
		if (hexString.length() % 2 != 0) {
			hexString = "0" + hexString;
		}
		byte[] data = new java.math.BigInteger(hexString, 16).toByteArray();
		
		// create and return message with tempo change
		MetaMessage m = new MetaMessage();
		try {
			// tempo change MetaMessage has command 81
			m.setMessage(81, data, data.length);
		} catch (InvalidMidiDataException e) {
			System.err.println("Error: (Sequence) cannot set tempo for file");
		}
		return m;
	}

	/**
	 * Returns a String representation of the sequence, including the number of tracks,
	 * tempo, and the String representation of each Track.
	 * @return the string representation of the sequence
	 */
	public String toString() {
		String s = "SEQUENCE: Tracks = " + tracks.size() + ", Tempo = " + tempo + " bpm\n";
		for (int i = 0; i < tracks.size(); i++) {
			s += tracks.get(i).toString() + "\n";
		}
		s += "END SEQUENCE";
		return s;
	}

}