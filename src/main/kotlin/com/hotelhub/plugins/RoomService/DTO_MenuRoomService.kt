package com.hotelhub.plugins.RoomService

import kotlinx.serialization.Serializable

@Serializable
data class DTO_MenuRoomService(val id: Int,
                          val name: String,
                          val price: Double)
{
}