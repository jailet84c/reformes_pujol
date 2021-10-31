package com.reformespujol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class escollirPressupostosC : AppCompatActivity() {

    private var presspenRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.escollirpressupostos)

        setSupportActionBar(findViewById(R.id.toolbar))

        val btEscollir = findViewById<Button>(R.id.btPressupost)
        val etpressp = findViewById<EditText>(R.id.etPress)
        val btPressGuardar = findViewById<Button>(R.id.btGuardarPressP)

        presspenRef = FirebaseDatabase.getInstance().getReference("Pressupost Pendent")

        btEscollir.setOnClickListener { obrirPressupostos() }

        btPressGuardar.setOnClickListener {

            val textPressPe: String = etpressp.text.toString().trim()
            presspenRef!!.setValue(textPressPe)
            Toast.makeText(this,"Pressupostos guardats",Toast.LENGTH_SHORT).show()
            finish()
        }

        val rebrePress = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val textPressPen: String? = snapshot.getValue(String::class.java)
                    etpressp.setText(textPressPen)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

            presspenRef!!.addValueEventListener(rebrePress)
        }


    private fun obrirPressupostos() {
        val intentObrir = Intent(this, LListaPressupostos::class.java)
        startActivity(intentObrir)
    }
}