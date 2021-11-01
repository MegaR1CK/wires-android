package com.wires.app.domain

import java.security.MessageDigest
import javax.inject.Inject

class Cryptor @Inject constructor() {

    companion object {
        private const val MD5_ALGORITHM_NAME = "MD5"
    }

    fun getMd5Hash(str: String): String {
        val messageDigest = MessageDigest.getInstance(MD5_ALGORITHM_NAME)
        return messageDigest.digest(str.toByteArray(Charsets.UTF_8)).toHexString()
    }

    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
}
