package com.hotelhub.plugins.Task

import kotlinx.serialization.Serializable

@Serializable
data class DTO_UserTasks (val userName: String, val tasks: MutableList<Task>) {
}