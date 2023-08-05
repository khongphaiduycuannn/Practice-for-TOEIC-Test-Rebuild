package com.example.practicefortoeictestrebuild.api

data class ApiResponse<T> (
    val code: String,
    val message: String,
    val data: T
)