package com.example.practicefortoeictestrebuild.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.model.DataOverview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel : ViewModel() {

    private val _weakCount = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _familiarCount = MutableLiveData<Int>().apply {
        value = 0
    }

    val weakCount: LiveData<Int> get() = _weakCount

    val familiarCount: LiveData<Int> get() = _familiarCount

    fun getProgressCardIdReview(review: String) {
        val list = mutableListOf<String>()
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
                        if (review == "1") _weakCount.value = list.size
                        if (review == "2") _familiarCount.value = list.size
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<MutableList<DataOverview>>>,
                    t: Throwable
                ) {
                }
            })
    }
}
