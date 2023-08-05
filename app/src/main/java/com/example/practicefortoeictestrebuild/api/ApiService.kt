package com.example.practicefortoeictestrebuild.api

import com.example.practicefortoeictestrebuild.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("/api/v1/auth/me")
    fun getUser(
        @Header("Authorization") authorization: String?
    ): Call<ApiResponse<User>>
}