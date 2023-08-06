package com.example.practicefortoeictestrebuild.ui.login

import android.app.Dialog
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentSignUpBinding
import com.example.practicefortoeictestrebuild.utils.PopUpNotification
import com.example.practicefortoeictestrebuild.utils.startLoading
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val dialog by lazy { context?.let { Dialog(it) } }

    private val popUpNotification by lazy { PopUpNotification(requireContext()) }

    override fun initData() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun handleEvent() {
        binding.btnSignUp.setOnClickListener {
            val name = binding.edtDisplayName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            signUp(
                name = name,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
        }

        binding.btnLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {
        popUpNotification.title.text = "Login failed!"
    }

    private fun signUp(
        email: String = "",
        name: String = "",
        password: String = "",
        confirmPassword: String = ""
    ) {
        val body = mapOf(
            "name" to name,
            "email" to email,
            "password" to password,
            "confirmPassword" to confirmPassword
        )

        dialog?.startLoading()
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)
        apiService.register(body).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(
                call: Call<ApiResponse<Any>>,
                response: Response<ApiResponse<Any>>
            ) {
                if (response.isSuccessful) {
                    findNavController().popBackStack()
                    Toast.makeText(requireActivity(), "Registration Successful", Toast.LENGTH_LONG).show()
                }
                else {
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