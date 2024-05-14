import java.io.File
//import java.io.FileWriter

class Cliente (
    id: String,
    userName: String,
    password: String,
    nome: String?,
    apelido: String?,
    email: String?,
    telefone: Int?):Pessoa(id, userName, password,nome, apelido, email, telefone, isClient = true)
{

    override fun register(): Pair<Boolean, String> {

        val fileUser = "users.txt"
        val filePermissions = "permissoes_utilizador.txt"
        if ((userName != "") && (password != "")){

            generateCode("users.txt")

            val responseName = verifyUserName(userName)
            if(responseName.first == true) {
                val fileUser = File("db/$fileUser")
                fileUser.appendText("$id|$userName|$password|$nome|$apelido|$email|$telefone|$isClient\n")

                val filePermissions = File("db/$filePermissions")
                filePermissions.appendText("$id|001\n")

                return Pair(true, responseName.second)
            }else
                return Pair(false, responseName.second)
        }else
            return Pair(false, "Dados Inválidos!")

        return Pair(false, "Erro! Tente novamente mais tarde.")
    }

}