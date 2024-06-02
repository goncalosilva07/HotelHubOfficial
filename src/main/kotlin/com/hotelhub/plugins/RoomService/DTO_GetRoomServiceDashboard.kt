package com.hotelhub.plugins.RoomService

import kotlinx.serialization.Serializable

@Serializable
data class DTO_GetRoomServiceDashboard (val nome: String, val quantidade: Int, val preco: Double){
}