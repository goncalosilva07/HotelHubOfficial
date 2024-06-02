package com.hotelhub.plugins.User

import Permissao
import java.io.File
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import kotlinx.datetime.LocalDate

abstract class Pessoa(
    var id: String,
    var userName: String,
    var password: String,
    var nome: String?,
    var apelido: String?,
    var email: String?,
    var telefone: String?,
    var permissoes: MutableList<Permissao>,
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

                        val returnData: DTO_LoginData = DTO_LoginData(userSplit[0], userSplit[1], enc)

                        return Pair(true, returnData)
                    }
                }
            } else {
                return Pair(false, "Insira todos os dados!")
            }
            return Pair(false, "Dados Incorretos!")
        }

        fun getUserData(id: String, userName: String, enc: String): Pair<Boolean, Any>{
            val fileUserDB = "users.txt"
            val fileUser = File("db/$fileUserDB")

            for (userData in fileUser.readLines()) {
                val userSplit = userData.split("|");

                if (id == userSplit[0] && userName == userSplit[1]) {
                    if (userSplit[7].toBoolean() == true){
                        val permissionsList: MutableList<Permissao> = getUserPermissions(userSplit[0])

                        val client: Cliente = Cliente(userSplit[0], userSplit[1],userSplit[2],userSplit[3],userSplit[4],userSplit[5],userSplit[6],permissionsList, true)

                        val recognizeUser = recognizeUser(id, userName, enc)

                        if (recognizeUser.first == false){
                            return Pair(false, recognizeUser.second)
                        }

                        return Pair(true, client)
                    }else {
                        val permissionsList: MutableList<Permissao> = getUserPermissions(userSplit[0])
                        val fileEmployeeDB = "funcionarios.txt"
                        val fileEmployee = File("db/$fileEmployeeDB")

                        var employeeInfo: MutableList<String> = mutableListOf()
                        for (employeeData in fileEmployee.readLines()) {
                            val employeeSplit = employeeData.split("|");
                            if (employeeSplit[0] == userSplit[0]){
                                employeeInfo.addAll(employeeSplit)
                            }
                        }

                        val dataDeNascimento: LocalDate = LocalDate.parse(employeeInfo[2])
                        /*ADICIONAR HORARIO*/
                        val employee: Employee = Employee(userSplit[0],userSplit[1],userSplit[2],userSplit[3],userSplit[4],userSplit[5],userSplit[6],
                            permissionsList, false, employeeInfo[1], dataDeNascimento, employeeInfo[3], employeeInfo[4].toDouble(), employeeInfo[5], mutableListOf())

                        val recognizeUser = recognizeUser(id, userName, enc)

                        if (recognizeUser.first == false){
                            return Pair(false, recognizeUser.second)
                        }

                        return Pair(true, employee)
                    }
                }
            }
            return Pair(false, "Erro! Utilizador não encontrado.")
        }

        fun getUserPermissions(idUser: String): MutableList<Permissao>{
            val filePermissionsDB = "permissoes_utilizador.txt"
            val filePermissions = File("db/$filePermissionsDB")

            var permissionsList: MutableList<Permissao> = mutableListOf()

            for (permission in filePermissions.readLines()) {
                val permissionSplit = permission.split("|");
                if (permissionSplit[0] == idUser){
                    permissionsList.add(Permissao(permissionSplit[1].toInt(), "", "", ""))
                }
            }
            return permissionsList
        }

        fun recognizeUser(id:String, userName: String, enc: String): Pair<Boolean, String>{
            val message = "${id}_${userName}"
            val sha256Digest = MessageDigest.getInstance("SHA-256").digest(message.toByteArray())
            val encNew = sha256Digest.joinToString("") { "%02x".format(it) }

            if (enc != encNew){
                return Pair(false, "Erro! Utilizador não reconhecido.")
            }else{
                return Pair(true, "Ok")
            }
        }
    }

}