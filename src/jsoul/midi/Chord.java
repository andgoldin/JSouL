package jsoul.midi;
import java.util.*;

/**
 * Represents a MIDI chord, containing multiple stacked notes.
 * @author Andrew Goldin
 */
public class Chord implements Playable, Comparable<Chord> {
	
	private int velocity, duration;
	private int[] pitches;
	
	/**
	 * Default constructor: Generates a middle C major triad, mezzoforte, whole note.
	 */
	public Chord() {
		pitches = new int[3];
		pitches[0] = 60; pitches[1] = 64; pitches[2] = 67;
		Arrays.sort(pitches);
		velocity = Dynamics.MEZZOFORTE;
		duration = Note.WHOLE;
	}
	
	/**
	 * Generates a new chord given a list of pitch values, a velocity, and duration
	 * @param p the list of pitch values (0 to 127)
	 * @param v the velocity (0 to 127)
	 * @param d the duration of the chord in midi ticks, where 1 tick is assumed to be one 16th of a beat
	 */
	public Chord(int[] p, int v, int d) {
		pitches = p;
		Arrays.sort(pitches);
		velocity = v;
		duration = d;
	}
	
	/**
	 * Generates a new chord given a list of pitch strings separated by spaces.
	 * @param pitchList the list of pitches
	 * @param v the velocity (0 to 127)
	 * @param d the duration of the chord in midi ticks, where 1 tick is assumed to be one 16th of a beat
	 */
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
	
	/**
	 * Returns the number of notes in the chord.
	 * @return the number of notes in the chord
	 */
	public int getSize() {
		return pitches.length;
	}
	
	/**
	 * Set the pitches to a new set of values.
	 * @param p the new pitches
	 */
	public void setPitches(int[] p) {
		pitches = p;
		Arrays.sort(pitches);
	}
	
	/**
	 * Returns all pitches in the chord, sorted from lowest to highest pitch
	 * @return the pitch values of the chord
	 */
	public int[] getPitches() {
		return pitches;
	}
	
	/**
	 * Returns the highest pitch in the chord.
	 * @return the highest pitch value
	 */
	public int getHighestPitch() {
		return pitches[pitches.length - 1];
	}
	
	/**
	 * Returns the lowest pitch in the chord.
	 * @return the lowest pitch value
	 */
	public int getLowestPitch() {
		return pitches[0];
	}
	
	/**
	 * Transposes all pitches by the specified number of half steps.
	 * If a pitch cannot be transposed any higher or lower, it will
	 * not be affected.
	 */
	public void transpose(int steps) {
		if (getLowestPitch() + steps >= 0 && getHighestPitch() + steps <= 127) {
			for (int i = 0; i < pitches.length; i++) {
				pitches[i] += steps;
			}
		}
	}
	
	/**
	 * Set the velocity (volume) of the chord.
	 * @param v the new velocity value (0 to 127)
	 */
	public void setVelocity(int v) {
		velocity = v;
	}
	
	/**
	 * Returns the velocity of the chord.
	 * @return the velocity value
	 */
	public int getVelocity() {
		return velocity;
	}
	
	/**
	 * Sets the duration of the chord.
	 * @param d the duration value in midi ticks, where 1 tick is assumed to be one 16th of a beat
	 */
	public void setDuration(int d) {
		duration = d;
	}
	
	/**
	 * Returns the duration of the chord.
	 * @return the duration value in ticks
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Returns the notes in the chord as an array of note objects, sorted
	 * in ascending order from lowest to highest pitch.
	 * @return the list of Note objects
	 */
	public Note[] getNotes() {
		Note[] notes = new Note[pitches.length];
		for (int i = 0; i < notes.length; i++) {
			notes[i] = new Note(pitches[i], velocity, duration);
		}
		return notes;
	}
	
	/**
	 * Returns a note at a specific position in the chord.
	 * @param i the index of the note (0 is lowest note)
	 * @return the Note object at the specified position
	 */
	public Note getNote(int i) {
		return getNotes()[i];
	}
	
	/**
	 * Returns a string representation of the chord, giving each note with pitch,
	 * velocity, and duration.
	 * @return the chord's String representation
	 */
	public String toString() {
		String s = "CHORD: Pitches =";
		for (int i = 0; i < pitches.length; i++) {
			s += " " + Note.pitchToString(pitches[i]);
		}
		s += ", Velocity = " + velocity + ", Duration = " + duration;
		return s;
	}
	
	/**
	 * Compares this chord to another chord by size (i.e. number of notes)
	 * @return a negative value if the size is smaller, positive if greater, 0 if equal
	 */
	public int compareTo(Chord other) {
		if (this.getSize() > other.getSize())
			return 1;
		else if (this.getSize() == other.getSize())
			return 0;
		else
			return -1;
	}
	
}