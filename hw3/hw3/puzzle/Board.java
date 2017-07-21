package hw3.puzzle;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.MinPQ;

public class Board implements WorldState {
    public int[][] board;
    private static final int BLANK = 0;
    private int distToGoal;

    /**
     * Constructs a board from an N-by-N array of tiles where
     tiles[i][j] = tile at row i, column j
     * @param tiles
     */
    public Board(int[][] tiles) {
        this.board = tiles;
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     * @param i
     * @param j
     * @return
     */
    public int tileAt(int i, int j) {
        if (i < 0 || i > this.size()-1 || j < 0 || j > this.size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        return this.board[i][j];
    }

    /**
     * Returns the board size N
     * @return
     */
    public int size() {
        return this.board[0].length;
    }

    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int count = 0;
        int ham = 0;
        for (int i = 0 ; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                if (this.board[i][j] != count) {
                    ham += 1;
                }
                count += 1;
            }
        }
        return ham;
    }

    public int manhattan() {
        int count = 0;
        int man = 0;
        for (int i = 0 ; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                if (this.board[i][j] != count) {
                    int countX = (count - 1) % this.size();
                    int countY = (count - 1) / this.size();
                    int diffX = Math.abs(countX - i);
                    int diffY = Math.abs(countY - j);
                    man += diffX + diffY;
                }
                count += 1;
            }
        }
        return man;
    }

    public int estimatedDistanceToGoal() {
        return this.distToGoal;
    }

    public boolean isGoal() {
        return this.distToGoal == 0;
    }

    public boolean equals(Object y) {
        return false;
    }

    /** Returns the string representation of the board. 
     * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
