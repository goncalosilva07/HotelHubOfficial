package com.hotelhub.plugins.User

import Permissao
import kotlinx.serialization.Serializable

@Serializable
data class DTO_getUserInitalData(
    val client: DTO_Cliente,
    val permissionsList: MutableList<Permissao>
)