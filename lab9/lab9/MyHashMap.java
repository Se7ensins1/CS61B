package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by anastasiav on 3/16/2017.
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    public HashSet hs;
    public double load;
    public Entry list;
    private int size;

    public class Entry {
        private K k;
        private V v;
        private Entry n;
        private Entry(K key, V value, Entry next) {
            this.k = key;
            this.v = value;
            this.n = next;
        }
        private Entry get(K k) {
            if (k != null && k.equals(k)) {
                return this;
            }
            if (n == null) {
                return null;
            }
            return n.get(k);
        }
    }

    private class MapIter implements Iterator<K> {

        /** Create a new ULLMapIter by setting cur to the first node in the
         *  linked list that stores the key-value pairs. */
        public MapIter() {
            cur = list;
        }

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public K next() {
            K ret = cur.k;
            cur = cur.n;
            return ret;
        }


        /** Stores the current key-value pair. */
        private Entry cur;

    }

    public MyHashMap() {
        this.hs = new HashSet();
    }
    public MyHashMap(int initialSize) {
        this.hs = new HashSet(initialSize);
    }
    public MyHashMap(int initialSize, double loadFactor) {
        this.hs = new HashSet(initialSize);
        this.load = loadFactor;
    }

    @Override
    public Iterator<K> iterator() {
        return new MapIter();
    }

    public void clear() {
        this.list = null;
        this.size = 0;
    }

    public boolean containsKey(K key) {
        if (list == null) {
            return false;
        }
        return list.get(key) != null;
    }

    public V get(K key) {
        if (list == null) {
            return null;
        }
        Entry lookup = list.get(key);
        if (lookup == null) {
            return null;
        }
        return lookup.v;
    }

    public int size() {
        return this.size;
    }

    public void put(K key, V val) {
        if (list != null) {
            Entry lookup = list.get(key);
            if (lookup == null) {
                list = new Entry(key, val, list);
                size = size + 1;
            } else {
                lookup.v = val;
            }
        } else {
            list = new Entry(key, val, list);
            size = size + 1;
        }
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}
