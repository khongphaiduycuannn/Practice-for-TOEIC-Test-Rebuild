package com.example.practicefortoeictestrebuild.model

import com.google.gson.annotations.SerializedName

data class QuestionCardChildren(
    @SerializedName("question")
    val question: Question,
    @SerializedName("answer")
    val answer: Answer
)