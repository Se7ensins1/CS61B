/**
 * Checks the LinkedListDeque's implementation.
 */
public class LinkedListDequeTest {
    private static final int INTEGER1 = 13;
    private static final int INTEGER2 = 10;
    private static final int INTEGER3 = 20;
    private static final int INTEGER4 = 30;

    /* Utility method for printing out empty checks. */
    private static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    private static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out get values.*/
    private static boolean checkVal(int expected, int actual) {
        if (expected != actual) {
            System.out.println("get() returned " + actual + ", expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out get equality.*/
    private static boolean checkEq(Object expected, Object actual) {
        if (expected != actual) {
            System.out.println("it returned " + actual + ", expected: " + expected);
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

    /**
     * Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     */
    private static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        LinkedListDeque<String> lld1 = new LinkedListDeque<>();
        boolean passed = checkEmpty(true, lld1.isEmpty());
        lld1.addFirst("front");
        passed = checkSize(1, lld1.size()) && passed;
        passed = checkEmpty(false, lld1.isEmpty()) && passed;
        lld1.addLast("middle");
        passed = checkSize(2, lld1.size()) && passed;
        lld1.addLast("back");
        passed = checkSize(3, lld1.size()) && passed;
        System.out.println("Printing out deque: ");
        lld1.printDeque();
        printTestStatus(passed);
    }

    /**
     * Adds an item, then removes an item, and ensures that dll is empty afterwards.
     */
    private static void addRemoveTest() {
        System.out.println("Running add/remove test.");
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        boolean passed = checkEmpty(true, lld1.isEmpty());
        lld1.addFirst(INTEGER2);
        passed = checkEmpty(false, lld1.isEmpty()) && passed;
        passed = checkEq(INTEGER2, lld1.removeFirst()) && passed;
        passed = checkEmpty(true, lld1.isEmpty()) && passed;
        lld1.addFirst(INTEGER1);
        passed = checkEq(INTEGER1, lld1.removeLast()) && passed;
        printTestStatus(passed);

    }

    /**
     * Adds items, then checks if the input index holds the correct values
     */
    private static void getIterRecurTest() {
        System.out.println("Running get iterative tests.");
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(INTEGER2);
        lld1.addFirst(INTEGER3);
        lld1.addFirst(INTEGER4);
        boolean passed1 = checkVal(INTEGER3, lld1.get(1));
        printTestStatus(passed1);

        System.out.println("Running get recursive tests.");
        boolean passed2 = checkVal(INTEGER3, lld1.getRecursive(1));
        printTestStatus(passed2);
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        getIterRecurTest();
    }
}
