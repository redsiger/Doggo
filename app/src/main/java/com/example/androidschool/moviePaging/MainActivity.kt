package com.example.androidschool.moviePaging

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
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

class MainActivity : AppCompatActivity() {

    lateinit var mNavController : NavController
    private var _binding : ActivityMainBinding? = null
    val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)

        val imgClickListener = View.OnClickListener {
            val builder = NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("movie?.title")
                .setContentText("movie?.originalTitle")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                    .bigText("Expanded text"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)

            val notification: Notification = builder.build()
            with(NotificationManagerCompat.from(this)) {
                notify(0, notification)
            }
            Log.e("notification", "Click")
        }

        mBinding.mainToolbar.setOnClickListener(imgClickListener)
//        mNavController.addOnDestinationChangedListener { controller, destination, arguments ->
//            if (destination.id == R.id.movieDetailsFragment) {
//                actionBar?.hide()
//            } else {
//                actionBar?.show()
//            }
//        }

        mBinding.bottomNav.setupWithNavController(mNavController)
        setSupportActionBar(mBinding.mainToolbar)
        setupActionBarWithNavController(mNavController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}