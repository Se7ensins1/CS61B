package hw3.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.PriorityQueue;

/**
 * Created by anastasiav on 3/21/2017.
 */
public class Solver {

    public PriorityQueue<WorldState> prior;
    private int moves;
    private WorldState[] solutions;

    /**
     * Constructor which solves the puzzle, computing
     everything necessary for moves() and solution() to
     not have to solve the problem again. Solves the
     puzzle using the A* algorithm. Assumes a solution exists.
     * @param initial
     */
    public Solver(WorldState initial) {
        this.moves = 0;
        while (!initial.isGoal()) {
            prior.add(initial);
            this.moves += 1;
        }

    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     at the initial WorldState.
     * @return
     */
    public int moves() {
        return this.moves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     to the solution.
     * @return
     */
    public Iterable<WorldState> solution() {
        return null;
    }
}