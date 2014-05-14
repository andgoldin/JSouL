import jsoul.midi.*;

// Some JSouL example code.
public class JSoulTester {

	public static void main(String[] args) throws Exception {

		// Create a Note, middle C, forte, half note
		Note n = new Note("C4", Dynamics.FORTE, Note.WHOLE);
		
		// play the note
		Player.play(n);

		// Create two chords, C major and G major
		Chord c = new Chord("C4 E4 G4 C5 E5", Dynamics.FORTE, Note.HALF);
		Chord g = new Chord("G3 D4 G4 B4 F5", Dynamics.FORTE, Note.HALF);
		
		// Create a track from the two chords
		Track t = new Track(c, g, c);
		
		// set the track's instrument
		t.setInstrument(Instrument.CHOIR_AAHS);
		
		// play the track
		Player.play(t);

		// Create a track and add the note and chord to it
		t = new Track();
		t.add(n);
		t.add(c);

		// set the instrument of the track to HONKY TONK PIANO
		t.setInstrument(Instrument.HONKY_TONK_PIANO);
		
		// play the track
		Player.play(t);
		
		// add the track to a multitrack sequence (that will only contain one track here)
		Sequence s = new Sequence();
		s.add(t);
		
		// play the sequence
		Player.play(s);
		
		// grab a midi file from within the project folder
		MidiFile m = new MidiFile("song.mid");
		
		// play the midi file, one of two ways
		Player.play(m);
		Player.play("song.mid");

	}

}