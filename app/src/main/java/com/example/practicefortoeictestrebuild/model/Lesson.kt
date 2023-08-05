package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class Lesson(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("children")
    val documents: MutableList<Document>
)
