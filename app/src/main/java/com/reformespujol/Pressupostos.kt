package com.reformespujol

import Client
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class Pressupostos : AppCompatActivity() {

    var etcampingpre: TextView? = null
    var etdatapre: TextView? = null
    var etnompre: TextView? = null
    var ettlfpre: TextView? = null
    var etconcepte: TextView? = null
    var etnumerospre: TextView? = null
    var ettotaliva: TextView? = null
    var etpreutotal: TextView? = null

    private var pressRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pressupostos)
        setSupportActionBar(findViewById(R.id.toolbar))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        etcampingpre = findViewById(R.id.etCampingP)
        etdatapre = findViewById(R.id.etDataP)
        etnompre = findViewById(R.id.etNomP)
        ettlfpre = findViewById(R.id.ettlfpre)
        etconcepte = findViewById(R.id.etConceptepre)
        etnumerospre = findViewById(R.id.etnumerospre)
        ettotaliva = findViewById(R.id.etTotalIvapreFet)
        etpreutotal = findViewById(R.id.etTotalpreFet)

        //Resultat Pressupost desde Activity ClientDetall
        val recibirclient = intent
        //Resultat Pressupost desde Activity Feines Pendents
        val recibirclientFP = intent

        val datosClient = recibirclient.getStringExtra("PressupostDades")
        val datosClientFP = recibirclientFP.getStringExtra("dadesFP")

        if (datosClient == null && datosClientFP == null) {
            rebrePressupost() // Clicat desde RecyclerViewFP
        } else if (datosClientFP == null && datosClient != null) {
            rebrePressupostClient(datosClient) //Rebut desde l Activity ClientDetall
        }

        pressRef = FirebaseDatabase.getInstance().getReference("pressupostos")

    }

    private fun rebrePressupostClient(infoClient: String?) {
        pressRef = FirebaseDatabase.getInstance().getReference("pressupostos").child(infoClient!!)
        val rebreClient = object : ValueEventListener {       // Rebre info client
            override fun onDataChange(snapshot: DataSnapshot) {

                val clientPressupost = snapshot.getValue(Client::class.java)

                etcampingpre!!.text = clientPressupost?.camping
                etdatapre!!.text = clientPressupost?.data
                etnompre!!.text = clientPressupost?.nom
                ettlfpre!!.text = clientPressupost?.telefon
                etconcepte?.text = clientPressupost?.concepte
                etnumerospre?.text = clientPressupost?.n1
                ettotaliva?.text = clientPressupost?.preutiva
                etpreutotal!!.text = clientPressupost?.preutotal

            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        pressRef?.addValueEventListener(rebreClient)        // Rebre info pressupost

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menupressupost, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actualitzarPressupost -> {
                actualitzarPressupost()
                return true
            }
            R.id.guardarpressupost -> {
                saveClick()
                return true
            }
            R.id.veureFeina -> {
                veureFeina()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun veureFeina() {

        val idClient = etcampingpre!!.text.toString()
        val intentVeureFeina = Intent(this, ClientDetall::class.java).apply {
            putExtra("clientDades", idClient)
        }
        startActivity(intentVeureFeina)
    }

    private fun actualitzarPressupost() {
        val pressupostId = etcampingpre!!.text.toString()
        val pressupostrenovat = Client(
                pressupostId,
                etcampingpre?.text.toString(),
                etdatapre?.text.toString(),
                etnompre?.text.toString(),
                ettlfpre?.text.toString(),
                etconcepte?.text.toString(),
                etnumerospre?.text.toString(),
                ettotaliva?.text.toString(),
                etpreutotal?.text.toString())

        pressRef!!.child(pressupostId).setValue(pressupostrenovat)
        adapterPressupost.notifyDataSetChanged()
        finish()
    }
    private fun rebrePressupost() {

        val resultID: Bundle? = this.intent.extras
        val idPressupost: String? = resultID?.getString("ID")
        pressRef = FirebaseDatabase.getInstance().getReference("pressupostos").child(idPressupost!!)
        val rebrePress = object : ValueEventListener {       // Rebre info pressupost
            override fun onDataChange(snapshot: DataSnapshot) {

                val pressu = snapshot.getValue(Client::class.java)

                etcampingpre!!.text = pressu?.camping
                etdatapre!!.text = pressu?.data
                etnompre!!.text = pressu?.nom
                ettlfpre!!.text = pressu?.telefon
                etconcepte?.text = pressu?.concepte
                etnumerospre?.text = pressu?.n1
                ettotaliva?.text = pressu?.preutiva
                etpreutotal!!.text = pressu?.preutotal

            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        pressRef?.addValueEventListener(rebrePress)        // Rebre info pressupost
    }
    private fun getBitmapFromView(view: View): Bitmap? {

        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888) //Define a bitmap with the same size as the view
        val canvas = Canvas(returnedBitmap) //Bind a canvas to it
        val bgDrawable = view.background  //Get the view's background
        if (bgDrawable != null) { //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE) //does not have background drawable, then draw white background on the canvas
        }
        view.draw(canvas) // draw the view on the canvas
        return returnedBitmap //return the bitmap
    }
    private fun saveBitMap(context: Context, drawView: View) {
        val filenamePre = "${System.currentTimeMillis()}.jpg"
        var fosdos: OutputStream? = null
        val ubicacioArchiuPre = Environment.DIRECTORY_PICTURES + File.separator + "Reformes Pujol/Pressupostos"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filenamePre)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, ubicacioArchiuPre)
                }
                val imageUri : Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fosdos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filenamePre)
            fosdos = FileOutputStream(image)
        }
        fosdos?.use {
            val bitmap = getBitmapFromView(drawView)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    private fun saveClick() {
        val savingLayout = findViewById<View>(R.id.pressupostLayout) as ConstraintLayout
        val file = saveBitMap(this@Pressupostos, savingLayout)
        Toast.makeText(this, "Foto guardada!", Toast.LENGTH_SHORT).show()
    }
}



