package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("hint")
    val hint: String?,
    @SerializedName("texts")
    val content: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("sound")
    val sound: String?,
)