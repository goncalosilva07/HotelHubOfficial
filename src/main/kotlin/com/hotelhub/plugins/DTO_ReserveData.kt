package com.hotelhub.plugins

import kotlinx.serialization.Serializable

@Serializable
data class DTO_ReserveData(val idRoom: Int,
                           val idClient: String,
                           val name: String,
                           val surname: String,
                           val email: String,
                           val phoneNumber: String,
                           val startDate: String,
                           val endDate: String)
