package com.example.practicefortoeictestrebuild.ui.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.base.DataResult
import com.example.practicefortoeictestrebuild.model.Document
import com.example.practicefortoeictestrebuild.model.Lesson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LessonViewModel : BaseViewModel() {

    private val _lessonId = MutableLiveData<String>().apply {
        value = ""
    }

    private val _documents = MutableLiveData<MutableList<Document>>().apply {
        value = mutableListOf()
    }

    val lessonId: LiveData<String> get() = _lessonId

    val documents: LiveData<MutableList<Document>> get() = _documents

    fun setId(id: String?) {
        _lessonId.value = id
    }

    private suspend fun getDocument(): DataResult<MutableList<Document>> {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        val tempList: MutableList<Document> = suspendCoroutine { continuation ->
            apiService.getLesson(MyApplication.getToken(), _lessonId.value)
                .enqueue(object : Callback<ApiResponse<Lesson>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Lesson>>,
                        response: Response<ApiResponse<Lesson>>
                    ) {
                        if (response.isSuccessful && response.body()?.data != null)
                            continuation.resume(response.body()?.data!!.documents)
                        else continuation.resume(mutableListOf())
                    }

                    override fun onFailure(call: Call<ApiResponse<Lesson>>, t: Throwable) {
                        continuation.resume(mutableListOf())
                    }
                })
        }
        return DataResult.Success(tempList)
    }

    fun getData() {
        executeTask(
            request = {
                getDocument()
            },
            onSuccess = {
                _documents.value = it
            },
            onError = {

            }
        )
    }
}