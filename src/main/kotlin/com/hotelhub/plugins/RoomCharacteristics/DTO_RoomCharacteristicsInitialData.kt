package com.hotelhub.plugins.RoomCharacteristics

import CaracteristicaQuarto
import kotlinx.serialization.Serializable

@Serializable
data class DTO_RoomCharacteristicsInitialData (val characteristicsList: MutableList<CaracteristicaQuarto>, val roomCharacteristics: MutableList<DTO_RoomCharacteristics>){
}