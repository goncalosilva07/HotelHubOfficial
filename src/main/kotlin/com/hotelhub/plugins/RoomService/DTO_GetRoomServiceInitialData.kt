package com.hotelhub.plugins.RoomService

import kotlinx.serialization.Serializable

@Serializable
data class DTO_GetRoomServiceInitialData(val idReserve: String,
                                         val menu: MutableList<DTO_MenuRoomService>) {
}