package jsoul.midi;

/**
 * The interface for a Playable object (Note, Chord, Track, or Sequence).
 * @author Andrew Goldin
 */
public interface Playable {
	
	/**
	 * Transposes the playable object by the specified number of half steps.
	 * @param steps the number of steps (positive or negative) to transpose the Playable
	 */
	public void transpose(int steps);
	
}