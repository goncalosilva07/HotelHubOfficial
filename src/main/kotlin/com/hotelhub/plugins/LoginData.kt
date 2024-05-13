package com.hotelhub.plugins

import kotlinx.serialization.Serializable

@Serializable
data class LoginData(val id: String, val userName: String, val enc: String)