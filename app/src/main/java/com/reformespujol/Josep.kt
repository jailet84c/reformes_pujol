package com.reformespujol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Josep : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_josep)

        setSupportActionBar(findViewById(R.id.toolbar))
        val mapaJosep = findViewById<TouchImageView>(R.id.mapaJosep)
    }
}
