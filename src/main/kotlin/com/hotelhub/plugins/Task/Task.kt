package com.hotelhub.plugins.Task

import com.hotelhub.plugins.Permission.Permissao
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.io.File
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime as JavaLocalDateTime
import kotlin.random.Random

@Serializable
class Task (var id: String,
            val user: String,
            val title: String,
            val description: String,
            val dateTime: LocalDateTime?,
            val dateTimeString: String?,
            val isConcluded: Boolean) {


    fun generateCode(){
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var code: String = ""
        var exists = false
        var i = 1

        val file = File("db/tarefas.txt").bufferedReader()
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


    companion object {

        fun getUserTasksByUserName(userName: String): Pair<Boolean, MutableList<Task>> {

            var idUser: String = ""

            val fileUserDB = "users.txt"
            val fileUser = File("db/$fileUserDB")

            for (user in fileUser.readLines()){
                val userSplit = user.split("|")

                if (userSplit[1] == userName){
                    idUser = userSplit[0]
                    break;
                }
            }

            if (idUser != "") {
                val fileTasksDB = "tarefas.txt"
                val fileTasks = File("db/$fileTasksDB")

                var tasksList: MutableList<Task> = mutableListOf()

                for (task in fileTasks.readLines()) {
                    val taskSplit = task.split("|");
                    if (taskSplit[1] == idUser) {
                        tasksList.add(Task(taskSplit[0], taskSplit[1], taskSplit[2], taskSplit[3], taskSplit[4].toLocalDateTime(), null, taskSplit[5].toBoolean()))
                    }
                }
                return Pair(true, tasksList)
            }
            return Pair(false, mutableListOf())

        }

        fun updateUserTasks(data: DTO_UserTasks): Pair<Boolean, String> {

            var idUser: String = ""

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

                var tasksToAdd = data.tasks.filter { it.id == "" }
                var tasksToUpdate: MutableList<Task> = data.tasks.filter { it.id != "" }.toMutableList()

                val fileUserTasksDB = "tarefas.txt"
                val fileUserTasks = File("db/$fileUserTasksDB")

                for (task in fileUserTasks.readLines()){
                    val taskSplit = task.split("|")

                    if (taskSplit[1] == idUser){
                        if (tasksToUpdate.find { it.id == taskSplit[0] } == null){
                            tasksToUpdate = tasksToUpdate.filter { it.id != taskSplit[0]}.toMutableList()
                        }
                    }else{
                        tasksToUpdate.add(Task(taskSplit[0], taskSplit[1], taskSplit[2], taskSplit[3], taskSplit[4].toLocalDateTime(), null, taskSplit[5].toBoolean()))
                    }
                }

                fileUserTasks.writeText("")

                if (tasksToUpdate != null) {
                    for (task in tasksToUpdate){
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                        var dateTime: JavaLocalDateTime
                        if (task.user == idUser){
                            //dateTime = JavaLocalDateTime.parse(task.dateTimeString!!, formatter)
                            dateTime = JavaLocalDateTime.parse(task.dateTimeString!!)
                        }else{
                            dateTime = task.dateTime!!.toJavaLocalDateTime()
                        }

                        fileUserTasks.appendText("${task.id}|${task.user}|${task.title}|${task.description}|$dateTime|${task.isConcluded}\n")
                    }
                }

                if (tasksToAdd != null) {
                    for (task in tasksToAdd){
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                        val dateTime = JavaLocalDateTime.parse(task.dateTimeString!!, formatter)

                        var taskObj = Task("", idUser, task.title, task.description, dateTime.toKotlinLocalDateTime(), null,false)
                        taskObj.generateCode()
                        fileUserTasks.appendText("${taskObj.id}|${taskObj.user}|${taskObj.title}|${taskObj.description}|${taskObj.dateTime}|${taskObj.isConcluded}\n")
                    }
                }

                return Pair(true, "Ok")
            }
            return Pair(false, "Erro! Utilizador não encontrado.")
        }

    }


}