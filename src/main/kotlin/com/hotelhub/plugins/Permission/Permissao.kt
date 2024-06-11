package com.hotelhub.plugins.Permission

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
class Permissao (var id: Int,
                 var nome: String?,
                 var descricao: String?,
                 var icon: String?){


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

        fun updateUserPermissions(data: DTO_UserPermissions): Pair<Boolean, String> {

            var idUser: String = ""
            var userPermissionsTemp: MutableList<Permissao> = mutableListOf()
            userPermissionsTemp = data.permissions

            val fileUserDB = "users.txt"
            val fileUser = File("db/$fileUserDB")

            for (user in fileUser.readLines()){
                val userSplit = user.split("|")

                if (userSplit[1] == data.userName){
                    idUser = userSplit[0]
                    break;
                }
            }

            if (idUser != ""){
                var usersPermissionsList: MutableList<String> = mutableListOf()
                val filePermissionsUtilizadorDB = "permissoes_utilizador.txt"
                val filePermissionsUtilizador = File("db/$filePermissionsUtilizadorDB")

                for (permission in filePermissionsUtilizador.readLines()){
                    val permissionSplit = permission.split("|")

                    if (permissionSplit[0] == idUser){

                        if (data.permissions.find { it.id == permissionSplit[1].toInt() } != null){
                            usersPermissionsList.add(permission)
                            var temp: MutableList<Permissao> = mutableListOf()

                            for (permissionTemp in userPermissionsTemp){
                                if (permissionTemp.id != permissionSplit[1].toInt()){
                                    temp.add(permissionTemp)
                                }
                            }
                            userPermissionsTemp = temp
                        }

                    }else{
                        usersPermissionsList.add(permission)
                    }
                }

                filePermissionsUtilizador.writeText("")

                if (usersPermissionsList != null) {
                    for (permission in usersPermissionsList){
                        val permissionSplit = permission.split("|")
                        filePermissionsUtilizador.appendText("${permissionSplit[0]}|${permissionSplit[1]}\n")
                    }
                }

                if (userPermissionsTemp != null) {
                    for (permission in userPermissionsTemp){
                        filePermissionsUtilizador.appendText("$idUser|${permission.id}\n")
                    }
                }
                return Pair(true, "Ok")
            }
            return Pair(false, "Erro! Utilizador não encontrado.")
        }
    }
}