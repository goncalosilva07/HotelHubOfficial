package com.hotelhub.plugins.Reserve

import kotlinx.serialization.Serializable

@Serializable
data class DTO_CheckInOrCheckOut (val idReserve: String, val action: Int /*1 - CheckIn; 2 - CheckOut*/){
}