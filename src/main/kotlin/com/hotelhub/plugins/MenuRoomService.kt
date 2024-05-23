package com.hotelhub.plugins

import kotlinx.serialization.Serializable

@Serializable
class MenuRoomService(val id: Int,
                      val name: String,
                      val price: Double)
{
}