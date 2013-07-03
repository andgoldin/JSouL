package jsoul.midi;
import java.util.*;

public class Chord implements Playable, Comparable<Chord> {
	
	private int velocity, duration;
	private int[] pitches;
	
	public Chord() {
		pitches = new int[3];
		pitches[0] = 60; pitches[1] = 64; pitches[2] = 67;
		Arrays.sort(pitches);
		velocity = 85;
		duration = Note.WHOLE;
	}
	
	public Chord(int[] p, int v, int d) {
		pitches = p;
		Arrays.sort(pitches);
		velocity = v;
		duration = d;
	}
	
	public Chord(String pitchList, int v, int d) {
		String[] p = pitchList.split(" ");
		pitches = new int[p.length];
		for (int i = 0; i < pitches.length; i++) {
			pitches[i] = Note.stringToPitch(p[i]);
		}
		Arrays.sort(pitches);
		velocity = v;
		duration = d;
	}
	
	public int getSize() {
		return pitches.length;
	}
	
	public void setPitches(int[] p) {
		pitches = p;
		Arrays.sort(pitches);
	}
	
	public int[] getPitches() {
		return pitches;
	}
	
	public int getHighestPitch() {
		return pitches[pitches.length - 1];
	}
	
	public int getLowestPitch() {
		return pitches[0];
	}
	
	public void transpose(int steps) {
		if (getLowestPitch() + steps >= 0 && getHighestPitch() + steps <= 127) {
			for (int i = 0; i < pitches.length; i++) {
				pitches[i] += steps;
			}
		}
	}
	
	public void setVelocity(int v) {
		velocity = v;
	}
	
	public int getVelocity() {
		return velocity;
	}
	
	public void setDuration(int d) {
		duration = d;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public Note[] getNotes() {
		Note[] notes = new Note[pitches.length];
		for (int i = 0; i < notes.length; i++) {
			notes[i] = new Note(pitches[i], velocity, duration);
		}
		return notes;
	}
	
	public Note getNote(int i) {
		return getNotes()[i];
	}
	
	public String toString() {
		String s = "CHORD: Pitches =";
		for (int i = 0; i < pitches.length; i++) {
			s += " " + Note.pitchToString(pitches[i]);
		}
		s += ", Velocity = " + velocity + ", Duration = " + duration;
		return s;
	}
	
	public int compareTo(Chord other) {
		if (this.getSize() > other.getSize())
			return 1;
		else if (this.getSize() == other.getSize())
			return 0;
		else
			return -1;
	}
	
}