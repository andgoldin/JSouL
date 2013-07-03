package jsoul.midi;
import javax.sound.midi.*;
import java.util.*;

public class Sequence implements Playable {

	private ArrayList<Track> tracks;
	private float tempo;

	public Sequence() {
		tracks = new ArrayList<Track>();
		tempo = 120;
	}

	public Sequence(Track... t) {
		setTracks(t);
		tempo = 120;
	}

	// adds a track to the sequence
	public void add(Track t) {
		tracks.add(t);
	}
	
	public void setTrack(int trackNum, Track t) {
		tracks.set(trackNum, t);
	}
	
	public void setTracks(Track... t) {
		tracks = new ArrayList<Track>();
		for (int i = 0; i < t.length; i++) {
			add(t[i]);
		}
	}
	
	public void removeTrack(int trackNum) {
		tracks.remove(trackNum);
	}
	
	public void clear() {
		tracks.clear();
		tempo = 120;
	}

	public int getNumTracks() {
		return tracks.size();
	}
	
	public Track[] getTracks() {
		return tracks.toArray(new Track[tracks.size()]);
	}

	public Track getTrack(int n) {
		return tracks.get(n);
	}

	public void setTempoInBPM(float tempoBPM) {
		tempo = tempoBPM;
	}

	public float getTempo() {
		return tempo;
	}

	public void transpose(int steps) {
		for (int i = 0; i < tracks.size(); i++) {
			tracks.get(i).transpose(steps);
		}
	}

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
				setInst.setMessage(ShortMessage.PROGRAM_CHANGE, channelNum, t.getInstrument().getInstrumentNumber(), 0);
				javaTrack.add(new MidiEvent(setInst, tickCount)); // set instrument for track
				for (int i = 0; i < t.getNumElements(); i++) {
					if (t.getElement(i) instanceof Note) {
						Note currentNote = (Note) t.getElement(i);
						ShortMessage onMessage = new ShortMessage();
						onMessage.setMessage(ShortMessage.NOTE_ON, channelNum, currentNote.getPitch(), currentNote.getVelocity());
						javaTrack.add(new MidiEvent(onMessage, tickCount));
						tickCount += currentNote.getDuration();
						ShortMessage offMessage = new ShortMessage();
						offMessage.setMessage(ShortMessage.NOTE_OFF, channelNum, currentNote.getPitch(), 0);
						javaTrack.add(new MidiEvent(offMessage, tickCount));
					}
					else if (t.getElement(i) instanceof Chord) {
						Chord currentChord = (Chord) t.getElement(i);
						Note[] noteList = currentChord.getNotes();
						for (int j = 0; j < noteList.length; j++) {
							ShortMessage onMessage = new ShortMessage();
							onMessage.setMessage(ShortMessage.NOTE_ON, channelNum, noteList[j].getPitch(), noteList[j].getVelocity());
							javaTrack.add(new MidiEvent(onMessage, tickCount));
						}
						tickCount += currentChord.getDuration();
						for (int j = 0; j < noteList.length; j++) {
							ShortMessage offMessage = new ShortMessage();
							offMessage.setMessage(ShortMessage.NOTE_OFF, channelNum, noteList[j].getPitch(), 0);
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

	public String toString() {
		String s = "SEQUENCE: Tracks = " + tracks.size() + ", Tempo = " + tempo + " bpm\n";
		for (int i = 0; i < tracks.size(); i++) {
			s += tracks.get(i).toString() + "\n";
		}
		s += "END SEQUENCE";
		return s;
	}

}