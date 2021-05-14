package com.matvey.newsapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.matvey.newsapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.fragment_container,
                            NewsListFragment.newInstance(),
                            "tag"
                    )
                    .commit()
        }
    }
}