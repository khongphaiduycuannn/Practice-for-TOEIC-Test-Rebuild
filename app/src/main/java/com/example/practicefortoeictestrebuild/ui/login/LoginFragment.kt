package com.example.practicefortoeictestrebuild.ui.login

import android.app.Dialog
import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentLoginBinding
import com.example.practicefortoeictestrebuild.ui.main.MainActivity
import com.example.practicefortoeictestrebuild.utils.PopUpNotification
import com.example.practicefortoeictestrebuild.utils.startLoading
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val dialog by lazy { context?.let { Dialog(it) } }

    private val popUpNotification by lazy { PopUpNotification(requireContext()) }

    override fun initData() {

    }

    override fun handleEvent() {
        binding.loginArea.btnLogin.setOnClickListener {
            val email = binding.loginArea.edtUsername.text.toString()
            val password = binding.loginArea.edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                binding.loginArea.errorMessage.text = "* Email, password are required!"
                binding.loginArea.errorMessage.setTextColor(requireActivity().getColor(carbon.R.color.carbon_red_200))
            } else login(email, password)
        }

        binding.loginArea.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    override fun bindData() {
        popUpNotification.title.text = "Login failed!"
    }

    private fun login(email: String, password: String) {
        val body = mapOf(
            "email" to email,
            "password" to password
        )

        dialog?.startLoading()

        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        apiService.login(body).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(
                call: Call<ApiResponse<Any>>,
                response: Response<ApiResponse<Any>>
            ) {
                val token = response.body()?.data.toString()
                if (response.isSuccessful) {
                    MyApplication.setToken(token)
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().overridePendingTransition(
                        R.anim.transition_zoom_in,
                        R.anim.transition_zoom_out
                    )
                    requireActivity().finishAffinity()
                } else {
                    val jsonObject = JSONObject(response.errorBody()!!.string())
                    popUpNotification.message.text = jsonObject.getString("message")
                    popUpNotification.show(requireView())
                }
                dialog?.dismiss()
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                popUpNotification.message.text =
                    "Your internet connection is not stable, please try again later"
                popUpNotification.show(requireView())
                dialog?.dismiss()
            }
        })
    }
}