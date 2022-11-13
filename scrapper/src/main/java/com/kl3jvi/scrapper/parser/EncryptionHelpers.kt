package com.kl3jvi.scrapper.parser

interface EncryptionHelpers {
    fun encryptAes(text: String, key: String, iv: String): String
    fun decryptAES(encrypted: String, key: String, iv: String): String
}