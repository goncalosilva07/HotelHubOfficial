package com.hotelhub.plugins.Reserve

import Quarto
import Quarto.Companion.getRoom
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.daysUntil
import kotlinx.serialization.Serializable
import java.io.File
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import java.time.LocalDateTime as javaLocalDateTime


@Serializable
class Reserva(
    var numero: String,
    var idCliente: String,
    var idQuarto: Int,
    var dataInicio: LocalDate,
    var dataFim: LocalDate,
    var checkIn: LocalDateTime?,
    var checkOut: LocalDateTime?,
    var preco: Double,
    var ativa: Boolean) {

    companion object {

        fun createReserve(data: DTO_ReserveData): Pair<Boolean, String>{

            if (data.clientUserName != null && data.clientUserName != ""){
                val fileUsersDB = "users.txt"
                val fileUsers = File("db/$fileUsersDB")

                for (user in fileUsers.readLines()){
                    val userSplit = user.split("|")
                    if (userSplit[1] == data.clientUserName)
                        data.idClient = userSplit[0]
                }
            }

            if (data.idClient != null && data.idClient != "") {

                val fileReserveDB = "reserva.txt"
                val fileReserve = File("db/$fileReserveDB")

                val startDate = LocalDate.parse(data.startDate)
                val endDate = LocalDate.parse(data.endDate)

                var error: Boolean = false

                for (reserve in fileReserve.readLines()) {
                    val reserveSplit = reserve.split("|");
                    val reserveStartDate = LocalDate.parse(reserveSplit[3])
                    val reserveEndDate = LocalDate.parse(reserveSplit[4])
                    if (reserveSplit[1] == data.idClient &&
                        (((reserveStartDate >= startDate && reserveStartDate <= endDate) || (reserveEndDate >= startDate && reserveEndDate <= endDate))
                                || reserveStartDate <= startDate && reserveStartDate <= endDate && reserveEndDate >= startDate && reserveEndDate >= endDate)
                    ) {
                        error = true
                    }
                }

                if (error == false) {
                    val daysBetween = startDate.daysUntil(endDate)

                    val number = generateCode(fileReserveDB)
                    var price: Double = 0.0

                    var roomResponse = getRoom(data.idRoom)

                    if (roomResponse.first) {

                        val room: Quarto = Quarto(
                            (roomResponse.second as Quarto).numero,
                            (roomResponse.second as Quarto).preco,
                            (roomResponse.second as Quarto).capacidade,
                            (roomResponse.second as Quarto).caracteristicas,
                            (roomResponse.second as Quarto).estaLimpeza,
                            (roomResponse.second as Quarto).estadoManutencao,
                            (roomResponse.second as Quarto).disponibilidade,
                        )

                        price = room.preco * daysBetween
                        fileReserve.appendText("$number|${data.idClient}|${data.idRoom}|$startDate|$endDate|null|null|$price|true\n")
                        return Pair(true, "Reserva criada com sucesso!")
                    } else {
                        return Pair(false, "Erro! Tente novamente mais tarde!")
                    }
                } else {
                    return Pair(false, "Erro! Reserva já existente na data selecionada!")
                }
            }
            return Pair(false, "Erro! Sem cliente para associar à reserva!")
        }

        fun generateCode(file: String): String{
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

            return code
        }

        fun updateCheckInAndCheckOut(data: DTO_CheckInOrCheckOut): Pair<Boolean, String>{

            try {
                var reserves: MutableList<String> = mutableListOf()

                val fileReserveDB = "reserva.txt"
                val fileReserve = File("db/$fileReserveDB")

                var error = false
                var msgError = ""

                for (reserve in fileReserve.readLines()){
                    val reserveSplit = reserve.split("|").toMutableList()

                    if (reserveSplit[0] == data.idReserve){
                        if (data.action == 1){
                            val date = LocalDateTime.parse(reserveSplit[3] + "T00:00:00")
                            val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            if (todayDate >= date){
                                reserveSplit[5] = todayDate.toString()
                            }else{
                                error = true
                                msgError = "Tentativa de check-in! A data da reserva é maior que a data de check-in."
                            }
                        }else if (data.action == 2){
                            val date = LocalDateTime.parse(reserveSplit[4] + "T00:00:00")
                            val date2 = LocalDateTime.parse(reserveSplit[3] + "T00:00:00")
                            val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            if (todayDate <= date && todayDate >= date2){
                                reserveSplit[6] = todayDate.toString()
                            }else if(todayDate < date2){
                                error = true
                                msgError = "Tentativa de check-out! A data de inicio da reserva é maior que a data de check-out."
                            }else
                            {
                                reserveSplit[6] = todayDate.toString()
                                error = true
                                msgError = "Tentativa de check-out! A data da reserva é menor que a data de check-out."
                            }
                        }
                        reserves.add("${reserveSplit[0]}|${reserveSplit[1]}|${reserveSplit[2]}|${reserveSplit[3]}|${reserveSplit[4]}|${reserveSplit[5]}|${reserveSplit[6]}|${reserveSplit[7]}|${reserveSplit[8]}")
                    }else{
                        reserves.add(reserve)
                    }
                }

                fileReserve.writeText("")

                for (reserve in reserves){
                    var reserveSplit = reserve.split("|")
                    fileReserve.appendText("${reserveSplit[0]}|${reserveSplit[1]}|${reserveSplit[2]}|${reserveSplit[3]}|${reserveSplit[4]}|${reserveSplit[5]}|${reserveSplit[6]}|${reserveSplit[7]}|${reserveSplit[8]}\n")
                }

                if (error == true){
                    return Pair(false, msgError)
                }else{
                    return Pair(true, if (data.action == 1) "Check-in efetuado com sucesso!" else "Check-out efetuado com sucesso!")
                }

            }catch (e: Exception){
                return  Pair(false, "Erro! Tente novamente mais tarde.")
            }
        }

    }
}