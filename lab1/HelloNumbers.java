public class HelloNumbers {
    public static void main(String[] args) {
        int x = 0;
        int i = 0;
        while (i < 10) {
        	x = x + i;
            System.out.print(x + " ");
            i += 1;
        }
        System.out.println();
    }
}