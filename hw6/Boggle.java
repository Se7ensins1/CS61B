
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Boggle {
    public VocabTree dic;
    public ArrayList<ArrayList<Node>> board;
    public int width;
    public int height;

    public class VocabTree {
        private HashMap<String, VocabTree> words;
        private String charTill;

        public VocabTree() {
            this.words = new HashMap<>();
        }

        public String getCharTill() {
            return this.charTill;
        }

        public void insertWord(String word, String origWord) {
            if (word.equals("")) {
                this.charTill = origWord;
                return;
            }
            String chair = Character.toString(word.charAt(0));
            if (!this.words.containsKey(chair)) {
                this.words.put(chair, new VocabTree());
            }
            VocabTree t = this.words.get(word.substring(0, 1));
            t.insertWord(word.substring(1), origWord);
        }

        public VocabTree getWords(String word) {
            if (word == null) {
                return null;
            }
            if (word.equals("")) {
                return this;
            }
            VocabTree t = this.words.get(word.substring(0, 1));
            return t.getWords(word.substring(1));
        }
    }

    public class Node {
        public String character;
        public ArrayList<Node> neighbors;

        public Node(String cha) {
            this.character = cha;
            this.neighbors = new ArrayList<>();
        }

        public void addNeighbor(Node n) {
            this.neighbors.add(n);
        }
    }

    public Boggle(int w, int h, String boardFile, String dicFile) {
        this.width = w;
        this.height = h;
        // filling the board
        try {
            File file = new File(boardFile);
            Scanner scan = new Scanner(file);
            int c = 0;
            int r = 0;
            while (scan.hasNext()) {
                if () {
                    c++;
                    r = 0;
                } else {
                    this.board.get(c).get(r) = new Node(scan.next());
                    r++;
                }
            }

            this.addNeighbors();
        } catch (IOException e) {
            System.out.println("can not find boardFile " + boardFile);
        }

        // Filling the dictionary
        List<String> dictionary = new ArrayList<>();
        try {
            dictionary = Files.readAllLines(Paths.get(dicFile));
        } catch (IOException e) {
            System.out.println("can not find dicFile " + dicFile);
        }

        this.dic = new VocabTree();
        for (String w : dictionary) {
            this.dic.insertWord(w, w);
        }
    }

    public void addNeighbors() {
        for (int c = 0; c < height; c++) {
            for (int r = 0; r < width; r++) {
                if (c == 0 && r == 0) {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c+1));
                } else if (c == height - 1 && r == 0) {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c-1));
                } else if (c == 0 && r == width - 1) {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c+1));
                } else if (c == height - 1 && r == width - 1) {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c-1));
                } else if (c == 0) {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c+1));
                } else if (c == height - 1) {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c-1));
                } else if (r == 0) {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c-1));
                } else if (r == height - 1) {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c+1));
                } else {
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r-1).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r+1).get(c+1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c-1));
                    this.board.get(r).get(c).addNeighbor(this.board.get(r).get(c+1));
                }
            }
        }
    }

    public ArrayList<String> findWords() {
        ArrayList<String> wordsInBoard = new ArrayList<>();

        ArrayList<Node> deque = new ArrayList<>();
        for (int c = 0; c < this.height; c++) {
            for (int r = 0; r < this.width; r++) {

                deque.add(this.board.get(c).get(r));
            }
        }
        return wordsInBoard;
    }

    public static void main(String[] arg) {
        String[] args = new String[6];
        args[0] = "-d";
        args[1] = "testDict";
        args[2] = "-k";
        args[3] = "20";
        args[4] = "<";
        args[5] = "test";
        int i = 0;
        int words = -1;
        int width = 0;
        int height = 0;
        boolean random = false;
        String boardFile = "";
        String dicFile = "words";
        while (i < args.length - 1) {
            if (args[i].equals("-k")) { // (-k [number of words])
                i++;
                words = Integer.parseInt(args[i]);
                i++;
                System.out.print("k");
            } else if (args[i].equals("-n")) { // (-n [width])
                i++;
                width = Integer.parseInt(args[i]);
                i++;
                System.out.print("n");
            } else if (args[i].equals("-m")) { // (-m [height])
                i++;
                height = Integer.parseInt(args[i]);
                i++;
                System.out.print("m");
            } else if (args[i].equals("-d")) { // (-d [path to dictionary])
                i++;
                dicFile = args[i];
                i++;
            } else if (args[i].equals("-r")) { // random option
                random = true;
                i++;
            } else if (args[i].equals("<")) { // input board file
                boardFile = args[i];
                i++;
            }
        }

        Boggle b = new Boggle(width, height, boardFile, dicFile);
        ArrayList<String> wordsInBoard = b.findWords();
        int wor = 0;
        while (wor < words) {
            System.out.println(wordsInBoard.get(wor));
            wor++;
        }
    }

}