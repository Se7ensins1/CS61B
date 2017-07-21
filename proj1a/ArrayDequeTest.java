/**
 * Tests the ArrayDeque's implementation
 */
public class ArrayDequeTest {

    private static final int INTEGER1 = 10;
    private static final int INTEGER2 = 64;
    private static final int INTEGER3 = 2;
    private static final int INTEGER4 = 41;

    private static boolean checkEqual(Object expected, Object actual) {
        if (expected != actual) {
            System.out.println("it returned " + actual + ", but " + expected + " was expected.");
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed. */
    private static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Adds items to the front and back of the deque, then checks if it is added correctly*/
    private static void addTest() {
        System.out.println("Running add first tests.");
        ArrayDeque<Integer> a1 = new ArrayDeque<>();
        boolean passed = checkEqual(0, a1.size());
        a1.addFirst(INTEGER1);
        passed = checkEqual(1, a1.size()) && passed;
        a1.addFirst(INTEGER2);
        passed = checkEqual(2, a1.size()) && passed;
        a1.printDeque();
        printTestStatus(passed);

        System.out.println("Running add last tests.");
        a1.addLast(INTEGER3);
        passed = checkEqual(3, a1.size()) && passed;
        a1.addLast(INTEGER4);
        passed = checkEqual(4, a1.size()) && passed;
        a1.printDeque();
        printTestStatus(passed);
    }

    /** Removes items from the front and back of the deque, then checks if it is removed correctly*/
    private static void removeTest() {

        System.out.println("Running remove tests.");
        ArrayDeque<Integer> a1 = new ArrayDeque<>();
        boolean passed = checkEqual(0, a1.size());
        a1.addFirst(INTEGER1);
        a1.addFirst(INTEGER2);
        a1.addLast(INTEGER3);
        a1.addLast(INTEGER4);
        passed = checkEqual(4, a1.size()) && passed;
        System.out.println("initial");
        a1.printDeque();
        passed = checkEqual(INTEGER2, a1.removeFirst()) && passed;
        passed = checkEqual(3, a1.size()) && passed;
        System.out.println("removefirst");
        a1.printDeque();
        passed = checkEqual(INTEGER4, a1.removeLast()) && passed;
        passed = checkEqual(2, a1.size()) && passed;
        System.out.println("removelast");
        a1.printDeque();
        passed = checkEqual(10, a1.removeFirst()) && passed;
        passed = checkEqual(1, a1.size()) && passed;
        System.out.println("removefirst");
        a1.printDeque();
        printTestStatus(passed);
    }

    /** Adds items to a deque, then sees whether they are placed in the correct index. */
    private static void getTest() {
        ArrayDeque<Integer> a1 = new ArrayDeque<>();
        a1.addLast(INTEGER1);
        a1.addFirst(INTEGER2);
        a1.addLast(INTEGER3);
        a1.printDeque();
        boolean passed = checkEqual(2, a1.get(2));
        passed = checkEqual(10, a1.get(1)) && passed;
        passed = checkEqual(INTEGER2, a1.get(0)) && passed;
        printTestStatus(passed);
    }

    /** Adds 20 items to a deque, then makes sure the item is resized */
    private static void resizeTest() {

        System.out.println("running add first 10x resize test...");
        ArrayDeque<Integer> a1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            a1.addFirst(INTEGER1);
        }
        a1.printDeque();

        System.out.println("running remove last 6x resize test...");
        for (int i = 0; i < 6; i++) {
            a1.removeLast();
        }
        a1.printDeque();

        System.out.println("running add last 10x resize test...");
        ArrayDeque<Integer> a2 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            a2.addLast(INTEGER1);
        }
        a2.printDeque();

        System.out.println("running remove first 8x resize test...");
        for (int i = 0; i < 8; i++) {
            a2.removeFirst();
        }
        a2.printDeque();
    }

    public static void autograderTests() {
        ArrayDeque a = new ArrayDeque();
        a.addLast(0);
        boolean passed = checkEqual(0, a.removeFirst());
        a.addLast(3);
        passed = checkEqual(3, a.removeFirst()) && passed;
        printTestStatus(passed);

        ArrayDeque a2 = new ArrayDeque();
        a2.addFirst(0);
        boolean passed2 = checkEqual(0, a2.removeFirst());
        a2.addFirst(2);
        passed2 = checkEqual(2, a2.removeFirst()) && passed2;
        a2.addFirst(4);
        passed2 = checkEqual(4, a2.removeFirst()) && passed2;
        a2.addFirst(9);
        passed2 = checkEqual(9, a2.removeFirst()) && passed2;
        a2.addFirst(1);
        printTestStatus(passed2);

        ArrayDeque a3 = new ArrayDeque();
        a3.addFirst(0);
        boolean passed3 = checkEqual(0, a3.removeLast());
        a3.addFirst(3);
        passed3 = checkEqual(3, a3.removeLast()) && passed3;
        a3.addFirst(6);
        a3.removeLast();
        printTestStatus(passed3);
    }

    public static void main(String[] args) {
        System.out.println("Running tests...");
        addTest();
        removeTest();
        getTest();
        resizeTest();
        autograderTests();
    }

}
