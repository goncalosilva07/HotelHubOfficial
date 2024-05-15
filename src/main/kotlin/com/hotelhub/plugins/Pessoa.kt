import com.hotelhub.plugins.LoginData
import java.io.File
import java.security.MessageDigest
import kotlin.random.Random

abstract class Pessoa (var id: String,
                       var userName: String,
                       var password: String,
                       var nome: String?,
                       var apelido: String?,
                       var email: String?,
                       var telefone: Int?,
                       var isClient: Boolean) {


    abstract fun register(): Pair<Boolean, String>

    fun generateCode(file: String){
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var code: String = ""
        var exists = false
        var i = 1

        val file = File("db/$file").bufferedReader()
        val allCodes = mutableListOf<String>()

        file.forEachLine {
            allCodes.add(it.split("|")[0])
        }

        while (i < 4) {
            if (code in allCodes) {
                i = 1
                code = ""
            }

            repeat(4) {
                val num = Random.nextInt(0, 25)
                code += alphabet[num]
                i++
            }
        }

        file.close()
        id = code
    }

    fun verifyUserName(userName: String): Pair<Boolean, String>{

        val file = "users.txt"
        var erro: Boolean = false
        val fileUser = File("db/$file").bufferedReader()

        for (line in fileUser.readLines()){
            if(line.split("|")[1] == userName){
                erro = true
                break
            }
        }

        if (erro)
            return false to "Nome de utilizador já em utilização!"
        else
            return true to "Ok"

    }

    companion object {
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

}