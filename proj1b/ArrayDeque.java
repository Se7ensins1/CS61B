/**
 * Created by anastasiav on 1/31/2017.
 */
public class ArrayDeque<Item> implements Deque<Item> {

    private static final int MINSIZEREDUCE = 8;
    private Item[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        this.items = (Item[]) new Object[8];
        this.size = 0;
        this.first = 0;
        this.last = 1;
    }

    /* Set Methods */
    private void incFirst() {
        this.first++;
        this.first = this.first % this.items.length;
    }
    private void incLast() {
        this.last++;
        this.last = this.last % this.items.length;
    }
    private void decFirst() {
        this.first--;
        if (this.first < 0) {
            this.first = this.items.length - 1;
        }
    }
    private void decLast() {
        this.last--;
        if (this.last < 0) {
            this.last = this.items.length - 1;
        }
    }

    /**
     * Adds an item to the front of the Deque.
     * @param a Item object that is added
     */
    @Override
    public void addFirst(Item a) {
        this.resize();
        items[first] = a;
        this.decFirst();
        this.size++;
    }

    /**
     * Adds an item to the back of the Deque.
     * @param a Item object that is being added
     */
    @Override
    public void addLast(Item a) {
        this.resize();
        items[last] = a;
        this.incLast();
        this.size++;
    }

    /**
     * Determines whether or not the deque is empty.
     * @return true if the deque is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (this.size == 0);
    }

    /**
     * Returns the number of items in the Deque
     * @return integer size of deque
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Prints the item in the Deque from first to last, separated by a space.
     */
    @Override
    public void printDeque() {
        for (Item a : this.items) {
            System.out.println(a);
        }
    }

    /**
     * Removes the item at the front of the Deque.
     * @return the item removed. If no such item exists, returns null.
     */
    @Override
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        this.incFirst();
        Item las = items[this.first];
        items[this.first] = null;
        this.size--;
        this.resize();
        return las;
    }

    /**
     * Removes the item at the back of the Deque.
     * @return the item removed. If no such item exists, returns null.
     */
    @Override
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        this.decLast();
        Item fir = items[this.last];
        items[this.last] = null;
        this.size--;
        this.resize();
        return fir;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * @param index int of where on the list to get value from
     * @return item at the given index. If no such item exists, returns null.
     */
    @Override
    public Item get(int index) {
        int i = (this.first + index + 1) % this.items.length;
        return items[i];
    }

    /**
     * Resizes the items array to be larger or smaller.
     */
    private void resize() {
        while (this.last == this.first) {
            // adding resize: if first and last point to same item
            //double items length
            Item[] a = (Item []) new Object[2 * this.items.length];
            System.arraycopy(this.items, 0, a, 0, this.last);
            int desPos = a.length - (this.items.length - this.first - 1);
            int length = this.items.length - this.first - 1;
            System.arraycopy(this.items, this.first + 1, a, desPos, length);
            this.first += this.items.length;
            this.items = a;
        }
        while (this.size <= (.25 * this.items.length) && this.size > MINSIZEREDUCE) {
            // removing resize: if size is less than 25% of items.length
            Item[] a = (Item []) new Object[(int) (.5 * this.items.length)];
            if (this.last > this.first) {
                System.arraycopy(this.items, this.first + 1, a, 0, this.last - this.first - 1);
                this.last -= this.first;
                this.first = 0;
            } else {
                System.arraycopy(this.items, 0, a, 0, this.last);
                int desPos = a.length - (this.items.length - this.first - 1);
                int length = this.items.length - this.first - 1;
                System.arraycopy(this.items, this.first + 1, a, desPos, length);
                this.first -= a.length;
            }
            this.items = a;
        }
    }
}
