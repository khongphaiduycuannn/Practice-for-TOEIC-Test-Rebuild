package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class QuestionCardDaily(
    @SerializedName("day")
    val day: Int,
    @SerializedName("cards")
    val cards: MutableList<String>
)