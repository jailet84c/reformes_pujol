package com.reformespujol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Campings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campings)

        setSupportActionBar(findViewById(R.id.toolbar))

        val ivgualba = findViewById<ImageView>(R.id.ivGualba)
        val logorocagrossa = findViewById<ImageView>(R.id.logorocagrossa)
        val logoberga = findViewById<ImageView>(R.id.logobergaresort)
        val logokanguro = findViewById<ImageView>(R.id.logokanguro)
        val logojosep = findViewById<ImageView>(R.id.logomasjosep)
        val logotossa = findViewById<ImageView>(R.id.logotossa)

        ivgualba.setOnClickListener { obrirGualba() }
        logorocagrossa.setOnClickListener { obrirRocaGrossa() }
        logoberga.setOnClickListener { obrirBerga() }
        logokanguro.setOnClickListener { obrirKanguro() }
        logojosep.setOnClickListener { obrirJosep() }
        logotossa.setOnClickListener { obrirTossa() }
    }

    private fun obrirTossa() {
        val intentoT = Intent(this, Tossa::class.java)
        startActivity(intentoT)
    }

    private fun obrirJosep() {
        val intentoJ = Intent(this, Josep::class.java)
        startActivity(intentoJ)
    }

    private fun obrirKanguro() {
        val intentoK = Intent(this, Kanguro::class.java)
        startActivity(intentoK)
    }

    private fun obrirBerga() {
        val intentoB = Intent(this, Berga::class.java)
        startActivity(intentoB)
    }

    private fun obrirRocaGrossa() {
        val intentoR = Intent(this, Roca::class.java)
        startActivity(intentoR)
    }

    private fun obrirGualba() {
        val intentoG = Intent(this, Gualba::class.java)
        startActivity(intentoG)
    }
}