package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("correct")
    val answer: String,
    @SerializedName("choices")
    val choices: List<String> = mutableListOf()
)