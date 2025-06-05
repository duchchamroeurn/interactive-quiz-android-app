package com.dcr.iqa.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDetail(
    val id: String,
    val questionText: String,
    val time: Int,
    val type: String,
    val options: List<OptionDetail>
)
