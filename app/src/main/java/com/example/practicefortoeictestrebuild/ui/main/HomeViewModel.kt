package com.example.practicefortoeictestrebuild.ui.main

import android.app.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.base.DataResult
import com.example.practicefortoeictestrebuild.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HomeViewModel : BaseViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User> get() = _user

    private suspend fun getUser(): DataResult<User?> {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        return suspendCoroutine { continuation ->
            apiService.getUser(MyApplication.getToken())
                .enqueue(object : Callback<ApiResponse<User>> {
                    override fun onResponse(
                        call: Call<ApiResponse<User>>,
                        response: Response<ApiResponse<User>>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(DataResult.Success(response.body()!!.data))
                        } else {
                            continuation.resume(DataResult.Success(null))
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                        continuation.resume(DataResult.Error(Exception()))
                    }
                })
        }
    }

    fun getUserWithOutDialog() {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        apiService.getUser(MyApplication.getToken())
            .enqueue(object : Callback<ApiResponse<User>> {
                override fun onResponse(
                    call: Call<ApiResponse<User>>,
                    response: Response<ApiResponse<User>>
                ) {
                    if (response.isSuccessful && response.body()?.data != null) {
                        _user.value = response.body()?.data!!
                    }
                }

                override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {

                }
            })
    }

    fun getData(dialog: Dialog) {
        executeTask(
            request = {
                getUser()
            },
            onSuccess = {
                _user.value = it
            },
            onError = {
                dialog.show()
            }
        )
    }
}