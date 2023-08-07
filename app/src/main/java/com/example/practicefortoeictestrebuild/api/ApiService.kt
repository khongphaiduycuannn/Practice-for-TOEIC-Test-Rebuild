package com.example.practicefortoeictestrebuild.api

import com.example.practicefortoeictestrebuild.model.*
import retrofit2.Call
import retrofit2.http.*

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

    @GET("/api/v1/questioncards/{questionId}")
    fun getQuestion(
        @Header("Authorization") authorization: String?,
        @Path("questionId") questionId: String?
    ): Call<ApiResponse<QuestionCard>>

    @POST("/api/v1/auth/login")
    fun login(
        @Body body: Map<String, String>
    ): Call<ApiResponse<Any>>

    @POST("/api/v1/auth/register")
    fun register(
        @Body body: Map<String, String>
    ): Call<ApiResponse<Any>>

    @PUT("/api/v1/progress/cards/study")
    fun updateProgressCard(
        @Header("Authorization") authorization: String?,
        @Query("cardId") cardId: String,
        @Query("status") status: String,
        @Query("answer") answer: String
    ): Call<ApiResponse<Any>>
}