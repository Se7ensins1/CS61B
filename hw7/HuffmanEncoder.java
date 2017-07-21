import java.io.*;
import java.util.*;

/**
 * Created by anastasiav on 4/27/2017.
 */
public class HuffmanEncoder {

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char i : inputSymbols) {
            if (freq.containsKey(i)) {
                freq.put(i, freq.get(i) + 1);
            } else {
                freq.put(i, 1);
            }
        }
        return freq;
    }
    public static void main(String[] args) {
        String fileName = args[0];

        char[] characters = FileUtils.readFile(fileName);
        System.out.println(characters);
        Map<Character, Integer> freq = buildFrequencyTable(characters);
        for (char c : freq.keySet()) {
            System.out.println(c + " | " + (int) c + " | " + freq.get(c));
        }
        ObjectWriter ow = new ObjectWriter(fileName + ".huf");
        ArrayList<BitSequence> bitSeq = new ArrayList<>();

        BinaryTrie b = new BinaryTrie(freq);
        Map<Character, BitSequence> lookupTable = b.buildLookupTable();

        for (char c : characters) {
            BitSequence charBit = lookupTable.get(c);
            bitSeq.add(charBit);
        }
        BitSequence combinedBit = BitSequence.assemble(bitSeq);

        ow.writeObject(b);
        ow.writeObject(characters.length);
        ow.writeObject(combinedBit);
    }
}