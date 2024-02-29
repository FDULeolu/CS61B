import java.awt.*;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        if (asciis.length <= 1) {
            return asciis;
        }
        String max = asciis[0];
        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            if (stringToAscii(max) < stringToAscii(asciis[i])) {
                max = asciis[i];
            }
            sorted[i] = asciis[i];
        }

        for (int i = 1; stringToAscii(max) / i > 0; i *= 256) {
            sortHelperLSD(sorted, i);
        }

        return sorted;
    }

    private static int stringToAscii(String s) {
        int ascii = 0;
        for (int i = 0; i < s.length(); i++) {
            ascii *= 256;
            ascii += s.charAt(i);
        }
        return ascii;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int max = Integer.MIN_VALUE;
        for (String s : asciis) {
            max = Math.max(max, (stringToAscii(s) / index) % 256);
        }

        int[] counts = new int[max + 1];
        for (String s : asciis) {
            counts[(stringToAscii(s) / index) % 256]++;
        }

        for (int i = 1; i < counts.length; i++) {
            counts[i] += counts[i - 1];
        }

        String[] output = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            output[counts[((stringToAscii(asciis[i])) / index ) % 256] - 1] = asciis[i];
            counts[((stringToAscii(asciis[i])) / index ) % 256]--;
        }

        for (int i = 0; i < output.length; i++) {
            asciis[i] = output[i];
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] sorted;
        String[] strings = {"bdb", "ca", "aba"};
        sorted = sort(strings);
        for (String s : sorted) {
            System.out.println(s);
        }
    }

}
