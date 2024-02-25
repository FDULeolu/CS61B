package lab9;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int index = hash(key);
        return buckets[index].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (!buckets[index].containsKey(key)) {
            size++;
        }
        buckets[index].put(key, value);
        if ((double) size / buckets.length > MAX_LF) {
            ArrayMap<K, V>[] newBuckets = new ArrayMap[buckets.length * 2];
            for (int i = 0; i < newBuckets.length; i++) {
                newBuckets[i] = new ArrayMap<>();
            }
            for (int i = 0; i < buckets.length; i++) {
                for (K k : buckets[i]) {
                    int newIndex = Math.floorMod(k.hashCode(), newBuckets.length);
                    newBuckets[newIndex].put(k, buckets[i].get(k));
                }
            }
            buckets = newBuckets;
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (int i = 0; i < buckets.length; i++) {
            for (K k : buckets[i]) {
                keySet.add(k);
            }
        }
        return keySet;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int index = hash(key);
        if (index >= 0 && index < buckets.length) {
            V value = buckets[index].get(key);
            buckets[index].remove(key);
            return value;
        }
        return null;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int index = hash(key);
        if (index >= 0 && index < buckets.length && buckets[index].get(key) == value) {
            buckets[index].remove(key, value);
            return value;
        }
        return null;
    }

    private class HashMapIterator implements Iterator<K> {

        private LinkedList<K> lst;
        private int index;

        public HashMapIterator() {
            index = 0;
            lst = new LinkedList<>();
            lst.addAll(keySet());
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return lst.get(index++);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }
}
