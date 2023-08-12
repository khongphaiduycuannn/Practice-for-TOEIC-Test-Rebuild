package com.example.practicefortoeictestrebuild.ui.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.base.DataResult
import com.example.practicefortoeictestrebuild.utils.RealPathUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ProfileViewModel : BaseViewModel() {

    var selectedImageUri: Uri? = null

    var newUsername: String? = null

    var email: String? = null

    val status = MutableLiveData<Boolean>()

    private suspend fun updateAvatar(): DataResult<Boolean> {
        val realPath =
            RealPathUtil.getRealPath(MyApplication.getAppContext(), selectedImageUri!!)
                ?: return DataResult.Success(false)

        val file = File(realPath)
        val reqBodyAvatar = RequestBody.create(
            MediaType.parse("image/*"),
            file
        )
        val multipartBodyAvatar =
            MultipartBody.Part.createFormData("avatar", file.name, reqBodyAvatar)
        val email: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            email
        )

        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        return suspendCoroutine { continuation ->
            apiService.updateAvatar(MyApplication.getToken(), multipartBodyAvatar, email)
                .enqueue(object : Callback<ApiResponse<Any>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Any>>,
                        response: Response<ApiResponse<Any>>
                    ) {
                        if (response.isSuccessful)
                            continuation.resume(DataResult.Success(true))
                        else continuation.resume(DataResult.Success(false))
                    }

                    override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                        continuation.resume(DataResult.Success(false))
                    }
                })
        }
    }

    private suspend fun updateUsername(): DataResult<Boolean> {
        val name: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            newUsername
        )

        val email: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            email
        )

        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        return suspendCoroutine { continuation ->
            apiService.updateUsername(MyApplication.getToken(), name, email)
                .enqueue(object : Callback<ApiResponse<Any>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Any>>,
                        response: Response<ApiResponse<Any>>
                    ) {
                        if (response.isSuccessful)
                            continuation.resume(DataResult.Success(true))
                        else continuation.resume(DataResult.Success(false))
                    }

                    override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                        continuation.resume(DataResult.Success(false))
                    }
                })
        }
    }

    fun putAvatar() {
        executeTask(
            request = {
                updateAvatar()
            },
            onSuccess = {
                status.value = it
            },
            onError = {

            }
        )
    }

    fun putUsername() {
        executeTask(
            request = {
                updateUsername()
            },
            onSuccess = {
                status.value = it
            },
            onError = {

            }
        )
    }

    fun putData() {
        executeTask(
            request = {
                updateUsername()
                updateAvatar()
            },
            onSuccess = {
                status.value = it
            },
            onError = {

            }
        )
    }
}