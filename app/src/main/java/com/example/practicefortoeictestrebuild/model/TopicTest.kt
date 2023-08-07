package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class TopicTest(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("cards")
    val cards: MutableList<DataOverview>
)
