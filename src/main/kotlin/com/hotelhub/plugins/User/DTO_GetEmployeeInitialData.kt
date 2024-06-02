package com.hotelhub.plugins.User

import Permissao
import kotlinx.serialization.Serializable

@Serializable
data class DTO_GetEmployeeInitialData(
    val user: DTO_Employee,
    val permissionsList: MutableList<Permissao>
)