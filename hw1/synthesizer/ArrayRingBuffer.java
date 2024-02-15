// TODO: Make sure to make this class a part of the synthesizer package
package synthesizer;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.

        this.capacity = capacity;
        this.fillCount = 0;
        first = 0;
        last = 0;
        rb = (T[]) new Object[capacity];
    }

    private int addOne(int index) {
        return (index + 1) % capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last = addOne(last);
        this.fillCount++;
    }

    /**
     * Dequeue the oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T res = rb[first];
        fillCount--;
        first = addOne(first);
        return res;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        // TODO: Return the first item. None of your instance variables should change.
        return rb[first];
    }


    private class BufferIterator() implements Iterator<T> {

        private int index;
        private int curNum;
        public BufferIterator() {
            index = first;
            curNum = 0;
        }
        @Override
        public boolean hasNext() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return curNum < fillCount;
        }

        @Override
        public T next() {
            T nextValue = rb[index];
            curNum++;
            index = addOne(index);
            return nextValue;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
}
