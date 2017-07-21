import java.util.ArrayList;

/**
 * Created by anastasiav on 4/27/2017.
 */
public class HuffmanDecoder {
    public static void main(String[] args) {

        String fileNameHug = args[0];
        String fileName = args[1];

        ObjectReader read = new ObjectReader(fileNameHug);
        Object trie = read.readObject();
        Object num = read.readObject();
        Object bit = read.readObject();

        BinaryTrie tree = (BinaryTrie) trie;
        int chars = (int) num;
        BitSequence bits = (BitSequence) bit;
        char[] c = new char[chars];

        for (int i = 0 ; i < chars; i++) {
            Match m = tree.longestPrefixMatch(bits);
            c[i] = m.getSymbol();
            System.out.print(m.getSymbol());
            bits = bits.lastNBits(bits.length() - m.getSequence().length());
        }

        FileUtils.writeCharArray(fileName, c);
    }
}