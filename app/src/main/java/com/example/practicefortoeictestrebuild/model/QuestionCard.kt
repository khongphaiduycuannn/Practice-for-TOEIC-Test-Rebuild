package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class QuestionCard(
    @SerializedName("_id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("sound")
    val sound: String,
    @SerializedName("texts")
    val content: String,
    @SerializedName("correct")
    val correct: String,
    @SerializedName("choices")
    val choices: MutableList<String>,
    var userChoice: Int
)