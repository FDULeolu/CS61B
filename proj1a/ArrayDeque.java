public class ArrayDeque<T> {
    private int size;
    private T[] items;
    private int front;
    private int last;

    /** Creates an empty array deque */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        front = 4;
        last = 4;
    }

    /** A function that control front and last index */
    private int addOne(int index, int module) {
        index %= module;
        if (index == module - 1) {
            return 0;
        }
        return index + 1;
    }

    /** A function that control front and last index */
    private int minusOne(int index) {
        if (index == 0) {
            return items.length - 1;
        }
        return index - 1;
    }

    /** Double the length of the array */
    private void grow() {
        T[] newArray = (T[]) new Object[items.length * 2];
        int ptr1 = front;
        int ptr2 = 0;
        while (ptr1 != last) {
            newArray[ptr2] = items[ptr1];
            ptr2 = addOne(ptr2, items.length * 2);
            ptr1 = addOne(ptr1, items.length);
        }
        newArray[ptr2] = items[ptr1];
        front = 0;
        last = ptr2;
        items = newArray;
    }

    /** Half the length of the array */
    private void shrink() {
        T[] newArray = (T[]) new Object[items.length / 2];
        int ptr1 = front;
        int ptr2 = 0;
        while (ptr1 != last) {
            newArray[ptr2] = items[ptr1];
            ptr2 = addOne(ptr2, items.length / 2);
            ptr1 = addOne(ptr1, items.length);
        }
        newArray[ptr2] = items[ptr1];
        front = 0;
        last = ptr2;
        items = newArray;
    }


    /** Check the length of array to keep the usage ratio is between 25% to 100%,
     * or the size of the deque is less than 16 */
    private void sizeChecker() {
        if (size / items.length < 0.25 && size >= 16) {
            shrink();
        } else if (size > items.length) {
            grow();
        }
    }

    /** Adds an item of type T to the front of the deque in constant time */
    public void addFirst(T item) {
        if (size == 0) {
            items[front] = item;
            size += 1;
        } else {
            size += 1;
            sizeChecker();
            front = minusOne(front);
            items[front] = item;
        }
    }

    /** Adds an item of type T to the back of the deque in constant time */
    public  void addLast(T item) {
        if (size == 0) {
            items[front] = item;
            size += 1;
        } else {
            size += 1;
            sizeChecker();
            last = addOne(last, items.length);
            items[last] = item;
        }
    }

    /** Returns true if deque is empty, false otherwise */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space */
    public void printDeque() {
        if (size == 0) {
            return;
        }
        for (int i = front; i <= last; i = addOne(i, items.length)) {
            System.out.println(items[i]);
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null */
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else if (size == 1) {
            T res = items[front];
            size -= 1;
            return res;
        } else {
            T res = items[front];
            size -= 1;
            sizeChecker();
            front = addOne(front, items.length);
            return res;
        }
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null */
    public T removeLast() {
        if (size == 0) {
            return null;
        } else if (size == 1) {
            T res = items[last];
            size -= 1;
            return res;
        } else {
            T res = items[last];
            size -= 1;
            sizeChecker();
            last = minusOne(last);
            return res;
        }
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null */
    public T get(int index) {
        int ptr = front;
        if (index >= size) {
            return null;
        }
        for (int i = 0; i < index; i += 1) {
            ptr = addOne(ptr, items.length);
        }
        return items[ptr];
    }
}
