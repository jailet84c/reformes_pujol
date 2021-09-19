package com.reformespujol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EscollirTiposFeina : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escollir_tipos_feina)

        setSupportActionBar(findViewById(R.id.toolbar))

        val btfeinesafer = findViewById<Button>(R.id.feinesafer)
        val btfeinesacabades = findViewById<Button>(R.id.feinesacabades)

        btfeinesafer.setOnClickListener { feinesAFer() }
        btfeinesacabades.setOnClickListener { feinesAcabades() }
    }

    private fun feinesAcabades() {
        val intentFacab = Intent(this, Feines::class.java)
        startActivity(intentFacab)
    }

    private fun feinesAFer() {
        val intentFaF = Intent(this, FeinesPendents::class.java)
        startActivity(intentFaF)
    }
}