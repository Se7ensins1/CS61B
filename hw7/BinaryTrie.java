import java.io.Serializable;
import java.util.*;

/**
 * Created by anastasiav on 4/27/2017.
 */

public class BinaryTrie implements Serializable {
    public Map<Character, Integer> freqTable;
    public Node root;
    public Map<Character, BitSequence> bit;

    public class Node implements Serializable {
        private int val;
        private char chara;
        private BitSequence bit;
        private Node zero;
        private Node one;
        private Node parent;

        public Node(int val, char c, Node z, Node o) {
            this.val = val;
            this.chara = c;
            this.zero = z;
            this.one = o;
            this.bit = new BitSequence();
        }

        public void setParent(Node n) {
            this.parent = n;
        }
        public void setBit(BitSequence b) {
            this.bit = b;
        }

        public int compareTo(Node other) {
            return this.val - other.val;
        }

        public boolean isLeaf() {
            return (this.zero == null && this.one == null);
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        this.freqTable = frequencyTable;
        this.bit = new HashMap<>();

        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.compareTo(o2);
            }
        });
        for (Character x : freqTable.keySet()) {
            int val = freqTable.get(x);
            pq.add(new Node(val, x, null, null));
        }
        while (pq.size() > 1) {
            Node zero = pq.peek();
            pq.remove(zero);
            Node one = pq.peek();
            pq.remove(one);
            Node parent = new Node(zero.val + one.val, '\0' , zero, one);
            zero.setParent(parent);
            one.setParent(parent);
            pq.add(parent);
        }
        this.root = pq.peek();

        ArrayList<Node> zeros = new ArrayList<>();
        ArrayList<Node> ones = new ArrayList<>();
        zeros.add(this.root.zero);
        ones.add(this.root.one);
        while (zeros.size() != 0) {
            while (zeros.size() != 0) {
                Node n = zeros.remove(0);
                BitSequence copyBit = n.parent.bit;
                n.setBit(copyBit.appended(0));
                if (n.zero != null) {
                    zeros.add(n.zero);
                }
                if (n.one != null) {
                    ones.add(n.one);
                }
                if (n.isLeaf()) {
                    bit.put(n.chara, n.bit);
                }
            }
            while (ones.size() != 0) {
                Node n = ones.remove(0);
                BitSequence copyBit = n.parent.bit;
                n.setBit(copyBit.appended(1));
                if (n.zero != null) {
                    zeros.add(n.zero);
                }
                if (n.one != null) {
                    ones.add(n.one);
                }
                if (n.isLeaf()) {
                    bit.put(n.chara, n.bit);
                }
            }
        }
        int i = 0;
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node n = parserHelper(querySequence, this.root);
        Match m = new Match(n.bit, n.chara);
        return m;
    }

    public Node parserHelper(BitSequence seq, Node n) {
        Node no = n;
        if (seq.bitAt(0) == 0 && n.zero != null) {
            return parserHelper(seq.lastNBits(seq.length() - 1), n.zero);
        } else if (seq.bitAt(0) == 1 && n.one != null) {
            return parserHelper(seq.lastNBits(seq.length() - 1), n.one);
        } else {
            return n;
        }
    }

    public Map<Character, BitSequence> buildLookupTable() {
        return bit;
    }
}
