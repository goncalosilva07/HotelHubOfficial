package com.hotelhub.plugins.Dashboard

import com.hotelhub.plugins.Task.Task
import kotlinx.serialization.Serializable

@Serializable
data class DTO_GetEmployeeDashboardInitialData (val tasks: MutableList<Task>){

}