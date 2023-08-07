package com.example.practicefortoeictestrebuild.ui.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.adapter.CourseAdapter
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.base.DataResult
import com.example.practicefortoeictestrebuild.model.Course
import com.example.practicefortoeictestrebuild.model.ProgressCourse
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

    fun updateLesson(lessonId: String, progress: Int) {
        _listCourse.value?.forEach { course ->
            course.listLessons?.forEach {
                if (it.id == lessonId) it.progress = progress
            }
        }
        _listCourse.value = _listCourse.value
    }

    private suspend fun getListCourse(): MutableList<Course> {
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
        return tempList
    }

    private suspend fun getProgressCourse(): MutableList<ProgressCourse> {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        val tempList: MutableList<ProgressCourse> = suspendCoroutine { continuation ->
            apiService.getProgressCourse(MyApplication.getToken(), _group.value)
                .enqueue(object : Callback<ApiResponse<MutableList<ProgressCourse>>> {
                    override fun onResponse(
                        call: Call<ApiResponse<MutableList<ProgressCourse>>>,
                        response: Response<ApiResponse<MutableList<ProgressCourse>>>
                    ) {
                        if (response.isSuccessful && response.body()?.data != null)
                            continuation.resume(response.body()?.data!!)
                        else continuation.resume(mutableListOf())
                    }

                    override fun onFailure(
                        call: Call<ApiResponse<MutableList<ProgressCourse>>>,
                        t: Throwable
                    ) {
                        continuation.resume(mutableListOf())
                    }
                })
        }
        return tempList
    }

    private suspend fun mergeCourse(): DataResult<MutableList<Course>> {
        val listCourse = getListCourse()
        val listProgress = getProgressCourse()

        listProgress.forEach { progressCourse ->
            listCourse.forEach { course ->
                if (progressCourse.id == course.id) {
                    progressCourse.listLessons.forEach { lessonId ->
                        course.listLessons?.forEach {
                            if (it.id == lessonId) it.progress = 1
                        }
                    }

                    progressCourse.listTopics.forEach {

                    }
                }
            }
        }

        return DataResult.Success(listCourse)
    }

    fun getData() {
        executeTask(
            request = {
                mergeCourse()
            },
            onSuccess = {
                _listCourse.value = it
            },
            onError = {

            }
        )
    }
}