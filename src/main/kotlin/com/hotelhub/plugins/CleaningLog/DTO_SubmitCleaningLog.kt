package com.hotelhub.plugins.CleaningLog

import kotlinx.serialization.Serializable

@Serializable
data class DTO_SubmitCleaningLog (val idRoom: Int, val idUser: String){
}