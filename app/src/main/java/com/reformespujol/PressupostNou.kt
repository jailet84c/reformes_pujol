
package com.reformespujol

import Client
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.*

class PressupostNou : AppCompatActivity() {

    private var pressupostosList: ArrayList<Client> = ArrayList()
    private val adapterPressupost = PressupostAdapter(pressupostosList)

    private var etcampingprenou: TextView? = null
    private var etdataprenou: TextView? = null
    private var etnomprenou: TextView? = null
    private var ettelefonnou: TextView? = null
    private var etconceptenou: TextView? = null
    private var etnumeros: TextView? = null
    private var ettotalivanou: TextView? = null
    private var etpreutotalnou: TextView? = null

    private var btGuardarnou: Button? = null

    private var pressRefnou: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pressupostnou)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        etcampingprenou = findViewById(R.id.nouetCampingP)
        etdataprenou = findViewById(R.id.nouetData)
        etnomprenou = findViewById(R.id.nouetNomP)
        ettelefonnou = findViewById(R.id.nouettlfpre)
        etconceptenou = findViewById(R.id.nouetConceptepre)
        etnumeros = findViewById(R.id.nouetnumerospre)
        ettotalivanou = findViewById(R.id.nouetTotalIvapre)
        etpreutotalnou = findViewById(R.id.nouetTotalpre)
        btGuardarnou = findViewById(R.id.noubtGuardar)

        pressRefnou = FirebaseDatabase.getInstance().getReference("pressupostos")
        btGuardarnou!!.setOnClickListener { guardarPressupost() }

    }

    private fun guardarPressupost() {

        val pressupostId = etcampingprenou!!.text.toString()
        val pressupost = Client(pressupostId,
            etcampingprenou!!.text.toString().trim(),
            etdataprenou!!.text.toString().trim(),
            etnomprenou!!.text.toString().trim(),
            ettelefonnou!!.text.toString().trim(),
            etconceptenou!!.text.toString(),
            etnumeros!!.text.toString().trim(),
            ettotalivanou!!.text.toString().trim(),
            etpreutotalnou!!.text.toString().trim())

        if (etcampingprenou!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Posa algo a concepte camping!", Toast.LENGTH_SHORT).show()
        } else {
            pressRefnou!!.child(pressupostId).setValue(pressupost)
            adapterPressupost.notifyDataSetChanged()
            finish()
        }
    }
}