package com.hotelhub.plugins.Reserve

import CaracteristicaQuarto
import kotlinx.serialization.Serializable

@Serializable
data class DTO_DataSerchRoomsForReserve(val startDate: String, val endDate: String, val numberOfPeople: Int, val additionalServices: MutableList<CaracteristicaQuarto>)