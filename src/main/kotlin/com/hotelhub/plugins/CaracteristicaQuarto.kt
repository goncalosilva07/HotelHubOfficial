import Quarto.Companion.getRoom
import com.hotelhub.plugins.Permission.DTO_UserPermissions
import com.hotelhub.plugins.Permission.Permissao
import com.hotelhub.plugins.RoomCharacteristics.DTO_RoomCharacteristics
import com.hotelhub.plugins.RoomCharacteristics.DTO_RoomCharacteristicsInitialData
import com.hotelhub.plugins.RoomCharacteristics.DTO_SubmitRoomCharacteristics
import kotlinx.serialization.Serializable
import java.io.File
@Serializable
class CaracteristicaQuarto (var id: Int, var nome: String) {

    companion object{
        fun getRoomFeatures(): MutableList<CaracteristicaQuarto>{
            val fileRoomFeaturesDB = "caracteristicas_quarto.txt"
            val fileRoomFeatures = File("db/$fileRoomFeaturesDB")

            var roomFeaturesList: MutableList<CaracteristicaQuarto> = mutableListOf()

            for (roomFeature in fileRoomFeatures.readLines()) {
                val roomFeatureSplit = roomFeature.split("|");
                roomFeaturesList.add(CaracteristicaQuarto(roomFeatureSplit[0].toInt(), roomFeatureSplit[1]))
            }
            return roomFeaturesList
        }

        fun getRoomCharacteristicsByRoomNumber(roomNumber: Int): Pair<Boolean, DTO_RoomCharacteristicsInitialData>{

            val room = getRoom(roomNumber)

            if (room.first){

                //Buscar todas as caracteristicas existentes
                val fileFeaturesRoomDB = "caracteristicas_quarto.txt"
                val fileFeaturesRoom = File("db/$fileFeaturesRoomDB")

                var featureList: MutableList<CaracteristicaQuarto> = mutableListOf()

                for (feature in fileFeaturesRoom.readLines()){
                    val featureSplit = feature.split("|")

                    featureList.add(CaracteristicaQuarto(featureSplit[0].toInt(), featureSplit[1]))
                }

                //Buscar todas as caracteristicas do quarto
                val fileRoomFeaturesDB = "quarto_caracteristicas.txt"
                val fileRoomFeatures = File("db/$fileRoomFeaturesDB")

                var roomFeatureList: MutableList<DTO_RoomCharacteristics> = mutableListOf()

                for (feature in fileRoomFeatures.readLines()){
                    val featureSplit = feature.split("|")

                    if(featureSplit[0].toInt() == roomNumber){
                        val featureName: String = featureList.find{ it.id == featureSplit[1].toInt()}?.nome.toString()
                        roomFeatureList.add(DTO_RoomCharacteristics(featureSplit[0].toInt(), featureSplit[1].toInt(), featureName))
                    }
                }

                val dataToReturn = DTO_RoomCharacteristicsInitialData(featureList, roomFeatureList)

                return Pair(true, dataToReturn)
            }else{
                val dataToReturn = DTO_RoomCharacteristicsInitialData(mutableListOf(), mutableListOf())
                return Pair(false, dataToReturn)
            }




        }

        fun updateRoomCharacteristics(data: DTO_SubmitRoomCharacteristics): Pair<Boolean, String> {

            val room = getRoom(data.idRoom)

            var roomCharacteristicsTemp: MutableList<DTO_RoomCharacteristics> = mutableListOf()
            roomCharacteristicsTemp = data.roomCharacteristics

            if (room.first){
                var roomCharacteristicsList: MutableList<String> = mutableListOf()
                val fileRoomCharacteristicsDB = "quarto_caracteristicas.txt"
                val fileRoomCharacteristics = File("db/$fileRoomCharacteristicsDB")

                for (feature in fileRoomCharacteristics.readLines()){
                    val featureSplit = feature.split("|")

                    if (featureSplit[0].toInt() == data.idRoom){

                        if (data.roomCharacteristics.find { it.idFeature == featureSplit[1].toInt() } != null){
                            roomCharacteristicsList.add(feature)
                            var temp: MutableList<DTO_RoomCharacteristics> = mutableListOf()

                            for (characteristicsTemp in roomCharacteristicsTemp){
                                if (characteristicsTemp.idFeature != featureSplit[1].toInt()){
                                    temp.add(characteristicsTemp)
                                }
                            }
                            roomCharacteristicsTemp = temp
                        }

                    }else{
                        roomCharacteristicsList.add(feature)
                    }
                }

                fileRoomCharacteristics.writeText("")

                if (roomCharacteristicsList != null) {
                    for (feature in roomCharacteristicsList){
                        val featureSplit = feature.split("|")
                        fileRoomCharacteristics.appendText("${featureSplit[0]}|${featureSplit[1]}\n")
                    }
                }

                if (roomCharacteristicsTemp != null) {
                    for (feature in roomCharacteristicsTemp){
                        fileRoomCharacteristics.appendText("${data.idRoom}|${feature.idFeature}\n")
                    }
                }
                return Pair(true, "Ok")
            }
            return Pair(false, "Erro! Quarto não encontrado.")
        }
    }

}