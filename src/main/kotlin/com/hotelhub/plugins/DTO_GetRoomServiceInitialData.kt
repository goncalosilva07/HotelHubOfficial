package com.hotelhub.plugins

import kotlinx.serialization.Serializable

@Serializable
data class DTO_GetRoomServiceInitialData(val idReserve: String,
                                    val menu: MutableList<MenuRoomService>) {
}