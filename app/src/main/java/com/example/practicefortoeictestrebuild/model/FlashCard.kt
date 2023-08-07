package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class FlashCard(
    @SerializedName("_id")
    val id: String,
    @SerializedName("word")
    val textFront: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("ipa")
    val ipa: String,
    @SerializedName("sound")
    val sound: String,
    @SerializedName("texts")
    val textBack: String,
    @SerializedName("hint")
    val hint: String
)
