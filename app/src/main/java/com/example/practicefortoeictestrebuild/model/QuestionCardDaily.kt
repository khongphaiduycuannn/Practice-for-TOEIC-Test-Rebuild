package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class QuestionCardDaily(
    @SerializedName("cards")
    val cards: MutableList<String>
)