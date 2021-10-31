package com.reformespujol

import Client
import Imatge
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.R.attr.data
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference

class VisorPressupost : AppCompatActivity() {

    val PRESS_IMAGE = 0

    var textCamping: TextView? = null
    var textData: TextView? = null
    var textNom: TextView? = null
    var textTelefon: TextView? = null
    var textPreu: TextView? = null
    var foto: ImageView? = null

    private var pressRef: DatabaseReference? = null
    private var storageFotoPre : StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visorpressupost)

        textCamping = findViewById(R.id.textCamping)
        textData = findViewById(R.id.textData)
        textNom = findViewById(R.id.textNom)
        textTelefon = findViewById(R.id.textTelefon)
        textPreu = findViewById(R.id.textPreu)
        foto = findViewById(R.id.imatgePre)

        setSupportActionBar(findViewById(R.id.toolbar))

        val floatingfoto = findViewById<View>(R.id.butoFotoPres) as FloatingActionButton

        val resultID: Bundle? = this.intent.extras
        val idPressupost: String? = resultID?.getString("ID")

        rebrePressupost(idPressupost)

        pressRef = FirebaseDatabase.getInstance().getReference("pressupostos")



        floatingfoto.setOnClickListener { obrirGalFotoPres() }
    }

    private fun rebrePressupost(idPressupost: String?) {
        pressRef = FirebaseDatabase.getInstance().getReference("pressupostos").child(idPressupost!!)
        val rebrePress = object : ValueEventListener {       // Rebre info pressupost
            override fun onDataChange(snapshot: DataSnapshot) {

                val pressu = snapshot.getValue(Client::class.java)

                textCamping!!.text = pressu?.camping
                textData!!.text = pressu?.data
                textNom!!.text = pressu?.nom
                textTelefon!!.text = pressu?.telefon
                textPreu?.text = pressu?.preutotal
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        pressRef?.addValueEventListener(rebrePress)        // Rebre info pressupost

        val campingup: String = idPressupost
        val storage = FirebaseStorage.getInstance()
        storageFotoPre = storage.reference.child("Fotos Pressupostos").child(campingup)

          item.downloadUrl.addOnSuccessListener {      // Mostrar imatges Firebase Storage

                    foto!!.setImageURI(imagePresUri)

                }.addOnCompleteListener {
                }
            }
        }
    }

    private fun obrirGalFotoPres() {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PRESS_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PRESS_IMAGE) {
            if (data?.data != null) {

                val nomPressupost : String = textCamping?.text.toString() // Posar textView.text... per que agafi nom
                val imagePresUri: Uri? = data.data
                storageFotoPre = FirebaseStorage.getInstance().reference.child("Fotos Pressupostos")
                val filePathPre: StorageReference = storageFotoPre!!.child(nomPressupost)
                filePathPre.putFile(imagePresUri!!).addOnSuccessListener {
                    Toast.makeText(this@VisorPressupost, "Foto pressupost correctament", Toast.LENGTH_SHORT).show()
                }
                foto!!.setImageURI(imagePresUri)

                }
        }
    }
}