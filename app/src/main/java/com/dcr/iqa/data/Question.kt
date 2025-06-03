package com.dcr.iqa.data

data class Question(
    val id: String,
    val questionText: String,
    val time: Int,
    val type: String, // "MULTIPLE_CHOICE", "TRUE_FALSE", "YES_NO"
    val options: List<Option>
)
