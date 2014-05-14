package jsoul.midi;
import java.util.*;

/**
 * Represents a MIDI track, containing a sequence of notes and Chords, as well as an instrument.
 * @author Andrew Goldin
 */
public class Track implements Playable {

	private ArrayList<Playable> elements;
	private Instrument instrument;

	/**
	 * Default constructor: An empty piano track.
	 */
	public Track() {
		elements = new ArrayList<Playable>();
		instrument = new Instrument(Instrument.PIANO);
	}

	/**
	 * Creates a new track with the given sequence of Playables. The playables can be
	 * Notes, Chords, or other Tracks. Whole Sequences cannot be added to a track. The
	 * track will be built in the order that the Playables are provided.
	 * @param p the list of Playables
	 */
	public Track(Playable... p) {
		elements = new ArrayList<Playable>();
		for (int i = 0; i < p.length; i++) {
			add(p[i]);
		}
		instrument = new Instrument(Instrument.PIANO);
	}

	/**
	 * Adds a new Playable to the track by appending it to the end. Providing a Sequence will do nothing.
	 * @param p the playable to add (can only be a Note, Chord, or Track)
	 */
	public void add(Playable p) {
		if (p instanceof Note || p instanceof Chord) {
			elements.add(p);
		}
		else if (p instanceof Track) {
			Track s = (Track) p;
			Playable[] temp = s.getElements();
			for (int i = 0; i < temp.length; i++) {
				elements.add(temp[i]);
			}
		}
	}

	/**
	 * Set the Note or Chord at a certain location in the track. If anything other
	 * than a Note or a Chord is provided, nothing will happen.
	 * @param index the location of the Note or Chord to set.
	 * @param p the new Playable. Can only be a Note or a Chord.
	 */
	public void set(int index, Playable p) {
		if (p instanceof Note || p instanceof Chord) {
			elements.set(index, p);
		}
	}

	/**
	 * Removes the Note or Chord at the specified location in the track.
	 * @param index the location of the Note or Chord to remove.
	 */
	public void remove(int index) {
		elements.remove(index);
	}
	
	/**
	 * Clears the track, removing all elements and resets the instrument to default piano.
	 */
	public void clear() {
		elements.clear();
		instrument = new Instrument(Instrument.PIANO);
	}

	/**
	 * Gets the number of elements (notes and chords) in the track.
	 * @return the number of notes and chords.
	 */
	public int getNumElements() {
		return elements.size();
	}

	/**
	 * Returns the full list of Playables (Notes and Chords) in the track.
	 * @return the list of Playables.
	 */
	public Playable[] getElements() {
		return elements.toArray(new Playable[elements.size()]);
	}

	/**
	 * Gets the Playable (note or chord) at the given position in the track.
	 * @param n the position of the Playable
	 * @return the Playable at position n
	 */
	public Playable getElement(int n) {
		return elements.get(n);
	}

	/**
	 * Sets the track's instrument using an Instrument object.
	 * @param inst the provided Instrument
	 */
	public void setInstrument(Instrument inst) {
		instrument = inst;
	}

	/**
	 * Sets the track's instrument using the instrument's MIDI value (0-127).
	 * Use the static final variables of the Instrument class as convenient
	 * parameters to this method.
	 * @param instNum the desired instrument number
	 */
	public void setInstrument(int instNum) {
		instrument = new Instrument(instNum);
	}

	/**
	 * Gets this track's instrument.
	 * @return the track's instrument
	 */
	public Instrument getInstrument() {
		return instrument;
	}

	/**
	 * Transposes all notes and chords in the track.
	 * @param steps the number of steps (positive or negative) to tranpose the track
	 */
	public void transpose(int steps) {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).transpose(steps);
		}
	}

	/**
	 * Returns a String representation of the track, including the number of elements,
	 * instrument, and the String representation of each element.
	 * @return the string representation of the track
	 */
	public String toString() {
		String s = "TRACK: Elements = " + elements.size() + ", Instrument = " + instrument.toString() + "\n";
		for (int i = 0; i < elements.size(); i++) {
			s += "   " + elements.get(i).toString() + "\n";
		}
		s += "END TRACK";
		return s;
	}

}