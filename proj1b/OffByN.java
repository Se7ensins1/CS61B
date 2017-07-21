/**
 * Created by anastasiav on 2/5/2017.
 */
public class OffByN implements CharacterComparator {

    private int N;

    public OffByN(int N) {
        this.N = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        if (y - x == this.N || x - y == this.N) {
            return true;
        }
        return false;
    }
}
