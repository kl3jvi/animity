LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := keys
LOCAL_SRC_FILES := keys.c
 LOCAL_CFLAGS += -DANILIST_KEY='"4b!P@ssW0rd^"'

include $(BUILD_SHARED_LIBRARY)
