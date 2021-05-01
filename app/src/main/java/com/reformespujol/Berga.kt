package com.reformespujol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Berga : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berga)

        setSupportActionBar(findViewById(R.id.toolbar))
        val mapaBerga = findViewById<TouchImageView>(R.id.mapaBerga)
    }
}
