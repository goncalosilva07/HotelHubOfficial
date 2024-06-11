package com.hotelhub.plugins.User

import Horario
import com.hotelhub.plugins.Dashboard.DTO_GetDashboardInitialData
import com.hotelhub.plugins.Dashboard.DTO_GetEmployeeDashboardInitialData
import com.hotelhub.plugins.Permission.Permissao
import com.hotelhub.plugins.Reserve.Reserva
import com.hotelhub.plugins.RoomService.DTO_GetRoomServiceDashboard
import com.hotelhub.plugins.RoomService.DTO_MenuRoomService
import com.hotelhub.plugins.Task.Task
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.io.File

class Employee (id: String,
                userName: String,
                password: String,
                nome: String,
                apelido: String,
                email: String,
                telefone: String,
                permissoes: MutableList<Permissao>,
                isClient: Boolean,
                var nif: String,
                var dataDeNascimento: LocalDate,
                var genero: String,
                var salario: Double,
                var cargo: String,
                var horario: MutableList<Horario>):
    Pessoa(id, userName, password, nome, apelido, email, telefone, permissoes, isClient)
{

    override fun register(): Pair<Boolean, String> {

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

        return Pair(false, "Erro")
    }

    companion object {

        fun createEmployee(employeeData: DTO_Employee): Pair<Boolean, String>{

            try {
                var erro: Boolean = false

                val employee = Employee("", employeeData.userName, employeeData.password, employeeData.nome!!,
                    employeeData.apelido!!, employeeData.email!!, employeeData.telefone!!, mutableListOf(), false, employeeData.nif,
                    employeeData.dataDeNascimento, employeeData.genero, employeeData.salario, employeeData.cargo, mutableListOf())

                employee.verifyUserName(employee.userName)
                employee.generateCode("users.txt")

                val fileUsersDB = "users.txt"
                val fileUsers = File("db/$fileUsersDB")

                fileUsers.appendText("${employee.id}|${employee.userName}|${employee.password}|${employee.nome}|${employee.apelido}|${employee.email}|${employee.telefone}|false\n")

                val fileEmployeeDB = "funcionarios.txt"
                val fileEmployee = File("db/$fileEmployeeDB")

                fileEmployee.appendText("${employee.id}|${employee.nif}|${employee.dataDeNascimento}|${employee.genero}|${employee.salario}|${employee.cargo}\n")

                return Pair(true, "Dados inseridos com sucesso!")

            }catch (e: Exception){
                return Pair(true, e.toString())
            }
        }

        fun getEmployeeDashBoardInitialData(idUser: String): DTO_GetEmployeeDashboardInitialData {

            val fileTasksDB = "tarefas.txt"
            val fileTasks = File("db/$fileTasksDB")

            var tasks: MutableList<Task> = mutableListOf()

            for (task in fileTasks.readLines()){
                val taskSplit = task.split("|")

                if (taskSplit[1] == idUser){
                    val newTask = Task(taskSplit[0], taskSplit[1], taskSplit[2], taskSplit[3], taskSplit[4].toLocalDateTime(), "", taskSplit[5].toBoolean())
                    tasks.add(newTask)
                }
            }

            return DTO_GetEmployeeDashboardInitialData(tasks)
        }
    }

}

