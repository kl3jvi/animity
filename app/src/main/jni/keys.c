#include <jni.h>


JNIEXPORT jstring JNICALL
Java_com_kl3jvi_animity_data_secrets_Secrets_getAnilistId(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "7381");
}

JNIEXPORT jstring JNICALL
Java_com_kl3jvi_animity_data_secrets_Secrets_getAnilistSecret(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "Gs0o9U2qoFSDgY4JUKcxErTdetrxKpofLfUDufEO");
}

JNIEXPORT jstring JNICALL
Java_com_kl3jvi_animity_data_secrets_Secrets_getRedirectUri(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "login://animity");
}