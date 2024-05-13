package com.hotelhub.plugins

import kotlinx.serialization.Serializable
import java.io.File
import java.security.MessageDigest

class LoginPage {
    fun login(userName: String, password: String): Pair<Boolean, Any> {

        val fileUser = "users.txt"

        if (userName != "" && password != "") {
            val fileUser = File("db/$fileUser")
            for (userData in fileUser.readLines()) {
                val userSplit = userData.split("|");
                if (userName == userSplit[1] && password == userSplit[2]) {

                    val message = "${userSplit[0]}_${userSplit[1]}"
                    val sha256Digest = MessageDigest.getInstance("SHA-256").digest(message.toByteArray())
                    val enc = sha256Digest.joinToString("") { "%02x".format(it) }

                    val returnData: LoginData = LoginData(userSplit[0], userSplit[1], enc)

                    return Pair(true, returnData)
                }
            }
        } else {
            return Pair(false, "Insira todos os dados!")
        }
        return Pair(false, "Dados Incorretos!")
    }
}