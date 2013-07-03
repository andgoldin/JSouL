package jsoul.midi;
import java.io.*;
import java.util.ArrayList;
import javax.sound.midi.*;

public class Midi {

	private String name;

	public Midi() {
		name = "output.mid";
	}

	public Midi(String fileName) {
		name = fileName;
		if (!name.endsWith(".mid")) name += ".mid";
	}

	public void setFileName(String fileName) {
		name = fileName;
		if (!name.endsWith(".mid")) name += ".mid";
	}

	public String getFileName() {
		return name;
	}

	// WRITE NOTE TO FILE
	public void writeToFile(Note n) {
		writeToFile(new Sequence(new Track(n)));
	}

	// WRITE CHORD TO FILE
	public void writeToFile(Chord c) {
		writeToFile(new Sequence(new Track(c)));
	}
	
	// WRITE TRACK TO FILE
	public void writeToFile(Track t) {
		writeToFile(new Sequence(t));
	}

	// WRITE SEQUENCE TO FILE
	public void writeToFile(Sequence s) {
		javax.sound.midi.Sequence seq = s.createMidiSequence();
		try {
			MidiSystem.write(seq, MidiSystem.getMidiFileTypes(seq)[0], new File(name));
		} catch (IOException e) {
			System.err.println("Error: (Midi) failure to write sequence to file " + name);
			//e.printStackTrace();
		}
	}
	
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
								outputTracks[i].add(new Note(pitchList.get(0), velocity, (int) (event.getTick() - currentTick)));
							}
							else {
								int[] list = new int[pitchList.size()];
								for (int k = 0; k < list.length; k++) list[k] = pitchList.get(k).intValue();
								outputTracks[i].add(new Chord(list, velocity, (int) (event.getTick() - currentTick)));
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