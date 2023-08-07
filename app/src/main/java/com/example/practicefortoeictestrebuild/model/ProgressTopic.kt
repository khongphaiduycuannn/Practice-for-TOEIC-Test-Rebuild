package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class ProgressTopic(
    @SerializedName("topicId")
    val id: String,
    @SerializedName("progress")
    val progress: Int
)
