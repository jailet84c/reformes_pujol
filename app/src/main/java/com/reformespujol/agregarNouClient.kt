package com.reformespujol

import Client
import ClientPendent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class agregarNouClient : AppCompatActivity() {

    private lateinit var etcamping : EditText
    private lateinit var etnom : EditText
    private lateinit var ettelefon : EditText
    private lateinit var etpreu : EditText
    private lateinit var etparcial : EditText
    private lateinit var etfeina : EditText

    private lateinit var btFeinaFeta : Button
    private lateinit var btFeinaPerFer : Button

    lateinit var clientsRef: DatabaseReference
    lateinit var clientsPendentsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nou_client)

        setSupportActionBar(findViewById(R.id.toolbar))

        etcamping = findViewById(R.id.etcamping)
        etnom = findViewById(R.id.etnom)
        ettelefon = findViewById(R.id.ettelefon)
        etpreu = findViewById(R.id.etpreu)
        etparcial = findViewById(R.id.etparciala)
        etfeina = findViewById(R.id.etfeina)
        btFeinaFeta = findViewById(R.id.btFeinaAcabada)
        btFeinaPerFer = findViewById(R.id.btFeinaPerFer)

        clientsRef = FirebaseDatabase.getInstance().getReference("clients")
        clientsPendentsRef = FirebaseDatabase.getInstance().getReference("clientsPendents")

        btFeinaFeta.setOnClickListener { agregarNCFirebase() }
        btFeinaPerFer.setOnClickListener { agregarFeinaPerFer() }

    }

    private fun agregarFeinaPerFer() {

        val camping : String = etcamping.text.toString().trim()
        val nom : String = etnom.text.toString().trim()
        val telefon : String = ettelefon.text.toString().trim()
        val preu : String = etpreu.text.toString().trim()
        val parcial : String = etparcial.text.toString().trim()
        val feina : String = etfeina.text.toString().trim()

        val sTD = ClientPendent(camping, camping,"", nom, telefon,"","","", preu,parcial, feina)

        clientsPendentsRef.child(camping).setValue(sTD)
        mAdapter.notifyDataSetChanged()
        finish()

    }

    private fun agregarNCFirebase() {

        val camping : String = etcamping.text.toString().trim()
        val nom : String = etnom.text.toString().trim()
        val telefon : String = ettelefon.text.toString().trim()
        val preu : String = etpreu.text.toString().trim()
        val parcial : String = etparcial.text.toString().trim()
        val feina : String = etfeina.text.toString().trim()

        val sTD = Client(camping, camping,"", nom, telefon,"","","", preu,parcial, feina)

        clientsRef.child(camping).setValue(sTD)
        mAdapter.notifyDataSetChanged()
        finish()
    }
}