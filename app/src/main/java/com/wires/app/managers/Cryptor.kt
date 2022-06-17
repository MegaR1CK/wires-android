package com.wires.app.managers

import java.security.MessageDigest
import javax.inject.Inject

class Cryptor @Inject constructor() {

    companion object {
        private const val SHA256_ALGORITHM_NAME = "SHA-256"
    }

    fun getSha256Hash(string: String): String {
        val messageDigest = MessageDigest.getInstance(SHA256_ALGORITHM_NAME)
        return messageDigest.digest(string.toByteArray(Charsets.UTF_8)).toHexString()
    }

    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
}
