package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class QuestionCard(
    @SerializedName("_id")
    val id: String,
    @SerializedName("children")
    val children: MutableList<QuestionCardChildren>,
    var userChoice: Int
)