import java.io.File
import java.util.*

class Employee (var nif: String,
                var dataDeNascimento: Date,
                var genero: String,
                var salario: Double,
                var cargo: String,
                var horario: List<Horario>,
                var permissoes: List<Permissao>,
                id: String,
                userName: String,
                password: String,
                nome: String,
                apelido: String,
                email: String,
                telefone: Int):Pessoa(id, userName, password, nome, apelido, email, telefone, isClient = false)
{

    override fun register() {

        val fileUser = "users.txt"
        val fileEmployee = "funcionarios.txt"

        if (userName != "" && password != "" && nome != ""
            && apelido != "" && email != "" && (telefone.toString().length >= 9)
            && cargo != "" && salario != 0.0 && genero != "" && (nif != "" && nif.length == 9)){

            generateCode("users.txt")

            val responseName = verifyUserName(userName)
            if(responseName.first == true) {

                val fileUser = File("db/$fileUser")
                fileUser.appendText("$id|$userName|$password|$nome|$apelido|$email|$telefone|$isClient\n")

                val fileEmployee = File("db/$fileEmployee")
                fileEmployee.appendText("$id|$nif|$dataDeNascimento|$genero|$salario|$cargo\n")
            }else
                println(responseName.second)
        }else
            println("Dados Inválidos!")
    }

}

/*
class Employee (var nome: String, var nif: Int,
                         var dataDeNascimento: Date, var genero: String, var salario: Double,
                         var email: String, var telefone: Int, var cargo: String){



}
 */