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
    }

}