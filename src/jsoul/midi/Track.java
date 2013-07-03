package jsoul.midi;
import java.util.*;

public class Track implements Playable {

	private ArrayList<Playable> elements;
	private Instrument instrument;

	public Track() {
		elements = new ArrayList<Playable>();
		instrument = new Instrument(Instrument.PIANO);
	}

	public Track(Playable... p) {
		elements = new ArrayList<Playable>();
		for (int i = 0; i < p.length; i++) {
			add(p[i]);
		}
		instrument = new Instrument(Instrument.PIANO);
	}

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

	public void set(int index, Playable p) {
		if (p instanceof Note || p instanceof Chord) {
			elements.set(index, p);
		}
	}

	public void remove(int index) {
		elements.remove(index);
	}
	
	public void clear() {
		elements.clear();
		instrument = new Instrument(Instrument.PIANO);
	}

	public int getNumElements() {
		return elements.size();
	}

	public Playable[] getElements() {
		return elements.toArray(new Playable[elements.size()]);
	}

	public Playable getElement(int n) {
		return elements.get(n);
	}

	public void setInstrument(Instrument inst) {
		instrument = inst;
	}

	public void setInstrument(int instNum) {
		instrument = new Instrument(instNum);
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void transpose(int steps) {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).transpose(steps);
		}
	}

	public String toString() {
		String s = "TRACK: Elements = " + elements.size() + ", Instrument = " + instrument.toString() + "\n";
		for (int i = 0; i < elements.size(); i++) {
			s += "   " + elements.get(i).toString() + "\n";
		}
		s += "END TRACK";
		return s;
	}

}