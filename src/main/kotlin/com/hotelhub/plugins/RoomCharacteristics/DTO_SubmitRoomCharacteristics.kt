package com.hotelhub.plugins.RoomCharacteristics

import kotlinx.serialization.Serializable

@Serializable
data class DTO_SubmitRoomCharacteristics (val idRoom: Int, val roomCharacteristics: MutableList<DTO_RoomCharacteristics>){
}