import java.io.File
import kotlin.random.Random

abstract class Pessoa (var id: String,
                       var userName: String,
                       var password: String,
                       var nome: String,
                       var apelido: String,
                       var email: String,
                       var telefone: Int,
                       var isClient: Boolean) {


    abstract fun register()

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
            return false to "Nome em utilização!"
        else
            return true to "Ok"

    }

}