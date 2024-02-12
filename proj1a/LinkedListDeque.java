public class LinkedListDeque<T> {

    /** A Nested Classes for LinkedListDeque */
    public class TNode {
        public TNode prev;
        public T item;
        public TNode next;
        public TNode(T t, TNode p, TNode n) {
            prev = p;
            item = t;
            next = n;
        }

        public TNode(TNode p, TNode n) {
            prev = p;
            next = n;
        }
    }

    private int size;
    private TNode sentinel;

    /** Creates an empty linked list deque */
    public LinkedListDeque() {
        size = 0;
        sentinel = new TNode(null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {
        size += 1;
        sentinel.next.prev = new TNode(item, sentinel.next, sentinel);
        sentinel.next = sentinel.next.prev;
    }

    /** Adds an item of type T to the back of the deque */
    public  void addLast(T item) {
        size += 1;
        sentinel.prev.next = new TNode(item, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
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
        TNode tPointer = sentinel.next;
        while (tPointer != sentinel) {
            System.out.println(tPointer.item + " ");
            tPointer = tPointer.next;
        }
    }

    /** Removes and returns the item at the front of the deque. If no such item exists, returns null */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T ret = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return ret;
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, returns null */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T ret = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return ret;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        TNode ptr = sentinel;
        for (int i = 0; i <= index; i++) {
            ptr = ptr.next;
        }
        return ptr.item;
    }

    /** A helper function for getRecursive() */
    private T getRecursiveHelper(TNode start, int index) {
        if (index == 0) {
            return start.item;
        }
        return getRecursiveHelper(start.next, index - 1);
    }

    /** Same as get(), but uses recursion */
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }
}
