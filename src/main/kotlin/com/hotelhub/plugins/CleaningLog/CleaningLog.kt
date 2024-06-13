package com.hotelhub.plugins.CleaningLog

import Quarto
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
class CleaningLog (val userName: String,
                   val idRoom: Int){

    companion object {

        fun submitCleaningLog(data: DTO_SubmitCleaningLog): Pair<Boolean, String>{
            try {

                val responseData = Quarto.Companion.getRoom(data.idRoom)

                if (responseData.first){
                    val fileCleaningLogDB = "registo_de_limpeza.txt"
                    val fileCleaningLog = File("db/$fileCleaningLogDB")

                    val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

                    fileCleaningLog.appendText("${data.idRoom}|${data.idUser}|$todayDate")

                    val fileRoomDB = "quartos.txt"
                    val fileRoom = File("db/$fileRoomDB")

                    var roomsList: MutableList<Quarto> = mutableListOf()

                    for (room in fileRoom.readLines()){
                        val roomSplit = room.split("|")

                        if (roomSplit[0].toInt() == data.idRoom){
                            roomsList.add(Quarto(roomSplit[0].toInt(), roomSplit[1].toDouble(), roomSplit[2].toInt(), mutableListOf(), false, roomSplit[4].toBoolean(), roomSplit[5].toBoolean()))
                        }else{
                            roomsList.add(Quarto(roomSplit[0].toInt(), roomSplit[1].toDouble(), roomSplit[2].toInt(), mutableListOf(), roomSplit[3].toBoolean(), roomSplit[4].toBoolean(), roomSplit[5].toBoolean()))
                        }
                    }

                    fileRoom.writeText("")

                    for (room in roomsList){
                        fileRoom.appendText("${room.numero}|${room.preco}|${room.capacidade}|${room.sujo}|${room.estadoManutencao}|${room.disponibilidade}\n")
                    }

                    return Pair(true, "Registo de Limpeza inserido com sucesso!")
                }else{
                    return Pair(false, responseData.second.toString())
                }

            }catch (e: Exception){
                return Pair(false, e.toString())
            }
        }
    }
}