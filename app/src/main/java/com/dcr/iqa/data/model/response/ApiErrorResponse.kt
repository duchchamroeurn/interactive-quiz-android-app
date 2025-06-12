package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ApiErrorResponse(
    val timestamp:String,
    val status: Int,
    val error: String?,
    val message: String?,
    val fieldErrors: Map<String, JsonElement>?
)
