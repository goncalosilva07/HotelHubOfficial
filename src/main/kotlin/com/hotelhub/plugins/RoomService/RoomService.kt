package com.hotelhub.plugins.RoomService

import com.hotelhub.plugins.Reserve.DTO_ReserveData
import com.hotelhub.plugins.Reserve.Reserva
import kotlinx.datetime.daysUntil

//import Reserva
//import kotlinx.datetime.LocalDate
import java.io.File
import java.time.LocalDate

class RoomService(val id: String,
                  val reserve: Reserva,
                  val item: MutableList<DTO_MenuRoomServiceUser>,
                  val isActive: Boolean) {

    companion object {

        fun getRoomServiceInitialData(idUser: String): Pair<Boolean, Any>{

            val fileReserveDB = "reserva.txt"
            val fileReserve = File("db/$fileReserveDB")

            for (reserve in fileReserve.readLines()) {
                val reserveSplit = reserve.split("|");
                val menuList: MutableList<DTO_MenuRoomService> = mutableListOf()
                val current = LocalDate.now()

                if (reserveSplit[1] == idUser && LocalDate.parse(reserveSplit[3]) <= current && LocalDate.parse(reserveSplit[4]) >= current && reserveSplit[8].toBoolean()){
                    val idReserve: String = reserveSplit[0]
                    val fileMenuDB = "menu.txt"
                    val fileMenu = File("db/$fileMenuDB")

                    for (item in fileMenu.readLines()) {
                        val itemSplit = item.split("|");

                        menuList.add(DTO_MenuRoomService(itemSplit[0].toInt(), itemSplit[1], itemSplit[2].toDouble()))
                    }
                    return Pair(true, DTO_GetRoomServiceInitialData(idReserve, menuList))
                }
            }
            return Pair(false, "Erro! Utilizador Sem Reserva.")
        }

        fun createRoomService(data: DTO_CreateRoomService): Pair<Boolean, String>{

            val fileRoomServiceDB = "servico_quartos.txt"
            val fileRoomService = File("db/$fileRoomServiceDB")

            val fileMenuDB = "menu.txt"
            val fileMenu = File("db/$fileMenuDB")

            val menuList: MutableList<DTO_MenuRoomService> = mutableListOf()

            for (item in fileMenu.readLines()) {
                val itemSplit = item.split("|");

                menuList.add(DTO_MenuRoomService(itemSplit[0].toInt(), itemSplit[1], itemSplit[2].toDouble()))
            }

            var wroteFile: Boolean = false

            for (product in data.products){

                val productInfo = menuList.find { it.id == product.idProduct }
                val totalPrice = productInfo!!.price * product.quantity

                fileRoomService.appendText("${data.idReserve}|${product.idProduct}|${product.quantity}|$totalPrice\n")
                wroteFile = true
            }

            if (wroteFile){
                return Pair(true, "Pedido efetuado com sucesso!")
            }else{
                return Pair(false, "Erro ao efetuar o pedido!")
            }
        }

    }
}