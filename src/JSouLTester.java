import jsoul.midi.*;

// Some JSouL example code.
public class JSouLTester {
	
	public static void main(String[] args) throws Exception {
		
		// create a new Track object
		Track t = new Track();
		
		// add a sequence of 150 random notes between F1 and F6 to the Track
		for (int i = 0; i < 150; i++) {
			t.add(new Note((int) (Math.random() * 30 + 60), Dynamics.FORTE, Note.SIXTYFOURTH));
		}
		
		// set instrument to honky tonk piano
		t.setInstrument(Instrument.HONKY_TONK_PIANO);
		
		// create a new Sequence object holding the sole Track
		Sequence s = new Sequence(t);
		
		// set the sequence tempo
		s.setTempoInBPM(150);
		
		// play the sequence
		Player.play(s);
		
	}
	
}
