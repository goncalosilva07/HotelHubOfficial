package com.hotelhub.plugins.Permission

import kotlinx.serialization.Serializable

@Serializable
data class DTO_UserPermissions (val userName: String,  val permissions: MutableList<Permissao>){
}