#include <jni.h>
#include <string>

int Fibonacci(int x) {
    if((x==1)||(x==0)) {
        return(x);
    }else {
        return(Fibonacci(x-1)+Fibonacci(x-2));
    }
}

extern "C"
JNIEXPORT jint JNICALL
Java_cytech_android_fibonacci_MainActivity_Fibonacci(JNIEnv *env, jobject thiz, jint i) {
    // TODO: implement Fibonacci()
    return Fibonacci(i);
}