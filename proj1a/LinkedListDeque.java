/**
 * The double ended queue is very similar to the SLList and AList classes. Linked list based
 */
public class LinkedListDeque<Item> {

    private int size;
    private ListNode first;
    private ListNode last;

    private class ListNode {
        private Item value;
        private ListNode front;
        private ListNode back;

        private ListNode(Item node) {
            this.value = node;
        }

        /** set methods */
        public void setFront(ListNode front) {
            this.front = front;
        }

        public void setBack(ListNode back) {
            this.back = back;
        }
    }

    /**
     * Creates an empty linked list deque.
     */
    public LinkedListDeque() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    /**
     * Adds an item to the front of the Deque.
     * @param a Item that is added
     */
    public void addFirst(Item a) {
        ListNode newNode = new ListNode(a);
        newNode.setBack(this.last);
        newNode.setFront(this.first);
        if (this.size == 0) {
            this.last = newNode;
        } else {
            this.first.setBack(newNode);
            this.last.setFront(newNode);
        }
        this.first = newNode;
        this.size += 1;
    }

    /**
     * Adds an item to the back of the Deque.
     * @param a Item that is added
     */
    public void addLast(Item a) {
        ListNode newNode = new ListNode(a);
        newNode.setBack(this.last);
        newNode.setFront(this.first);
        if (this.isEmpty()) {
            this.first = newNode;
        } else {
            this.first.setBack(newNode);
            this.last.setFront(newNode);
        }
        this.last = newNode;
        this.size += 1;
    }

    /**
     * Removes and returns the item at the back fo the Deque. If no such item exists, returns
     * @return whether of not the Linked List is empty
     */
    public boolean isEmpty() {
        return (this.size == 0);
    }

    /**
     * Determines the number of items in the Deque.
     * @return the number of items in the deque
     */
    public int size() {
        return this.size;
    }

    /**
     * Prints the items in the Deque from first to last, separated by a space.
     */
    public void printDeque() {
        int i = this.size();
        ListNode first2 = this.first;
        while (i > 0) {
            System.out.println(this.first.value);
            i -= 1;
            this.first = this.first.front;
        }
        this.first = first2;
    }

    /**
     * Removes the item at the front of the Deque.
     * @return The item that was removed. If no such item exists, returns null.
     */
    public Item removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        Item val = this.first.value;
        if (this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            this.first = this.first.front;
            this.last.setFront(this.first);
            this.first.setBack(this.last);
        }
        this.size -= 1;
        return val;
    }

    /**
     * Removes the item at the back of the Deque.
     * @return The item that was removed. If no such item exists, returns null
     */
    public Item removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        Item val = this.last.value;
        if (this.size == 1) {
            this.first = null;
            this.last = null;
        } else {
            this.last = this.last.back;
            this.last.setFront(this.first);
            this.first.setBack(this.last);
        }
        this.size -= 1;
        return val;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next, etc.
     * @param index int location of where to get value in array from
     * @return the item at the given index. If no such item exists, returns null.
     *          Must not alter the deque!
     */
    public Item get(int index) {
        ListNode node = this.first;
        while (index > 0) {
            node = node.front;
            index -= 1;
        }
        return node.value;
    }

    /**
     * Same as get, but uses recursion.
     * @param index int location of where to get value in array from
     * @return the item at the given index. If no such item exists, returns null.
     *          Must not alter the deque!
     */
    public Item getRecursive(int index) {
        ListNode fir = this.first;
        Item in = this.helper(index);
        this.first = fir;
        return in;
    }

    /**
     * getRecursive's helper function
     * @param index int location of where to get value in array from
     * @return the item at the given index. If no such item exists, returns null.
     *          Must not alter the deque!
     */
    private Item helper(int index) {
        if (index == 0) {
            return this.first.value;
        }
        this.first = this.first.front;
        return this.helper(index - 1);
    }
}
