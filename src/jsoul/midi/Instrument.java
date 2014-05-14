package jsoul.midi;
import javax.sound.midi.*;

/**
 * Represents a MIDI instrument. Contains static fields for 128 different
 * MIDI instruments. Useful for building Tracks.
 * @author Andrew Goldin
 */
public class Instrument {

	public static final int PIANO = 0,
			BRIGHT_PIANO = 1,
			ELECTRIC_GRAND = 2,
			HONKY_TONK_PIANO = 3,
			ELECTRIC_PIANO_1 = 4,
			ELECTRIC_PIANO_2 = 5,
			HARPSICHORD = 6,
			CLAVINET = 7,
			CELESTA = 8,
			GLOCKENSPIEL = 9,
			MUSIC_BOX = 10,
			VIBRAPHONE = 11,
			MARIMBA = 12,
			XYLOPHONE = 13,
			TUBULAR_BELL = 14,
			DULCIMER = 15,
			HAMMOND_ORGAN = 16,
			PERC_ORGAN = 17,
			ROCK_ORGAN = 18,
			CHURCH_ORGAN = 19,
			REED_ORGAN = 20,
			ACCORDION = 21,
			HARMONICA = 22,
			TANGO_ACCORDION = 23,
			NYLON_STR_GUITAR = 24,
			STEEL_STR_GUITAR = 25,
			JAZZ_ELECTRIC_GTR = 26,
			CLEAN_GUITAR = 27,
			MUTED_GUITAR = 28,
			OVERDRIVE_GUITAR = 29,
			DISTORTION_GUITAR = 30,
			GUITAR_HARMONICS = 31,
			ACOUSTIC_BASS = 32,
			FINGERED_BASS = 33,
			PICKED_BASS = 34,
			FRETLESS_BASS = 35,
			SLAP_BASS_1 = 36,
			SLAP_BASS_2 = 37,
			SYN_BASS_1 = 38,
			SYN_BASS_2 = 39,
			VIOLIN = 40,
			VIOLA = 41,
			CELLO = 42,
			CONTRABASS = 43,
			TREMOLO_STRINGS = 44,
			PIZZICATO_STRINGS = 45,
			ORCHESTRAL_HARP = 46,
			TIMPANI = 47,
			ENSEMBLE_STRINGS = 48,
			SLOW_STRINGS = 49,
			SYNTH_STRINGS_1 = 50,
			SYNTH_STRINGS_2 = 51,
			CHOIR_AAHS = 52,
			VOICE_OOHS = 53,
			SYN_CHOIR = 54,
			ORCHESTRAL_HIT = 55,
			TRUMPET = 56,
			TROMBONE = 57,
			TUBA = 58,
			MUTED_TRUMPET = 59,
			FRENCH_HORN = 60,
			BRASS_ENSEMBLE = 61,
			SYN_BRASS_1 = 62,
			SYN_BRASS_2 = 63,
			SOPRANO_SAX = 64,
			ALTO_SAX = 65,
			TENOR_SAX = 66,
			BARITONE_SAX = 67,
			OBOE = 68,
			ENGLISH_HORN = 69,
			BASSOON = 70,
			CLARINET = 71,
			PICCOLO = 72,
			FLUTE = 73,
			RECORDER = 74,
			PAN_FLUTE = 75,
			BOTTLE_BLOW = 76,
			SHAKUHACHI = 77,
			WHISTLE = 78,
			OCARINA = 79,
			SYN_SQUARE_WAVE = 80,
			SYN_SAW_WAVE = 81,
			SYN_CALLIOPE = 82,
			SYN_CHIFF = 83,
			SYN_CHARANG = 84,
			SYN_VOICE = 85,
			SYN_FIFTHS_SAW = 86,
			SYN_BRASS_AND_LEAD = 87,
			FANTASIA = 88,
			WARM_PAD = 89,
			POLYSYNTH = 90,
			SPACE_VOX = 91,
			BOWED_GLASS = 92,
			METAL_PAD = 93,
			HALO_PAD = 94,
			SWEEP_PAD = 95,
			ICE_RAIN = 96,
			SOUNDTRACK = 97,
			CRYSTAL = 98,
			ATMOSPHERE = 99,
			BRIGHTNESS = 100,
			GOBLINS = 101,
			ECHO_DROPS = 102,
			SCI_FI = 103,
			SITAR = 104,
			BANJO = 105,
			SHAMISEN = 106,
			KOTO = 107,
			KALIMBA = 108,
			BAG_PIPE = 109,
			FIDDLE = 110,
			SHANAI = 111,
			TINKLE_BELL = 112,
			AGOGO = 113,
			STEEL_DRUMS = 114,
			WOODBLOCK = 115,
			TAIKO_DRUM = 116,
			MELODIC_TOM = 117,
			SYN_DRUM = 118,
			REVERSE_CYMBAL = 119,
			GUITAR_FRET_NOISE = 120,
			BREATH_NOISE = 121,
			SEASHORE = 122,
			BIRD = 123,
			TELEPHONE = 124,
			HELICOPTER = 125,
			APPLAUSE = 126,
			GUNSHOT = 127;

	private int instrument;

	/**
	 * Constructs a new Instrument with the given instrument value (0-127).
	 * @param instNum the instrument value
	 */
	public Instrument(int instNum) {
		instrument = instNum;
	}

	/**
	 * Gets the MIDI instrument value for this instrument.
	 * @return the instrument number
	 */
	public int getInstrumentNumber() {
		return instrument;
	}

	/**
	 * Sets the instrument value for this instrument
	 * @param instNum the instrument number (0-127)
	 */
	public void setInstrument(int instNum) {
		instrument = instNum;
	}
	
	/**
	 * Returns a String representation of the instrument.
	 * @return a String representation
	 */
	public String toString() {
		String s = "";
		try {
			s = MidiSystem.getSynthesizer().getDefaultSoundbank().getInstruments()[instrument].getName();
		} catch (MidiUnavailableException e) {
			System.err.println("Error: (instrument) cannot retrieve instrument");
			//e.printStackTrace();
		}
		return s;
	}

}