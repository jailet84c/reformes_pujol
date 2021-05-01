
package com.reformespujol

import Pressupost
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.*
import java.text.DecimalFormat

class PressupostNou : AppCompatActivity() {

    var pressupostosList: ArrayList<Pressupost> = ArrayList()
    val adapterPressupost = PressupostAdapter(pressupostosList)

    var etdataprenou: TextView? = null
    var etcampingprenou: TextView? = null
    var etnomprenou: TextView? = null
    var ettelefonnou: TextView? = null
    var etconceptenou: TextView? = null
    var etnumero1nou: TextView? = null
    var etnumero2nou: TextView? = null
    var etnumero3nou: TextView? = null
    var etnumero4nou: TextView? = null
    var etnumero5nou: TextView? = null
    var etnumero6nou: TextView? = null
    var etnumero7nou: TextView? = null
    var ettotalivanou: TextView? = null
    var etpreutotalnou: TextView? = null
    var btGuardarnou: Button? = null

    private var pressRefnou: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pressupostnou)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        etdataprenou = findViewById(R.id.nouetData)
        etcampingprenou = findViewById(R.id.nouetCampingP)
        etnomprenou = findViewById(R.id.nouetNomP)
        ettelefonnou = findViewById(R.id.ettlfpre)
        etconceptenou = findViewById(R.id.nouetConcepte1)
        etnumero1nou = findViewById(R.id.nouetnumero1)
        etnumero2nou = findViewById(R.id.nouetnumero2)
        etnumero3nou = findViewById(R.id.nouetnumero3)
        etnumero4nou = findViewById(R.id.nouetnumero4)
        etnumero5nou = findViewById(R.id.nouetnumero5)
        etnumero6nou = findViewById(R.id.nouetnumero6)
        etnumero7nou = findViewById(R.id.nouetnumero7)
        ettotalivanou = findViewById(R.id.nouetTotalIva)
        etpreutotalnou = findViewById(R.id.nouetTotal)

        btGuardarnou = findViewById<Button>(R.id.noubtGuardar)

        btGuardarnou!!.setOnClickListener { guardarPressupost() }

        pressRefnou = FirebaseDatabase.getInstance().getReference("pressupostos")
    }

    private fun guardarPressupost() {

        val pressupostId = etcampingprenou!!.text.toString()
        val pressupost = Pressupost(pressupostId,
                etdataprenou!!.text.toString().trim(),
                etcampingprenou!!.text.toString().trim(),
                etnomprenou!!.text.toString().trim(),
                ettelefonnou!!.text.toString().trim(),
                etconceptenou!!.text.toString().trim(),
                etnumero1nou!!.text.toString().trim(),
                etnumero2nou!!.text.toString().trim(),
                etnumero3nou!!.text.toString().trim(),
                etnumero4nou!!.text.toString().trim(),
                etnumero5nou!!.text.toString().trim(),
                etnumero6nou!!.text.toString().trim(),
                etnumero7nou!!.text.toString().trim(),
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