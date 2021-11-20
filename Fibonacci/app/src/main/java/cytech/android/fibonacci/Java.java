package cytech.android.fibonacci;

public class Java {
    public static long Fibonacci(int i) {
        if ((i == 0) || (i == 1))
            return i;
        else
            return Fibonacci(i - 1) + Fibonacci(i - 2);
    }
}
