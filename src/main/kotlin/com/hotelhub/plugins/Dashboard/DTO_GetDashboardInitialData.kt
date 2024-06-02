package com.hotelhub.plugins.Dashboard

import com.hotelhub.plugins.Reserve.Reserva
import com.hotelhub.plugins.RoomService.DTO_GetRoomServiceDashboard
import kotlinx.serialization.Serializable

@Serializable
data class DTO_GetDashboardInitialData (val reserveList: MutableList<Reserva>, val roomServiceList: MutableList<DTO_GetRoomServiceDashboard>){
}