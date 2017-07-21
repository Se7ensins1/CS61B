import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by anastasiav on 2/4/2017.
 */
public class TestArrayDeque1B {
    @Test
    public void test1() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        OperationSequence fs = new OperationSequence();
        DequeOperation op;
        for (int i = 0; i < 100; i++) {
            double num = StdRandom.uniform();
            Integer x = 0;
            Integer y = 0;

            if (0 <= num && num < 0.25) {
                student.addFirst(i);
                solution.addFirst(i);
                op = new DequeOperation("addFirst", i);
                fs.addOperation(op);
            } else if (0.25 <= num && num < 0.5) {
                student.addLast(i);
                solution.addLast(i);
                op = new DequeOperation("addLast", i);
                fs.addOperation(op);
            } else if (0.5 <= num && num < 0.75 && student.size() > 0) {
                x = student.removeFirst();
                y = solution.removeFirst();
                op = new DequeOperation("removeFirst");
                fs.addOperation(op);
            } else if (0.75 <= num && num < 1 && student.size() > 0) {
                x = student.removeLast();
                y = solution.removeLast();
                op = new DequeOperation("removeLast");
                fs.addOperation(op);
            }

            assertEquals(fs.toString(), x, y);
        }
        if (solution.size() > 1) {
            Integer j = student.get(1);
            Integer k = solution.get(1);
            assertEquals(fs.toString(), j, k);
        }
    }
}
