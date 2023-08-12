package com.example.practicefortoeictestrebuild.ui.profile

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.base.BaseActivity
import com.example.practicefortoeictestrebuild.databinding.ActivityProfileBinding
import com.example.practicefortoeictestrebuild.databinding.DialogEditProfileBinding
import com.example.practicefortoeictestrebuild.databinding.DialogInternetErrorBinding
import com.example.practicefortoeictestrebuild.model.User
import com.example.practicefortoeictestrebuild.ui.calendar.CalendarViewModel
import com.example.practicefortoeictestrebuild.ui.main.HomeViewModel
import com.example.practicefortoeictestrebuild.utils.startLoading
import java.util.*

private const val SELECT_PICTURE = 200

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {

    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private val calendarViewModel by lazy {
        ViewModelProvider(this)[CalendarViewModel::class.java]
    }

    private val profileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private val internetDialog by lazy { Dialog(this) }

    private val internetDialogBinding by lazy { DialogInternetErrorBinding.inflate(layoutInflater) }

    private val editDialog by lazy { Dialog(this) }

    private val editDialogBinding by lazy { DialogEditProfileBinding.inflate(layoutInflater) }


    override fun initData() {
        initDialog()
        homeViewModel.getData(internetDialog)
        calendarViewModel.getDailyQuestionCard()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        }
    }

    override fun handleEvent() {
        binding.btnEditProfile.setOnClickListener {
            editDialog.show()
        }

        editDialogBinding.imgNewAvatar.setOnClickListener {
            imageChooser()
        }

        editDialogBinding.btnSave.setOnClickListener {
            profileViewModel.newUsername = editDialogBinding.edtNewUsername.text.toString()
            if (profileViewModel.selectedImageUri == null && profileViewModel.newUsername.isNullOrEmpty()) {

            } else if (profileViewModel.newUsername.isNullOrEmpty()) {
                profileViewModel.putAvatar()
            }
            else if (profileViewModel.selectedImageUri == null) {
                profileViewModel.putUsername()
            }
            else {
                profileViewModel.putData()
            }
        }

        internetDialogBinding.btnRetry.setOnClickListener {
            homeViewModel.getData(internetDialog)
            calendarViewModel.getDailyQuestionCard()
        }
    }

    override fun bindData() {
        val homeDialog = Dialog(this)
        val calendarDialog = Dialog(this)

        homeViewModel.isLoading.observe(this) {
            if (it) {
                homeDialog.startLoading(false)
            } else {
                homeDialog.dismiss()
            }
        }

        calendarViewModel.isLoading.observe(this) {
            if (it) {
                calendarDialog.startLoading(false)
            } else {
                calendarDialog.dismiss()
            }
        }

        profileViewModel.isLoading.observe(this) {
            if (it) {
                homeDialog.startLoading(false)
            } else {
                homeDialog.dismiss()
            }
        }

        homeViewModel.user.observe(this) {
            if (it != null) {
                setUserInformation(it)
                profileViewModel.email = it.email
            }
        }

        calendarViewModel.listQuestionCardDaily.observe(this) {
            var count = 0
            val curDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            it.forEach { daily ->
                count += daily.cards.size
                if (daily.day == curDay)
                    binding.txtTotalCardToday.text = "${daily.cards.size} questions."
            }
            binding.txtTotalCardLastMonth.text = "$count questions."
        }

        profileViewModel.status.observe(this) {
            if (it) {
                Toast.makeText(this, "Update profile successful!", Toast.LENGTH_LONG).show()
                editDialog.dismiss()
            }
            else Toast.makeText(this, "Update profile failed!", Toast.LENGTH_LONG).show()
            homeViewModel.getData(internetDialog)
        }
    }

    private fun setUserInformation(user: User) {
        Glide.with(binding.imgAvatar.context).load(user.avatar).into(binding.imgAvatar)
        binding.txtUsername.text = user.name
        binding.txtEmail.text = user.email
    }

    private fun initDialog() {
        editDialog.setContentView(editDialogBinding.root)
        editDialog.window?.apply {
            attributes.apply {
                gravity = Gravity.CENTER
                windowAnimations = R.style.pauseDialogAnimation
            }
            setDimAmount(0.5F)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        internetDialog.setContentView(internetDialogBinding.root)
        internetDialog.setCancelable(false)
        internetDialog.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            attributes.apply {
                gravity = Gravity.CENTER
            }
            setDimAmount(0.5F)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun imageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, SELECT_PICTURE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && data.data != null && resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            profileViewModel.selectedImageUri = data.data!!
            editDialogBinding.imgNewAvatar.setImageURI(profileViewModel.selectedImageUri)
        }
    }
}