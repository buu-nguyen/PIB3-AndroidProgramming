package cytech.android.fibonacci

fun Fibonacci(i: Int): Int {
    return if (i == 0 || i == 1) i else Fibonacci(i - 1) + Fibonacci(i - 2)
}