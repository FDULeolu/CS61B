
import synthesizer.ArrayRingBuffer;

import java.util.Iterator;

public class TestArrayRingBufferIterator {
    public void testNext() {
        ArrayRingBuffer<Integer> myArray = new ArrayRingBuffer<>(5);

        for (int i = 0; i < 5; i++) {
            myArray.enqueue(i);
        }

        Iterator<Integer> myInterator = myArray.iterator();
        System.out.println(myInterator.hasNext());
        System.out.println(myInterator.next());

    }

    public static void main(String[] args) {
        TestArrayRingBufferIterator test = new TestArrayRingBufferIterator();

        test.testNext();
    }
}
