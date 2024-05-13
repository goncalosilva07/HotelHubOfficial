import java.io.File
//import java.io.FileWriter

class Cliente (
    id: String,
    userName: String,
    password: String,
    nome: String,
    apelido: String,
    email: String,
    telefone: Int):Pessoa(id, userName, password,nome, apelido, email, telefone, isClient = true)
{

    override fun register() {

        val fileUser = "users.txt"
        val filePermissions = "permissoes_utilizador.txt"
        if ((userName != "") && (password != "") && (nome != "")
            && (apelido != "") && (email != "") && (telefone.toString().length >= 9)){

            generateCode("users.txt")

            val responseName = verifyUserName(userName)
            if(responseName.first == true) {
                val fileUser = File("db/$fileUser")
                fileUser.appendText("$id|$userName|$password|$nome|$apelido|$email|$telefone|$isClient\n")

                val filePermissions = File("db/$filePermissions")
                filePermissions.appendText("$id|001\n")

            }else
                println(responseName.second)
        }else
            println("Dados Inválidos!")
    }

}