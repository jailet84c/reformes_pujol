package com.reformespujol

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val btfeines = findViewById<Button>(R.id.btfeines)
        val btCampings = findViewById<Button>(R.id.btCampings)
        val btPressupostos = findViewById<Button>(R.id.btPressupostos)
        val btMaterials = findViewById<Button>(R.id.btMaterials)
        val instagramlogo = findViewById<ImageView>(R.id.instagramiv)
        val botoweb = findViewById<ImageView>(R.id.ivpagina)


        btfeines.setOnClickListener { obrirFeines() }
        btCampings.setOnClickListener { obrirCampings() }
        btPressupostos.setOnClickListener { obrirPressupostos() }
        btMaterials.setOnClickListener { obrirMaterials() }
        instagramlogo.setOnClickListener { obrirInstagram() }
        botoweb.setOnClickListener { obrirweb() }
    }

    private fun obrirweb() {
        val url : String = "http://www.reformespujol.com/"
        val uri : Uri = Uri.parse(url)
        val intentWeb = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intentWeb)

    }

    private fun obrirMaterials() {
        val intentM = Intent(this, MaterialPendent::class.java)
        startActivity(intentM)
    }

    private fun obrirInstagram() {
        val uri = Uri.parse("http://instagram.com/_u/reformescamping_pujol")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)

            startActivity(likeIng, null)

    }

    private fun obrirPressupostos() {
        val intent = Intent(this, escollirPressupostosC::class.java)
        startActivity(intent)
    }

    private fun obrirCampings() {
        val intentc = Intent(this, Campings::class.java)
        startActivity(intentc)
    }

    private fun obrirFeines() {
        val intent = Intent(this, EscollirTiposFeina::class.java)
        startActivity(intent)
    }
}