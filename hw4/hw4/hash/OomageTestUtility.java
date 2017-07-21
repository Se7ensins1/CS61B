package hw4.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] bucket = new int[M];
        for (Oomage o : oomages) {
            int buckNum = (o.hashCode() & 0x7FFFFFFF) % M;
            bucket[buckNum] += 1;
        }
        int N = oomages.size();

        for (int i : bucket) {
            if (i < N / 50 || i > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
