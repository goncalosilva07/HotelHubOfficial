package com.hotelhub.plugins.RoomCharacteristics

import kotlinx.serialization.Serializable
import javax.xml.catalog.CatalogFeatures.Feature

@Serializable
data class DTO_RoomCharacteristics (val idRoom: Int, val idFeature: Int, val nameFeature: String){
}