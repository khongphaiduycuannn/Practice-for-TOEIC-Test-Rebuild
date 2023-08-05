package com.example.practicefortoeictestrebuild.api

import com.example.practicefortoeictestrebuild.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/v1/auth/me")
    fun getUser(
        @Header("Authorization") authorization: String?
    ): Call<ApiResponse<User>>

    @GET("/api/v1/courses")
    fun getCourse(
        @Header("Authorization") authorization: String?,
        @Query("group") groupName: String?
    ): Call<ApiResponse<MutableList<Course>>>

    @GET("/api/v1/lessons/{lessonId}")
    fun getLesson(
        @Header("Authorization") authorization: String?,
        @Path("lessonId") lessonId: String?
    ): Call<ApiResponse<Lesson>>

    @GET("/api/v1/topics/{topicId}")
    fun getTopic(
        @Header("Authorization") authorization: String?,
        @Path("topicId") topicId: String?
    ): Call<ApiResponse<Topic>>

    @GET("/api/v1/progress/cards/study")
    fun getProgressCard(
        @Header("Authorization") authorization: String?,
        @Query("topicId") topicId: String?
    ): Call<ApiResponse<MutableList<DataOverview>>>
}