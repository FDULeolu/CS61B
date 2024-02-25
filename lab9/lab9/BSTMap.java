package lab9;

import java.util.*;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            return getHelper(key, p.left);
        } else if (cmp > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            p = new Node(key, value);
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (cmp > 0) {
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
        size++;
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
        return keySetHelper(root);
    }

    private Set<K> keySetHelper(Node p) {
        Set<K> keySet =  new HashSet<>();
        if (p == null) {
            return keySet;
        }
        keySet.add(p.key);
        keySet.addAll(keySetHelper(p.left));
        keySet.addAll(keySetHelper(p.right));
        return keySet;
    }

    private Node findMaxInLeftTree(Node x) {
        if (x.left == null && x.right == null) {
            return x;
        } else if (x.right != null) {
            return findMaxInLeftTree(x.right);
        } else {
            return x;
        }
    }

    private Node deleteFromKey(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = deleteFromKey(x.left, key);
            return x;
        } else if (cmp > 0) {
            x.right = deleteFromKey(x.right, key);
            return x;
        } else {
            if (x.left == null && x.right == null) {
                return null;
            } else if (x.left != null && x.right == null) {
                x = x.left;
                return x;
            } else if (x.left == null) {
                x = x.right;
                return x;
            } else {
                Node maxNode = findMaxInLeftTree(x.left);
                x.value = maxNode.value;
                x.key = maxNode.key;
                x.left = deleteFromKey(x.left, maxNode.key);
                return x;
            }
        }
    }

    private Node deleteFromKeyAndValue(Node x, K key, V value) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = deleteFromKeyAndValue(x.left, key, value);
            return x;
        } else if (cmp > 0) {
            x.right = deleteFromKeyAndValue(x.right, key, value);
            return x;
        } else {
            if (x.value != value) {
                return null;
            } else {
                if (x.left == null && x.right == null) {
                    return null;
                } else if (x.left != null && x.right == null) {
                    x = x.left;
                    return x;
                } else if (x.left == null) {
                    x = x.right;
                    return x;
                } else {
                    Node maxNode = findMaxInLeftTree(x.left);
                    x.value = maxNode.value;
                    x.key = maxNode.key;
                    x.left = deleteFromKey(x.left, maxNode.key);
                    return x;
                }
            }
        }
    }

        /** Removes KEY from the tree if present
         *  returns VALUE removed,
         *  null on failed removal.
         */
    @Override
    public V remove(K key) {
        V value = get(key);
        root = deleteFromKey(root, key);
        size--;
        return value;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        root = deleteFromKeyAndValue(root, key, value);
        size--;
        return value;
    }

    private class BSTIterator implements Iterator<K> {

        int index;
        LinkedList<K> lst;

        public BSTIterator() {
            lst = new LinkedList<>();
            index = 0;
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
        return new BSTIterator();
    }
}
