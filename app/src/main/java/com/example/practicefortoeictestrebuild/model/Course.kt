package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val title: String,
    @SerializedName("lessons")
    var listLessons: MutableList<DataOverview>?,
    @SerializedName("topics")
    var listTopics: MutableList<DataOverview>?,
)
