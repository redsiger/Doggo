package com.example.androidschool.moviePaging

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidschool.moviePaging.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
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
        setContentView(mBinding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController
//        mToolbar = mBinding.mainToolbar
//        mToolbar.setTitleTextAppearance(this, R.style.style_toolbar_title)
//
        mBinding.bottomNav.setupWithNavController(mNavController)
//
//        setSupportActionBar(mToolbar)
//        setupActionBarWithNavController(mNavController)
    }

    fun showSnackbar(movieTitle: String) {
        val snackbar = Snackbar.make(mBinding.root, "$movieTitle deleted", Snackbar.LENGTH_SHORT)
//        snackbar.anchorView = mBinding.bottomNav
        snackbar.show()
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