/**
 * Created by anastasiav on 2/4/2017.
 */
public class Palindrome {
    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> a = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            char x = word.charAt(i);
            a.addLast(x);
        }
        return a;
    }

    public static boolean isPalindrome(String word) {
        return isPalindrome(wordToDeque(word));
    }

    private static boolean isPalindrome(Deque<Character> a) {
        if (a.size() < 2) {
            return true;
        }
        char first = a.removeFirst();
        char last = a.removeLast();
        if (first == last) {
            return isPalindrome(a);
        }
        return false;
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
        return isPalindrome(wordToDeque(word), cc);
    }

    private static boolean isPalindrome(Deque<Character> a, CharacterComparator cc) {
        if (a.size() < 2) {
            return true;
        }
        char first = a.removeFirst();
        char last = a.removeLast();
        if (cc.equalChars(first, last)) {
            return isPalindrome(a, cc);
        }
        return false;
    }
}
