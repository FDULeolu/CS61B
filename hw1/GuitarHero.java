import synthesizer.GuitarString;

import java.util.HashMap;
import java.util.Map;

public class GuitarHero {
    private static final Map<Character, Integer> KEYBOARD_INDEX_MAP = new HashMap<>();

    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    private GuitarString[] strings;
    private double[] vol;

    private double mixString() {
        double sample = 0.0;
        for (int i = 0; i < strings.length; i++) {
            sample += strings[i].sample() * vol[i];
        }
        return sample;
    }

    private void tic() {
        for (int i = 0; i < strings.length; i++) {
            if (vol[i] > 0) {
                strings[i].tic();
            }
        }
    }

    public GuitarHero() {
        strings = new GuitarString[37];
        vol = new double[37];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = new GuitarString(440.0 * Math.pow(2.0, (i - 24.0) / 12.0));
            KEYBOARD_INDEX_MAP.put(KEYBOARD.charAt(i), i);
            vol[i] = 0.0;
        }
    }

    public static void main(String[] args) {
        GuitarHero guitarHero = new GuitarHero();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (KEYBOARD_INDEX_MAP.containsKey(key)) {
                    int index = KEYBOARD_INDEX_MAP.get(key);
                    guitarHero.strings[index].pluck();
                    guitarHero.vol[index] = 1.0;
                }
            }
            StdAudio.play(guitarHero.mixString());

            for (int i = 0; i < guitarHero.vol.length; i++) {
                guitarHero.vol[i] *= 0.9999;
            }

            guitarHero.tic();
        }
    }
}
