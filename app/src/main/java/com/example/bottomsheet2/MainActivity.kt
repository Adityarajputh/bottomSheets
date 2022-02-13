package com.example.bottomsheet2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportFragmentManager.beginTransaction()
            .replace(R.id.app_container,BlankFragment())
            .commit()
    }
}