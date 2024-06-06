package com.hotelhub.plugins.Reserve

import kotlinx.serialization.Serializable

@Serializable
data class DTO_ReserveData(val idRoom: Int,
                           var idClient: String?,
                           val clientUserName: String?,
                           val name: String,
                           val surname: String,
                           val email: String,
                           val phoneNumber: String,
                           val startDate: String,
                           val endDate: String)
