import jsoul.midi.*;

public class JSouLTester {
	public static void main(String[] args) throws Exception {
		Track t = new Track();
		for (int i = 0; i < 1500; i++) {
			t.add(new Note((int) (Math.random() * 30 + 60),Dynamics.FORTE, Note.SIXTYFOURTH));
		}
		t.setInstrument(Instrument.HONKY_TONK_PIANO);
		Sequence s = new Sequence(t);
		s.setTempoInBPM(700);
		Player.play(s);
	}
}