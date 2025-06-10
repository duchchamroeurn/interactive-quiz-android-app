package com.dcr.iqa.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SubmittedAnswerRequest(
    val questionId: String,
    val submittedValue: String // This will be the selected option ID
)