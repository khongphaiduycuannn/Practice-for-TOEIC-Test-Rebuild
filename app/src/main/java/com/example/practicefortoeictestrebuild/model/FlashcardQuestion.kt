package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class FlashcardQuestion(
    @SerializedName("word")
    val word: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("sound")
    val sound: String,
    @SerializedName("correct")
    val correct: String,
    @SerializedName("choices")
    val choices: MutableList<String>,
    var userChoice: Int = 0
)