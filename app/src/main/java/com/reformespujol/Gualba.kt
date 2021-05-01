package com.reformespujol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Gualba : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gualba)

        setSupportActionBar(findViewById(R.id.toolbar))
        val mapaGualba = findViewById<TouchImageView>(R.id.mapaGualba)
    }
}