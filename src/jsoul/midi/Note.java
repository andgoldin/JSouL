package jsoul.midi;
public class Note implements Playable, Comparable<Note> {

	public static final int WHOLE = 64,
			HALF = 32,
			QUARTER = 16,
			EIGHTH = 8,
			SIXTEENTH = 4,
			THIRTYSECOND = 2,
			SIXTYFOURTH = 1;

	public static final String[] KEYNAMES =
		{ "C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B" };

	private int pitch, velocity, duration;

	// duration is the number of 64th notes
	public Note(int p, int v, int d) {
		setNote(p, v, d);
	}

	public Note(String p, int v, int d) {
		setNote(stringToPitch(p), v, d);
	}

	public Note() {
		setNote(60, 85, WHOLE);
	}
	
	public void setNote(int p, int v, int d) {
		pitch = p;
		velocity = v;
		duration = d;
	}
	
	public void setNote(String p, int v, int d) {
		pitch = stringToPitch(p);
		velocity = v;
		duration = d;
	}

	public void setPitch(int p) {
		pitch = p;
	}
	
	public void setPitch(String p) {
		pitch = stringToPitch(p);
	}
	
	public void transpose(int steps) {
		if (pitch + steps >= 0 && pitch + steps <= 127) {
			pitch += steps;
		}
	}

	public int getPitch() {
		return pitch;
	}

	public String getPitchString() {
		return pitchToString(pitch);
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

	public static String pitchToString(int pitch) {
		return KEYNAMES[pitch % 12] + ((pitch / 12) - 1);
	}

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

	public String toString() {
		return "NOTE: Pitch = " + pitchToString(pitch) + ", Velocity = " + velocity + ", Duration = " + duration;
	}

	public int compareTo(Note other) {
		if (this.getPitch() > other.getPitch())
			return 1;
		else if (this.getPitch() == other.getPitch())
			return 0;
		else
			return -1;
	}

}