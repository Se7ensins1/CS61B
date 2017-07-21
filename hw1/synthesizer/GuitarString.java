package synthesizer;

//Make sure this class is public
public class GuitarString {
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        this.buffer  = new ArrayRingBuffer<>(Math.round((int) (SR / frequency)));
        for (int i = 0; i < this.buffer.capacity(); i++) {
            this.buffer.enqueue(0.0);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        for (int i = 0; i < this.buffer.capacity(); i++) {
            double r = Math.random() - 0.5;
            this.buffer.dequeue();
            this.buffer.enqueue(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double factor = DECAY * .5 * (this.buffer.dequeue() + this.buffer.peek());
        this.buffer.enqueue(factor);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return this.buffer.peek();
    }
}
