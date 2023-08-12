package com.example.practicefortoeictestrebuild.api

import com.example.practicefortoeictestrebuild.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    ): Call<ApiResponse<TopicTest>>

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

    @GET("/api/v1/progress/courses")
    fun getProgressCourse(
        @Header("Authorization") authorization: String?,
        @Query("group") name: String?
    ): Call<ApiResponse<MutableList<ProgressCourse>>>

    @GET("/api/v1/topics/{topicId}")
    fun getFlashCard(
        @Header("Authorization") authorization: String?,
        @Path("topicId") topicId: String?
    ): Call<ApiResponse<TopicVocabulary>>

    @GET("/api/v1/progress/cards/daily")
    fun getFlashcardDaily(
        @Header("Authorization") authorization: String?,
    ): Call<ApiResponse<MutableList<FlashCard>>>

    @GET("/api/v1/progress/cards/review")
    fun getProgressCardIdReview(
        @Header("Authorization") authorization: String?,
        @Query("review") review: String?
    ): Call<ApiResponse<MutableList<DataOverview>>>

    @GET("/api/v1/progress/calendar")
    fun getDailyQuestionCard(
        @Header("Authorization") authorization: String?,
        @Query("year") year: Int?,
        @Query("month") month: Int?
    ): Call<ApiResponse<MutableList<QuestionCardDaily>>>

    @GET("/api/v1/game/single")
    fun getFlashcardGame(
        @Header("Authorization") authorization: String?,
        @Query("topicId") topicId: String?,
    ): Call<ApiResponse<MutableList<FlashcardQuestion>>>

    @PUT("/api/v1/progress/cards/review")
    fun updateCardReviewStatus(
        @Header("Authorization") authorization: String?,
        @Query("cardId") cardId: String?,
        @Query("status") status: String?,
        @Query("answer") answer: String?
    ): Call<ApiResponse<Any>>

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

    @PUT("/api/v1/progress/lesson")
    fun updateProgressLesson(
        @Header("Authorization") authorization: String?,
        @Query("lessonId") lessonId: String,
    ): Call<ApiResponse<Any>>

    @PUT("/api/v1/progress/calendar")
    fun updateProgressCalendar(
        @Header("Authorization") authorization: String?,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("cardId") cardId: String
    ): Call<ApiResponse<Any>>

    @Multipart
    @PUT("/api/v1/auth/profile")
    fun updateAvatar(
        @Header("Authorization") authorization: String?,
        @Part avatar: MultipartBody.Part,
        @Part("email") email: RequestBody
    ): Call<ApiResponse<Any>>

    @Multipart
    @PUT("/api/v1/auth/profile")
    fun updateUsername(
        @Header("Authorization") authorization: String?,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody
    ): Call<ApiResponse<Any>>
}