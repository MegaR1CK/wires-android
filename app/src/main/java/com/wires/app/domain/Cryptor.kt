package com.wires.app.domain

import java.security.MessageDigest
import javax.inject.Inject

class Cryptor @Inject constructor() {

    companion object {
        private const val SHA256_ALGORITHM_NAME = "SHA-256"
    }

    fun getSha256Hash(string: String, salt: String): String {
        val messageDigest = MessageDigest.getInstance(SHA256_ALGORITHM_NAME)
        val saltedString = string + salt
        return messageDigest.digest(saltedString.toByteArray(Charsets.UTF_8)).toHexString()
    }

    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
}
