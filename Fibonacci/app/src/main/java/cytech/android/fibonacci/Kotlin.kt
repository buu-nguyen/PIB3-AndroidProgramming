package cytech.android.fibonacci

fun Fibonacci(i: Int): Long {
    return if (i == 0 || i == 1) i.toLong() else Fibonacci(i - 1) + Fibonacci(i - 2)
}