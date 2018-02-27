#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_timeapp_shawn_camera_preview_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
