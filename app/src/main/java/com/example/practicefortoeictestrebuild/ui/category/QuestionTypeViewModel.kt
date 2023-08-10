package com.example.practicefortoeictestrebuild.ui.category

import android.app.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.base.DataResult
import com.example.practicefortoeictestrebuild.model.DataOverview
import com.example.practicefortoeictestrebuild.model.QuestionCard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QuestionTypeViewModel : BaseViewModel() {

    private var _questionIds = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }

    private val _questions = MutableLiveData<MutableList<QuestionCard>>().apply {
        value = mutableListOf()
    }

    val questions: LiveData<MutableList<QuestionCard>> get() = _questions

    val questionIds: LiveData<MutableList<String>> get() = _questionIds

    var correctCount = MutableLiveData<Int>().apply {
        value = 0
    }

    var incorrectCount = MutableLiveData<Int>().apply {
        value = 0
    }

    var startTime: LocalDateTime? = null

    var endTime: LocalDateTime? = null

    var review: String? = "1"

    private suspend fun getProgressCardIdReview(): DataResult<MutableList<String>> {
        val list = mutableListOf<String>()
        return suspendCoroutine { continuation ->
            val apiService = ApiHelper.getInstance().create(ApiService::class.java)
            apiService.getProgressCardIdReview(MyApplication.getToken(), review)
                .enqueue(object : Callback<ApiResponse<MutableList<DataOverview>>> {
                    override fun onResponse(
                        call: Call<ApiResponse<MutableList<DataOverview>>>,
                        response: Response<ApiResponse<MutableList<DataOverview>>>
                    ) {
                        if (response.isSuccessful && response.body()?.data != null) {
                            val data = response.body()?.data
                            data?.forEach {
                                list.add(it.cardId)
                            }
                        }
                        continuation.resume(DataResult.Success(list))
                    }

                    override fun onFailure(
                        call: Call<ApiResponse<MutableList<DataOverview>>>,
                        t: Throwable
                    ) {
                        continuation.resume(DataResult.Success(list))
                    }
                })
        }
    }

    fun getQuestion(index: Int, dialog: Dialog) {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        apiService.getQuestion(MyApplication.getToken(), _questionIds.value!![index])
            .enqueue(object : Callback<ApiResponse<QuestionCard>> {
                override fun onResponse(
                    call: Call<ApiResponse<QuestionCard>>,
                    response: Response<ApiResponse<QuestionCard>>
                ) {
                    if (response.isSuccessful && response.body()?.data != null) {
                        val questionCard = response.body()?.data!!
                        questionCard.userChoice = 0
                        _questions.value?.add(questionCard)
                        _questions.value = _questions.value
                    }
                }

                override fun onFailure(call: Call<ApiResponse<QuestionCard>>, t: Throwable) {
                    dialog.show()
                }
            })
    }

    fun updateCardReviewStatus(cardId: String, answer: String) {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        apiService.updateCardReviewStatus(
            MyApplication.getToken(),
            cardId,
            review,
            answer
        ).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(
                call: Call<ApiResponse<Any>>,
                response: Response<ApiResponse<Any>>
            ) {

            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {

            }
        })
    }

    fun getData() {
        executeTask(
            request = {
                getProgressCardIdReview()
            },
            onSuccess = {
                _questionIds.value = it
            },
            onError = {

            }
        )
    }

    fun resetListQuestions() {
        _questions.value = _questions.value
    }

    fun getListSize(): Int {
        return _questionIds.value!!.size
    }

    fun clearQuestions() {
        _questions.value = mutableListOf()
    }
}
