package com.example.practicefortoeictestrebuild

import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.practicefortoeictestrebuild.base.BaseActivity
import com.example.practicefortoeictestrebuild.databinding.ActivityMainBinding
import com.simform.custombottomnavigation.Model

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNavigationWithNavController(savedInstanceState)
        setDrawerNavigationWithNavController()
    }

    override fun initData() {

    }

    override fun handleEvent() {

    }

    override fun bindData() {

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