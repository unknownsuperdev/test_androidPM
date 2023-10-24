package com.fiction.core.utils

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Encryption {

    private const val TRANSFORMATION = "AES/CBC/PKCS7PADDING"
    private const val ALGORITHM = "AES"
    private val CHAR_SET = charset("UTF-8")
    private const val SHA_256 = "SHA-256"
    private const val INT_VECTOR_LENGTH = 16
    private const val SECRET_KEY = 32
    private const val SALT = "7@8!BN4EW4^%HQVkg^eVNy!d"

    private fun generateKey(password: String): SecretKey {
        val key = sha256(password).toByteArray().copyOf(SECRET_KEY)
        return SecretKeySpec(key, ALGORITHM)
    }

    private fun generateIv(password: ByteArray): IvParameterSpec {
        return IvParameterSpec(password)
    }

    private fun decryptMsg(cipherText: ByteArray, secret: SecretKey, iv: IvParameterSpec): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secret, iv)
        return String(
            cipher.doFinal(cipherText),
            CHAR_SET
        )
    }

    private fun sha256(input: String): String {
        return hashString(input, SHA_256)
    }

    private fun hashString(input: String, algorithm: String): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun decryptAES(key: String, encrypted: String): String {
        val secretKey = generateKey(key)
        val base64 = Base64.decode(encrypted, Base64.DEFAULT)
        val iv = generateIv(base64.copyOf(INT_VECTOR_LENGTH))
        return decryptMsg(
            base64.copyOfRange(INT_VECTOR_LENGTH, base64.size),
            secretKey,
            iv
        )
    }

    fun decryptText(uuid: String, title: String?, text: String?, chapterId: Long?): String? {
        if (title.isNullOrEmpty() || text.isNullOrEmpty() || chapterId == null) return null

        val newUuid = if (title.toCharArray().size % 2 == 0
        ) uuid.reversed() else uuid

        return decryptAES(
            "$chapterId${sha256(title)}$SALT${newUuid}",
            text
        )
    }

}