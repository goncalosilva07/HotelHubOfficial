package com.hotelhub.plugins.User

import com.hotelhub.plugins.Permission.Permissao
import com.hotelhub.plugins.Dashboard.DTO_GetDashboardInitialData
import com.hotelhub.plugins.Reserve.Reserva
import com.hotelhub.plugins.RoomService.DTO_GetRoomServiceDashboard
import com.hotelhub.plugins.RoomService.DTO_MenuRoomService
import java.io.File
import java.time.LocalDate as LocalDate1
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class Cliente (
    id: String,
    userName: String,
    password: String,
    nome: String?,
    apelido: String?,
    email: String?,
    telefone: String?,
    permissoes: MutableList<Permissao>,
    isClient: Boolean): Pessoa(id, userName, password, nome, apelido, email, telefone, permissoes, isClient)
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
                filePermissions.appendText("$id|002\n")

                return Pair(true, responseName.second)
            }else
                return Pair(false, responseName.second)
        }else
            return Pair(false, "Dados Inválidos!")

        return Pair(false, "Erro! Tente novamente mais tarde.")
    }

    companion object {

        fun getClientDashBoardInitialData(idUser: String): DTO_GetDashboardInitialData{
            val fileReserveDB = "reserva.txt"
            val fileReserve = File("db/$fileReserveDB")

            val fileMenuDB = "menu.txt"
            val fileMenu = File("db/$fileMenuDB")

            val menuList: MutableList<DTO_MenuRoomService> = mutableListOf()

            for (menu in fileMenu.readLines()){
                val menuSplit = menu.split("|");

                menuList.add(DTO_MenuRoomService(menuSplit[0].toInt(), menuSplit[1], menuSplit[2].toDouble()))
            }

            val reserveList: MutableList<Reserva> = mutableListOf()
            val roomServiceList: MutableList<DTO_GetRoomServiceDashboard> = mutableListOf()

            for (reserve in fileReserve.readLines()) {
                val reserveSplit = reserve.split("|");
                val current = LocalDate1.now()

                if (reserveSplit[1] == idUser){
                    reserveList.add(Reserva(reserveSplit[0], reserveSplit[1], reserveSplit[2].toInt(), LocalDate.parse(reserveSplit[3]), LocalDate.parse(reserveSplit[4]),
                        if (reserveSplit[6] == "null") null else (LocalDateTime.parse(reserveSplit[5])), if (reserveSplit[6] == "null") null else (LocalDateTime.parse(reserveSplit[6])),
                        reserveSplit[7].toDouble(), reserveSplit[8].toBoolean()))
                }

                if (reserveSplit[1] == idUser && LocalDate1.parse(reserveSplit[3]) <= current && LocalDate1.parse(reserveSplit[4]) >= current && reserveSplit[8].toBoolean()){

                    val idReserve: String = reserveSplit[0]
                    val fileRoomServiceDB = "servico_quartos.txt"
                    val fileRoomService = File("db/$fileRoomServiceDB")

                    for (roomService in fileRoomService.readLines()){
                        val roomServiceSplit = roomService.split("|");

                        if(roomServiceSplit[0] == idReserve){
                            val nameProduct = menuList.find { it.id == roomServiceSplit[1].toInt()}?.name
                            roomServiceList.add(DTO_GetRoomServiceDashboard(nameProduct!!, roomServiceSplit[2].toInt(), roomServiceSplit[3].toDouble()))
                        }
                    }
                }
            }

            val returnData = DTO_GetDashboardInitialData(reserveList, roomServiceList)
            return returnData
        }

    }

}