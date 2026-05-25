package com.example.watertracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LifecycleCheck"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: Activity Dibuat")

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_stats -> {
                    replaceFragment(StatsFragment())
                    true
                }
                R.id.nav_reminder -> {
                    replaceFragment(ReminderFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_home
            replaceFragment(HomeFragment())
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity Terlihat")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity Interaktif (Running)")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity Dijeda (Sebagian tertutup)")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity Berhenti (Tidak terlihat)")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity Dihancurkan")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Activity Dijalankan Kembali")
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}