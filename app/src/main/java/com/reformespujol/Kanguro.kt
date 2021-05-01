package com.reformespujol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Kanguro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanguro)

        setSupportActionBar(findViewById(R.id.toolbar))
        val mapaKanguro = findViewById<TouchImageView>(R.id.mapaKanguro)
    }
}
