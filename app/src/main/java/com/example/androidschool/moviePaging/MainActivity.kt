package com.example.androidschool.moviePaging

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidschool.moviePaging.databinding.ActivityMainBinding
import com.example.androidschool.moviePaging.notifications.CHANNEL_1_ID
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var mNavController : NavController
    private var _binding : ActivityMainBinding? = null
    val mBinding get() = _binding!!
    lateinit var mToolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)
        mToolbar = mBinding.mainToolbar
        mToolbar.setTitleTextAppearance(this, R.style.style_toolbar_title)

        mBinding.bottomNav.setupWithNavController(mNavController)

        setSupportActionBar(mToolbar)
        setupActionBarWithNavController(mNavController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        finish()
        startActivity(intent)
    }
}