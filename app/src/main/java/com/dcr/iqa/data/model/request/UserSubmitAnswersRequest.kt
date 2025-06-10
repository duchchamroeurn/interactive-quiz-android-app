package com.dcr.iqa.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserSubmitAnswersRequest(
    val answers: List<SubmittedAnswerRequest>
)
