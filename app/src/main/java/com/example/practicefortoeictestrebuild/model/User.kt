package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?
)