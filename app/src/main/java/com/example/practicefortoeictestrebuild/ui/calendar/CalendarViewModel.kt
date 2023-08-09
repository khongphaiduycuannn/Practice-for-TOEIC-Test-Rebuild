package com.example.practicefortoeictestrebuild.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.model.QuestionCardDaily
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class CalendarViewModel : BaseViewModel() {

    private val _listQuestionCardDaily = MutableLiveData<MutableList<QuestionCardDaily>>().apply {
        value = mutableListOf()
    }

    val listQuestionCardDaily: LiveData<MutableList<QuestionCardDaily>> get() = _listQuestionCardDaily

    fun getDailyQuestionCard() {
        val localDate = LocalDate.now()
        val year = localDate.year
        val month = localDate.month.value

        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        apiService.getDailyQuestionCard(
            MyApplication.getToken(),
            year,
            month,
        ).enqueue(object : Callback<ApiResponse<MutableList<QuestionCardDaily>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<QuestionCardDaily>>>,
                response: Response<ApiResponse<MutableList<QuestionCardDaily>>>
            ) {
                if (response.isSuccessful && response.body()?.data != null) {
                    _listQuestionCardDaily.value = response.body()?.data
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<MutableList<QuestionCardDaily>>>,
                t: Throwable
            ) {

            }
        })
    }
}