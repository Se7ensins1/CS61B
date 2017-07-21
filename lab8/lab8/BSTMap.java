package lab8;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node list;
    private int size;

    private class Node {
        K key;
        V value;
        Node next;

        Node(K key, V value, Node next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }

        Node get(K k) {
            if (k != null && k.equals(this.key)) {
                return this;
            }
            if (next == null) {
                return null;
            }
            return next.get(key);
        }
    }

    public Iterator<K> iterator() {
        return new BSTIterator();
    }

    /** An iterator that iterates over the keys of the dictionary. */
    private class BSTIterator implements Iterator<K> {

        /** Create a new ULLMapIter by setting cur to the first node in the
         *  linked list that stores the key-value pairs. */
        BSTIterator() {
            cur = list;
        }

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public K next() {
            K ret = cur.key;
            cur = cur.next;
            return ret;
        }

        /** Stores the current key-value pair. */
        private Node cur;

    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        this.size = 0;
        this.list = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (list == null) {
            return false;
        }
        return list.get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        if (this.list == null) {
            return null;
        }
        Node lookup = this.list.get(key);
        if (lookup == null) {
            return null;
        }
        return lookup.value;
    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return this.size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        if (this.list != null) {
            Node lookup = list.get(key);
            if (lookup == null) {
                this.list = new Node(key, value, this.list);
            } else {
                lookup.value = value;
            }
        } else {
            this.list = new Node(key, value, this.list);

        }
        this.size += 1;
    }

    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {

    }
}
