package com.example.practicefortoeictestrebuild.ui.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.base.BaseActivity
import com.example.practicefortoeictestrebuild.databinding.ActivityMainBinding
import com.example.practicefortoeictestrebuild.databinding.ViewDrawerNavHeaderBinding
import com.example.practicefortoeictestrebuild.model.User
import com.example.practicefortoeictestrebuild.ui.login.LoginActivity
import com.example.practicefortoeictestrebuild.utils.startLoading
import com.simform.custombottomnavigation.Model

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private val loadingDialog by lazy { Dialog(this) }

    private val internetDialog by lazy { Dialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNavigationWithNavController(savedInstanceState)
        setDrawerNavigationWithNavController()
    }

    override fun initData() {
        MyApplication.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NGJkYmQ1NmI3OWVkOGRkNDNiMWQ5NDYiLCJpYXQiOjE2OTEyMzEzNTAsImV4cCI6MTY5MTgzNjE1MH0.3Q_aeLnbteLVd0cjnvmGaMC_6p5l5xvuFckP46EI0jo")
        if (MyApplication.getToken().isNullOrEmpty()) {
            logout()
        } else viewModel.getData(internetDialog)
    }

    override fun handleEvent() {

    }

    override fun bindData() {
        viewModel.isLoading.observe(this) {
            if (it) {
                loadingDialog.startLoading(false)
            } else {
                loadingDialog.dismiss()
            }
        }

        viewModel.user.observe(this) {
            if (it == null) {
                logout()
            } else {
                setUserInformation(it)
            }
        }
    }

    private fun logout() {
        MyApplication.clearToken()
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_zoom_out)
        finishAffinity()
    }

    private fun setUserInformation(user: User) {
        val headerBinding = ViewDrawerNavHeaderBinding.inflate(layoutInflater)
        Glide.with(headerBinding.imgAvatar.context).load(user.avatar).into(headerBinding.imgAvatar)
        headerBinding.txtName.text = user.name
        headerBinding.txtEmail.text = user.email
        binding.drawerView.addHeaderView(headerBinding.root)
    }

    private fun setBottomNavigationWithNavController(savedInstanceState: Bundle?) {

        val activeIndex = savedInstanceState?.getInt("activeIndex") ?: 1

        val navController = findNavController(R.id.nav_fragment)
        val menuItems = arrayOf(
            Model(
                R.drawable.ic_home,
                R.id.nav_home,
                1,
                R.string.home,
            ),
            Model(
                R.drawable.ic_category,
                R.id.nav_category,
                2,
                R.string.category
            )
        )

        binding.bottomNav.apply {
            setMenuItems(menuItems, activeIndex)
            setupWithNavController(navController)
        }
    }

    private fun setDrawerNavigationWithNavController() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_category),
            binding.drawerLayout
        )
        val navController = findNavController(R.id.nav_fragment)
        binding.drawerView.setupWithNavController(navController)

        binding.drawerView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {

                }
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.calendar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}