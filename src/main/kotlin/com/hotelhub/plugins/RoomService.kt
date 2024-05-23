package com.hotelhub.plugins

import Reserva
//import kotlinx.datetime.LocalDate
import java.io.File
import java.time.LocalDate

class RoomService(val id: String,
                  val reserve: Reserva,
                  val item: MutableList<MenuRoomServiceUser>,
                  val isActive: Boolean) {

    companion object {

        fun getRoomServiceInitialData(idUser: String): Pair<Boolean, Any>{

            val fileReserveDB = "reserva.txt"
            val fileReserve = File("db/$fileReserveDB")

            for (reserve in fileReserve.readLines()) {
                val reserveSplit = reserve.split("|");
                val menuList: MutableList<MenuRoomService> = mutableListOf()
                val current = LocalDate.now()

                if (reserveSplit[1] == idUser && LocalDate.parse(reserveSplit[3]) <= current && LocalDate.parse(reserveSplit[4]) >= current && reserveSplit[8].toBoolean()){
                    val idReserve: String = reserveSplit[0]
                    val fileMenuDB = "menu.txt"
                    val fileMenu = File("db/$fileMenuDB")

                    for (item in fileMenu.readLines()) {
                        val itemSplit = item.split("|");

                        menuList.add(MenuRoomService(itemSplit[0].toInt(), itemSplit[1], itemSplit[2].toDouble()))
                    }
                    return Pair(true, DTO_GetRoomServiceInitialData(idReserve, menuList))
                }
            }
            return Pair(false, "Erro! Utilizador Sem Reserva.")
        }


    }
}