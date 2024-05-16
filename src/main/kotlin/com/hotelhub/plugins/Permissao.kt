import kotlinx.serialization.Serializable
import java.io.File

@Serializable
class Permissao (var id: Int,
                 var nome: String,
                 var descricao: String,
                 var icon: String){


    companion object {
        fun getAllPermissions(): MutableList<Permissao>{
            val filePermissionsDB = "permissoes.txt"
            val filePermissions = File("db/$filePermissionsDB")

            var permissionsList: MutableList<Permissao> = mutableListOf()

            for (permission in filePermissions.readLines()) {
                val permissionSplit = permission.split("|");
                permissionsList.add(Permissao(permissionSplit[0].toInt(), permissionSplit[1], permissionSplit[2], permissionSplit[3]))
            }
            return permissionsList
        }
    }
}