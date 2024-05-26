package com.hotelhub.plugins.Reserve

import Quarto
import Quarto.Companion.getRoom
import com.hotelhub.plugins.User.DTO_Cliente
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.serialization.Serializable
import java.io.File
import kotlin.random.Random


@Serializable
class Reserva(var numero: String,
              var cliente: DTO_Cliente,
              var quarto: Quarto,
              var dataInicio: LocalDate,
              var dataFim: LocalDate,
              var checkIn: LocalDate?,
              var checkOut: LocalDate?,
              var preco: Double,
              var ativa: Boolean) {

    companion object {

        fun createReserve(data: DTO_ReserveData): Pair<Boolean, String>{

            val fileReserveDB = "reserva.txt"
            val fileReserve = File("db/$fileReserveDB")

            val startDate = LocalDate.parse(data.startDate)
            val endDate = LocalDate.parse(data.endDate)

            var error: Boolean = false

            for (reserve in fileReserve.readLines()){
                val reserveSplit = reserve.split("|");
                val reserveStartDate = LocalDate.parse(reserveSplit[3])
                val reserveEndDate = LocalDate.parse(reserveSplit[4])
                if (reserveSplit[1] == data.idClient &&
                    (((reserveStartDate >= startDate && reserveStartDate <= endDate) || (reserveEndDate >= startDate && reserveEndDate <= endDate))
                    || reserveStartDate <= startDate && reserveStartDate <= endDate && reserveEndDate >= startDate && reserveEndDate >= endDate)){
                    error = true
                }
            }

            if (error == false){
                val daysBetween = startDate.daysUntil(endDate)

                val number = generateCode(fileReserveDB)
                var price:Double = 0.0

                var roomResponse = getRoom(data.idRoom)

                if (roomResponse.first){

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
                }else{
                    return Pair(false, "Erro! Tente novamente mais tarde!")
                }
            }else{
                return Pair(false, "Erro! Reserva já existente na data selecionada!")
            }
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

    }
}