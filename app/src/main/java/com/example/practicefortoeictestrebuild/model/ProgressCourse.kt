package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class ProgressCourse(
    @SerializedName("courseId")
    val id: String,
    @SerializedName("lessons")
    val listLessons: MutableList<String>,
    @SerializedName("topics")
    val listTopics: MutableList<ProgressTopic>
)
