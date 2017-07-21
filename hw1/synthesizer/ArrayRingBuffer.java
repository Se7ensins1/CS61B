package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
        this.rb = (T[]) new Object[capacity];
    }

    /**
     * Checks if the list is already full.
     */
    private void checkCapacity(boolean checkEmpty) {
        if (this.fillCount == 0 && checkEmpty) {
            throw new RuntimeException("Ring buffer underflow");
        } else if (this.fillCount + 1 > this.capacity && !checkEmpty) {
            throw new RuntimeException("Ring buffer overflow");
        }
    }

    /**
     * Wrap the first/last numbers around.
     */
    private void wrapIndices() {
        this.first %= this.rb.length;
        this.last %= this.rb.length;
        if (this.first < 0) {
            this.first = this.rb.length - 1;
        }
        if (this.last < 0) {
            this.last = this.rb.length - 1;
        }
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        this.checkCapacity(false);
        this.rb[last] = x;
        this.fillCount += 1;
        this.last += 1;
        this.wrapIndices();
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        this.checkCapacity(true);
        T x = rb[first];
        this.rb[first] = null;
        this.fillCount -= 1;
        this.first += 1;
        this.wrapIndices();
        return x;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        this.checkCapacity(true);
        return this.rb[first];
    }

    private class A implements Iterator<T> {
        private int location;

        A() {
            location = 0;
        }

        public boolean hasNext() {
            return (location < fillCount);
        }

        public T next() {
            T current = rb[location];
            location += 1;
            return current;
        }
    }

    public Iterator<T> iterator() {
        return new A();
    }
}
