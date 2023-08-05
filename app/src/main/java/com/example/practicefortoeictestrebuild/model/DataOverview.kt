package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class DataOverview(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String
)