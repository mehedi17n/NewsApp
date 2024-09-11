package com.example.newsapp.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.newsapp.BookmarkFragment
import com.example.newsapp.HomeFragment
import com.example.newsapp.NotificationFragment
import com.example.newsapp.ProfileFragment
import com.example.newsapp.R
import com.example.newsapp.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var activeFragment: Fragment? = null
    private lateinit var lottieProgressBar: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        // Load the default fragment (Home)
        if (savedInstanceState == null) {
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, homeFragment)
                .commit()
            activeFragment = homeFragment
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        bottomNav.setOnItemSelectedListener { item ->
            val newFragment: Fragment = when (item.itemId) {
                R.id.home -> HomeFragment()
                R.id.bookmark -> BookmarkFragment()
                R.id.search -> SearchFragment()
                R.id.notification -> NotificationFragment()
                R.id.profile -> ProfileFragment()
                else -> return@setOnItemSelectedListener false
            }

            // Replace fragment only if the new fragment is different
            if (newFragment::class != activeFragment!!::class) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayout, newFragment)
                    commit()
                }
                activeFragment = newFragment
            } else {
                Toast.makeText(this, "Already on the selected page", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}
