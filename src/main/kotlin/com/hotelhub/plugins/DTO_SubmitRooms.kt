package com.hotelhub.plugins

import Quarto
import kotlinx.serialization.Serializable

@Serializable
data class DTO_SubmitRooms (val roomList: MutableList<Quarto>){
}