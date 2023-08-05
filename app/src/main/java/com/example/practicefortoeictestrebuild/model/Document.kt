package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("title")
    val title: String,
    @SerializedName("contents")
    val contents: MutableList<String>
)
