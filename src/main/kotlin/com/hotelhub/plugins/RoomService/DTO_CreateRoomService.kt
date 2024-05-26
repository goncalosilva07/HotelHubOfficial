package com.hotelhub.plugins.RoomService

import kotlinx.serialization.Serializable

@Serializable
data class DTO_CreateRoomService(val idReserve: String, val products: MutableList<DTO_MenuRoomServiceUser>) {
}