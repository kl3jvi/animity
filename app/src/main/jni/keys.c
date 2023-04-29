#include <jni.h>
#include <string.h>
#include <malloc.h>

#ifdef ANILIST_KEY
const char *key = ANILIST_KEY;
#else
#error "No anilist key found"
#endif

static const unsigned char anilist_id_ciphertext[] = {0x37, 0x33, 0x38, 0x31};

static const unsigned char anilist_secret_ciphertext[] = {0x47, 0x73, 0x30, 0x6F, 0x39, 0x55, 0x32,
                                                          0x71, 0x6F, 0x46, 0x53, 0x44, 0x67,
                                                          0x59, 0x34, 0x4A, 0x55, 0x4B, 0x63, 0x78,
                                                          0x45, 0x72, 0x54, 0x64, 0x65, 0x74,
                                                          0x72, 0x78, 0x4B, 0x70, 0x6F, 0x66, 0x4C,
                                                          0x66, 0x55, 0x44, 0x75, 0x66, 0x45,
                                                          0x4F};

static const unsigned char redirect_uri_ciphertext[] = {0x6C, 0x6F, 0x67, 0x69, 0x6E, 0x3A, 0x2F,
                                                        0x2F, 0x61, 0x6E, 0x69, 0x6D, 0x69,
                                                        0x74, 0x79};

jbyteArray xor_cipher(JNIEnv *env, const jbyte *input, int input_len, int key_len) {
    jbyteArray result = (*env)->NewByteArray(env, input_len);

    jbyte *buffer = (jbyte *) malloc(input_len * sizeof(jbyte));
    for (int i = 0; i < input_len; ++i) {
        buffer[i] = input[i] ^ key[i % key_len];
    }
    printf("Key: %s\n", key);

    (*env)->SetByteArrayRegion(env, result, 0, input_len, buffer);
    free(buffer);

    return result;
}

JNIEXPORT jbyteArray JNICALL
Java_com_kl3jvi_animity_data_secrets_Secrets_getAniListId(JNIEnv *env, jobject thiz) {
    int input_len = sizeof(anilist_id_ciphertext) / sizeof(anilist_id_ciphertext[0]);
    int key_len = strlen(key);
    return xor_cipher(env, (const jbyte *) anilist_id_ciphertext, input_len, key_len);
}

JNIEXPORT jbyteArray JNICALL
Java_com_kl3jvi_animity_data_secrets_Secrets_getAniListSecret(JNIEnv *env, jobject thiz) {
    int input_len = sizeof(anilist_secret_ciphertext) / sizeof(anilist_secret_ciphertext[0]);
    int key_len = strlen(key);
    return xor_cipher(env, (const jbyte *) anilist_secret_ciphertext, input_len, key_len);
}

JNIEXPORT jbyteArray JNICALL
Java_com_kl3jvi_animity_data_secrets_Secrets_getRedirectUri(JNIEnv *env, jobject thiz) {
    int input_len = sizeof(redirect_uri_ciphertext) / sizeof(redirect_uri_ciphertext[0]);
    int key_len = strlen(key);
    return xor_cipher(env, (const jbyte *) redirect_uri_ciphertext, input_len, key_len);
}

