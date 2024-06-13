import com.hotelhub.plugins.DTO_SubmitRooms
import com.hotelhub.plugins.Reserve.DTO_DataSerchRoomsForReserve
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.io.File
@Serializable
class Quarto(var numero: Int,
             var preco: Double,
             var capacidade: Int,
             var caracteristicas: List<CaracteristicaQuarto>?,
             var sujo: Boolean,
             var estadoManutencao: Boolean,
             var disponibilidade: Boolean) {

    companion object {
        @Serializable
        data class DTO_ReserveInfo(var number: String, var idClient: String, var idRoom: Int, var startDate: LocalDate, var endDate: LocalDate, var isActive: Boolean)

        fun getAvailableRooms(data: DTO_DataSerchRoomsForReserve): MutableList<Quarto>{

            val fileRoomDB = "quartos.txt"
            val fileRoom = File("db/$fileRoomDB")

            var roomsList: MutableList<Quarto> = mutableListOf()

            val fileRoomFeaturesListDB = "caracteristicas_quarto.txt"
            val fileRoomFeaturesList = File("db/$fileRoomFeaturesListDB")

            var featuresList: MutableList<CaracteristicaQuarto> = mutableListOf()

            for (feature in fileRoomFeaturesList.readLines()) {
                val featureSplit = feature.split("|");
                featuresList.add(CaracteristicaQuarto(featureSplit[0].toInt(), featureSplit[1]))
            }

            val startDate = LocalDate.parse(data.startDate)
            val endDate = LocalDate.parse(data.endDate)

            val fileReservesDB = "reserva.txt"
            val fileReserves = File("db/$fileReservesDB")

            var reserveList: MutableList<DTO_ReserveInfo> = mutableListOf()

            for (reserve in fileReserves.readLines()) {
                val reserveSplit = reserve.split("|");
                reserveList.add(DTO_ReserveInfo(reserveSplit[0], reserveSplit[1], reserveSplit[2].toInt(), LocalDate.parse(reserveSplit[3]), LocalDate.parse(reserveSplit[4]), reserveSplit[8].toBoolean()))
            }

            for (room in fileRoom.readLines()) {
                val roomSplit = room.split("|");

                val reserveForRoom = reserveList.find { it.idRoom == roomSplit[0].toInt() && it.isActive && (it.startDate <= startDate && it.endDate >= startDate)}

                if (roomSplit[5].toBoolean() && roomSplit[4].toBoolean() && roomSplit[2].toInt() >= data.numberOfPeople && reserveForRoom == null){
                    var counter: Int = 0
                    var roomFeaturesList: MutableList<CaracteristicaQuarto> = mutableListOf()

                    val fileRoomFeaturesDB = "quarto_caracteristicas.txt"
                    val fileRoomFeatures = File("db/$fileRoomFeaturesDB")

                    for (roomFeature in fileRoomFeatures.readLines()) {
                        val roomFeatureSplit = roomFeature.split("|");

                        if (roomFeatureSplit[0].toInt() == roomSplit[0].toInt()){
                            val service = data.additionalServices.find { it.id == roomFeatureSplit[1].toInt()}
                            val feature = featuresList.find { it.id == roomFeatureSplit[1].toInt() }

                            //roomFeaturesList.add(CaracteristicaQuarto(roomFeatureSplit[0].toInt(), roomFeatureSplit[1]))

                            if (feature != null) {
                                roomFeaturesList.add(CaracteristicaQuarto(feature.id, feature.nome))
                            }

                            if (service != null){
                                counter++
                            }
                        }


                    }

                    if (counter == data.additionalServices.size){
                        roomsList.add(Quarto(roomSplit[0].toInt(), roomSplit[1].toDouble(), roomSplit[2].toInt(), roomFeaturesList,
                            roomSplit[3].toBoolean(), roomSplit[4].toBoolean(), roomSplit[5].toBoolean()))
                    }
                }
            }
            return roomsList
        }

        fun getRoom(idRoom: Int): Pair<Boolean, Any>{

            val fileRoomDB = "quartos.txt"
            val fileRoom = File("db/$fileRoomDB")

            val fileRoomFeaturesListDB = "caracteristicas_quarto.txt"
            val fileRoomFeaturesList = File("db/$fileRoomFeaturesListDB")

            var featuresList: MutableList<CaracteristicaQuarto> = mutableListOf()

            for (feature in fileRoomFeaturesList.readLines()) {
                val featureSplit = feature.split("|");
                featuresList.add(CaracteristicaQuarto(featureSplit[0].toInt(), featureSplit[1]))
            }


            for (room in fileRoom.readLines()) {
                val roomSplit = room.split("|");
                if (roomSplit[0].toInt() == idRoom){

                    var roomFeaturesList: MutableList<CaracteristicaQuarto> = mutableListOf()

                    val fileRoomFeaturesDB = "quarto_caracteristicas.txt"
                    val fileRoomFeatures = File("db/$fileRoomFeaturesDB")

                    for (roomFeature in fileRoomFeatures.readLines()) {

                        val roomFeatureSplit = roomFeature.split("|");

                        if (roomFeatureSplit[0].toInt() == roomSplit[0].toInt()){
                            val feature = featuresList.find { it.id == roomFeatureSplit[1].toInt() }

                            if (feature != null) {
                                roomFeaturesList.add(CaracteristicaQuarto(feature.id, feature.nome))
                            }
                        }
                    }
                    var room = Quarto(roomSplit[0].toInt(), roomSplit[1].toDouble(), roomSplit[2].toInt(), roomFeaturesList, roomSplit[3].toBoolean(), roomSplit[4].toBoolean(), roomSplit[5].toBoolean())
                    return Pair(true, room)
                }
            }
            return Pair(false ,"Quarto não encontrado")
        }

        fun getDirtyRooms(): MutableList<Quarto>{

            val fileRoomDB = "quartos.txt"
            val fileRoom = File("db/$fileRoomDB")

            var roomList: MutableList<Quarto> = mutableListOf()

            for (room in fileRoom.readLines()){
                val roomSplit = room.split("|")

                if (roomSplit[3].toBoolean() == true){
                    roomList.add(Quarto(roomSplit[0].toInt(),roomSplit[1].toDouble(),roomSplit[2].toInt(), mutableListOf(),roomSplit[3].toBoolean(),roomSplit[4].toBoolean(),roomSplit[5].toBoolean()))
                }
            }

            return roomList
        }

        fun getRooms(): Pair<Boolean, MutableList<Quarto>>{

            try {
                val fileRoomDB = "quartos.txt"
                val fileRoom = File("db/$fileRoomDB")

                var roomList: MutableList<Quarto> = mutableListOf()

                for (room in fileRoom.readLines()){
                    val roomSplit = room.split("|")

                    roomList.add(Quarto(roomSplit[0].toInt(), roomSplit[1].toDouble(), roomSplit[2].toInt(), mutableListOf(),
                        roomSplit[3].toBoolean(), roomSplit[4].toBoolean(), roomSplit[5].toBoolean()))
                }

                return Pair(true, roomList)
            }catch (e: Exception){
                return Pair(false, mutableListOf())
            }
        }

        fun updateRooms(data: DTO_SubmitRooms): Pair<Boolean, String>{

            try {
                val fileRoomDB = "quartos.txt"
                val fileRoom = File("db/$fileRoomDB")

                fileRoom.writeText("")

                for (room in data.roomList){
                    fileRoom.appendText("${room.numero}|${room.preco}|${room.capacidade}|${room.sujo}|${room.estadoManutencao}|${room.disponibilidade}\n")
                }

                return Pair(true, "Dados inseridos com sucesso!")

            }catch (e: Exception){
                return Pair(false, "Erro ao inserir os dados!")
            }
        }
    }

}