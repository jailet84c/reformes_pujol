package com.reformespujol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Tossa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tossa)

        setSupportActionBar(findViewById(R.id.toolbar))
        val mapaTossa = findViewById<TouchImageView>(R.id.mapaTossa)
    }
}