import lab14lib.Generator;

/**
 * Created by anastasiav on 4/27/2017.
 */
public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    @Override
    public double next() {
        this.state = (this.state + 1) % this.period;
        return normalize(this.state);
    }

    public double normalize(int num) {
        double range = num / ((this.period - 1) / 2.0);
        return range - 1;
    }
}
