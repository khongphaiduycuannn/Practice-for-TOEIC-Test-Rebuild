package com.example.practicefortoeictestrebuild.ui.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.base.DataResult
import com.example.practicefortoeictestrebuild.model.Course
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CourseViewModel : BaseViewModel() {

    private val _title = MutableLiveData<String>().apply {
        value = ""
    }

    private val _group = MutableLiveData<String>().apply {
        value = ""
    }

    private val _listCourse = MutableLiveData<MutableList<Course>>().apply {
        value = mutableListOf()
    }

    val title: LiveData<String> get() = _title

    val group: LiveData<String> get() = _group

    val listCourse: LiveData<MutableList<Course>> get() = _listCourse

    fun setTitle(title: String?) {
        this._title.value = title
    }

    fun setGroup(group: String?) {
        this._group.value = group
    }

    private suspend fun getListCourse(): DataResult<MutableList<Course>> {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        val tempList: MutableList<Course> = suspendCoroutine { continuation ->
            apiService.getCourse(MyApplication.getToken(), _group.value)
                .enqueue(object : Callback<ApiResponse<MutableList<Course>>> {
                    override fun onResponse(
                        call: Call<ApiResponse<MutableList<Course>>>,
                        response: Response<ApiResponse<MutableList<Course>>>
                    ) {
                        response.body()
                        if (response.isSuccessful && response.body()?.data != null)
                            continuation.resume(response.body()?.data!!)
                        else continuation.resume(mutableListOf())
                    }

                    override fun onFailure(
                        call: Call<ApiResponse<MutableList<Course>>>,
                        t: Throwable
                    ) {
                        continuation.resume(mutableListOf())
                    }
                })
        }
        return DataResult.Success(tempList)
    }

    fun getData() {
        executeTask(
            request = {
                getListCourse()
            },
            onSuccess = {
                _listCourse.value = it
            },
            onError = {

            }
        )
    }
}