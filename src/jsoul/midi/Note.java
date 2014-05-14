package jsoul.midi;

/**
 * Represents a MIDI node, with a pitch, velocity, and duration.
 * @author Andrew Goldin
 */
public class Note implements Playable, Comparable<Note> {

	/**
	 * The midi duration of a whole note, measured in ticks, where 1 tick is assumed to be one 16th of a beat.
	 */
	public static final int WHOLE = 64;
	/**
	 * The midi duration of a half note, measured in ticks, where 1 tick is assumed to be one 16th of a beat.
	 */
	public static final int HALF = 32;
	/**
	 * The midi duration of a quarter note, measured in ticks, where 1 tick is assumed to be one 16th of a beat.
	 */
	public static final int QUARTER = 16;
	/**
	 * The midi duration of an eighth note, measured in ticks, where 1 tick is assumed to be one 16th of a beat.
	 */
	public static final int EIGHTH = 8;
	/**
	 * The midi duration of a sixteenth note, measured in ticks, where 1 tick is assumed to be one 16th of a beat.
	 */
	public static final int SIXTEENTH = 4;
	/**
	 * The midi duration of a 32nd note, measured in ticks, where 1 tick is assumed to be one 16th of a beat.
	 */
	public static final int THIRTYSECOND = 2;
	/**
	 * The midi duration of a 64th note, measured in ticks, where 1 tick is assumed to be one 16th of a beat.
	 */
	public static final int SIXTYFOURTH = 1;

	/**
	 * The list of 12 key names contained within an octave, from C up to B.
	 */
	public static final String[] KEYNAMES =
		{ "C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B" };

	private int pitch, velocity, duration;

	/**
	 * Constructs a new note given a pitch value (0-127), velocity value (0-127),
	 * and a duration value in ticks.
	 * @param p the pitch
	 * @param v the velocity
	 * @param d the duration
	 */
	public Note(int p, int v, int d) {
		setNote(p, v, d);
	}

	/**
	 * Constructs a new note given a key name, velocity value (0-127),
	 * and a duration value in ticks. Example key names include "C4" (middle C),
	 * "Eb5", "G#2", etc.
	 * @param p the key name as a String
	 * @param v the velocity
	 * @param d the duration
	 */
	public Note(String p, int v, int d) {
		setNote(stringToPitch(p), v, d);
	}

	/**
	 * Default constructor: Creates middle C, mezzoforte, whole note.
	 */
	public Note() {
		setNote(60, Dynamics.MEZZOFORTE, WHOLE);
	}
	
	/**
	 * Sets the properties of the Note, similar to the corresponding constructor.
	 * @param p the pitch value
	 * @param v the velocity value
	 * @param d the duration value
	 */
	public void setNote(int p, int v, int d) {
		pitch = p;
		velocity = v;
		duration = d;
	}
	
	/**
	 * Sets the properties of the Note, similar to the corresponding constructor.
	 * @param p the pitch value
	 * @param v the velocity value
	 * @param d the duration value
	 */
	public void setNote(String p, int v, int d) {
		pitch = stringToPitch(p);
		velocity = v;
		duration = d;
	}

	/**
	 * Sets the Note's pitch.
	 * @param p the pitch value (0-127)
	 */
	public void setPitch(int p) {
		pitch = p;
	}
	
	/**
	 * Sets the Note's pitch
	 * @param p the pitch as a String (e.g. "F#5")
	 */
	public void setPitch(String p) {
		pitch = stringToPitch(p);
	}
	
	/**
	 * Tranposes the note up or down by the specified number of half steps.
	 * If the number of steps would cause the note to leave the 0-127 range,
	 * nothing will happen.
	 * @param steps the number of steps (positive or negative) to transpose the note
	 */
	public void transpose(int steps) {
		if (pitch + steps >= 0 && pitch + steps <= 127) {
			pitch += steps;
		}
	}

	/**
	 * Gets the note's pitch value as an int
	 * @return the pitch value
	 */
	public int getPitch() {
		return pitch;
	}

	/**
	 * Gets the note's pitch as a String.
	 * @return the pitch as a string
	 */
	public String getPitchString() {
		return pitchToString(pitch);
	}

	/**
	 * Sets the velocity value of the note (0-127)
	 * @param v the velocity value
	 */
	public void setVelocity(int v) {
		velocity = v;
	}

	/**
	 * Gets the note's velocity value.
	 * @return the velocity value
	 */
	public int getVelocity() {
		return velocity;
	}

	/**
	 * Sets the duration of the note, in midi ticks, where 1 tick is a 16th of a beat.
	 * @param d the duration
	 */
	public void setDuration(int d) {
		duration = d;
	}

	/**
	 * Gets the duration of the note, in midi ticks, where 1 tick is a 16th of a beat.
	 * @return the note's duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Converts a integer pitch value to a String. For example,
	 * pitchToString(60) will return "C4".
	 * @param pitch the pitch value
	 * @return the pitch as a String
	 */
	public static String pitchToString(int pitch) {
		return KEYNAMES[pitch % 12] + ((pitch / 12) - 1);
	}

	/**
	 * Converts a String representation of a pitch to an int. For example,
	 * stringToPitch("C4") will return 60.
	 * @param s the pitch String representation
	 * @return the pitch midi value (0-127)
	 */
	public static int stringToPitch(String s) {
		int pitch = 0, base = 0, multiplier = 0;
		String note = "";
		if (s.endsWith("-1")) {
			multiplier = 0;
			note = s.substring(0, s.length() - 2);
		}
		else {
			multiplier = Character.getNumericValue(s.charAt(s.length() - 1)) + 1;
			note = s.substring(0, s.length() - 1);
		}
		if (note.equals("C") || note.equals("B#")) base = 0;
		else if (note.equals("C#") || note.equals("Db")) base = 1;
		else if (note.equals("D")) base = 2;
		else if (note.equals("D#") || note.equals("Eb")) base = 3;
		else if (note.equals("E") || note.equals("Fb")) base = 4;
		else if (note.equals("F") || note.equals("Eb")) base = 5;
		else if (note.equals("F#") || note.equals("Gb")) base = 6;
		else if (note.equals("G")) base = 7;
		else if (note.equals("G#") || note.equals("Ab")) base = 8;
		else if (note.equals("A")) base = 9;
		else if (note.equals("A#") || note.equals("Bb")) base = 10;
		else if (note.equals("B") || note.equals("Cb")) base = 11;
		pitch = base + 12 * multiplier;
		if (pitch > 127) {
			System.err.println("Error: (Note) pitch out of range");
			System.exit(0);
		}
		return pitch;
	}

	/**
	 * Returns a string representation of the Note, giving pitch, velocity, and duration.
	 * @return the note's String representation
	 */
	public String toString() {
		return "NOTE: Pitch = " + pitchToString(pitch) + ", Velocity = "
				+ velocity + ", Duration = " + duration;
	}

	/**
	 * Compares this note to another Note by pitch.
	 * @return a negative value if the pitch is lower, positive if higher, 0 if equal
	 */
	public int compareTo(Note other) {
		if (this.getPitch() > other.getPitch())
			return 1;
		else if (this.getPitch() == other.getPitch())
			return 0;
		else
			return -1;
	}

}