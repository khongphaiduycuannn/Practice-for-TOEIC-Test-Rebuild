package com.example.practicefortoeictestrebuild.ui.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.base.DataResult
import com.example.practicefortoeictestrebuild.model.DataOverview
import com.example.practicefortoeictestrebuild.model.TopicTest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TestViewModel : BaseViewModel() {

    private val _topicName = MutableLiveData<String>().apply {
        value = ""
    }

    private val _topicIds = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }

    private val _allQuestion = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }

    private val _correctQuestion = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }

    private val _wrongQuestion = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }

    val topicName: LiveData<String> get() = _topicName

    val topicIds: LiveData<MutableList<String>> get() = _topicIds

    val allQuestion: LiveData<MutableList<String>> get() = _allQuestion

    val correctQuestion: LiveData<MutableList<String>> get() = _correctQuestion

    val wrongQuestion: LiveData<MutableList<String>> get() = _wrongQuestion

    val index = MutableLiveData<Int>().apply { value = 0 }

    fun setTopicId(list: MutableList<String>?) {
        _topicIds.value = list
    }

    private suspend fun getQuestion(): DataResult<Boolean> {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)

        val allQuestion = mutableListOf<String>()
        val correctQuestion = mutableListOf<String>()
        val wrongQuestion = mutableListOf<String>()

        val x = suspendCoroutine { continuation ->
            apiService.getProgressCard(MyApplication.getToken(),
                index.value?.let { _topicIds.value?.get(it) })
                .enqueue(object : Callback<ApiResponse<MutableList<DataOverview>>> {
                    override fun onResponse(
                        call: Call<ApiResponse<MutableList<DataOverview>>>,
                        response: Response<ApiResponse<MutableList<DataOverview>>>
                    ) {
                        if (response.isSuccessful && response.body()?.data != null) {
                            val list = response.body()?.data
                            list?.forEach {
                                if (it.status == 1) wrongQuestion.add(it.cardId)
                                else correctQuestion.add(it.cardId)
                            }
                            continuation.resume(1)
                        } else continuation.resume(0)
                    }

                    override fun onFailure(
                        call: Call<ApiResponse<MutableList<DataOverview>>>,
                        t: Throwable
                    ) {
                        continuation.resume(0)
                    }
                })
        }

        val y = suspendCoroutine { continuation ->
            apiService.getTopic(
                MyApplication.getToken(),
                index.value?.let { _topicIds.value?.get(it) })
                .enqueue(object : Callback<ApiResponse<TopicTest>> {
                    override fun onResponse(
                        call: Call<ApiResponse<TopicTest>>,
                        response: Response<ApiResponse<TopicTest>>
                    ) {
                        if (response.isSuccessful && response.body()?.data != null) {
                            _topicName.value = response.body()?.data!!.name
                            response.body()?.data!!.cards.forEach {
                                allQuestion.add(it.id)
                            }
                            continuation.resume(1)
                        } else continuation.resume(0)
                    }

                    override fun onFailure(call: Call<ApiResponse<TopicTest>>, t: Throwable) {
                        continuation.resume(0)
                    }
                })
        }
        _allQuestion.value = allQuestion
        _correctQuestion.value = correctQuestion
        _wrongQuestion.value = wrongQuestion

        return DataResult.Success(true)
    }

    fun getData() {
        executeTask(
            request = {
                getQuestion()
            },
            onSuccess = {

            },
            onError = {

            })
    }

    fun removeCorrectCard(id: String) {
        _correctQuestion.value?.remove(id)
        if (_wrongQuestion.value?.contains(id) == false) {
            _wrongQuestion.value?.add(id)
        }
    }

    fun removeWrongCard(id: String) {
        _wrongQuestion.value?.remove(id)
        if (_correctQuestion.value?.contains(id) == false) {
            _correctQuestion.value?.add(id)
        }
    }

    fun indexPlusOne() {
        index.value = index.value!! + 1
    }
}