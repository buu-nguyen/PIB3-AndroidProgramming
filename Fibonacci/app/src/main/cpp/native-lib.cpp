#include <jni.h>
#include <string>

long Fibonacci(int x) {
    if((x==1)||(x==0)) {
        return(x);
    }else {
        return(Fibonacci(x-1)+Fibonacci(x-2));
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_cytech_android_fibonacci_MainActivity_JNIFibonacci
(JNIEnv *env, jobject thiz, jint i) {
    // TODO: implement Fibonacci()
    return Fibonacci(i);
}