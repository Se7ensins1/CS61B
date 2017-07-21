import lab14lib.Generator;

/**
 * Created by anastasiav on 4/27/2017.
 */
public class AcceleratingSawToothGenerator implements Generator{
    private int state;
    private double speed;
    private int period;

    public AcceleratingSawToothGenerator(int period, double speed) {
        this.period = period;
        this.speed = speed;
        this.state = 0;
    }

    @Override
    public double next() {
        this.state = (this.state + 1) % this.period;
        if (this.state == 0) {
            this.period = (int) Math.round(this.period * this.speed);
        }
        return normalize(this.state);
    }

    public double normalize(int num) {
        double range = num / ((this.period - 1) / 2.0);
        return range - 1;
    }
}
