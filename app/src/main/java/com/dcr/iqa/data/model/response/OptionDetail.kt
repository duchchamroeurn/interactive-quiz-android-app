package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class OptionDetail(
    val id: String,
    val optionText: String,
    val correct: Boolean
)
