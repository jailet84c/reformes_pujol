package com.reformespujol

import Client
import Pressupost
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

    var etdatapre: TextView? = null
    var etcampingpre: TextView? = null
    var etnompre: TextView? = null
    var ettlfpre: TextView? = null
    var etconcepte: TextView? = null
    var etnumero1: TextView? = null
    var etnumero2: TextView? = null
    var etnumero3: TextView? = null
    var etnumero4: TextView? = null
    var etnumero5: TextView? = null
    var etnumero6: TextView? = null
    var etnumero7: TextView? = null
    var ettotaliva: TextView? = null
    var etpreutotal: TextView? = null

    private var pressRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pressupostos)
        setSupportActionBar(findViewById(R.id.toolbar))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        etdatapre = findViewById(R.id.etData)
        etcampingpre = findViewById(R.id.etCampingP)
        etnompre = findViewById(R.id.etNomP)
        ettlfpre = findViewById(R.id.ettlfpre)
        etconcepte = findViewById(R.id.etConcepte1)
        etnumero1 = findViewById(R.id.etnumero1)
        etnumero2 = findViewById(R.id.etnumero2)
        etnumero3 = findViewById(R.id.etnumero3)
        etnumero4 = findViewById(R.id.etnumero4)
        etnumero5 = findViewById(R.id.etnumero5)
        etnumero6 = findViewById(R.id.etnumero6)
        etnumero7 = findViewById(R.id.etnumero7)
        ettotaliva = findViewById(R.id.etTotalIva)
        etpreutotal = findViewById(R.id.etTotal)

        rebrePressupost()

        pressRef = FirebaseDatabase.getInstance().getReference("pressupostos")

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


    }

    private fun actualitzarPressupost() {
        val pressupostId = etcampingpre!!.text.toString()
        val pressupostrenovat = Pressupost(
                pressupostId,
                etdatapre?.text.toString(),
                etcampingpre?.text.toString(),
                etnompre?.text.toString(),
                ettlfpre?.text.toString(),
                etconcepte?.text.toString(),
                etnumero1?.text.toString(),
                etnumero2?.text.toString(),
                etnumero3?.text.toString(),
                etnumero4?.text.toString(),
                etnumero5?.text.toString(),
                etnumero6?.text.toString(),
                etnumero7?.text.toString(),
                ettotaliva?.text.toString(),
                etpreutotal?.text.toString()
        )
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
                val pressu = snapshot.getValue(Pressupost::class.java)

                etdatapre!!.text = pressu?.datapre
                etcampingpre!!.text = pressu?.campingpre
                etnompre!!.text = pressu?.nompre
                ettlfpre!!.text = pressu?.telefon
                etpreutotal!!.text = pressu?.preutotal.toString()
                etconcepte?.text = pressu?.conceptepre
                etnumero1?.text = pressu?.n1.toString()
                etnumero2?.text = pressu?.n2.toString()
                etnumero3?.text = pressu?.n3.toString()
                etnumero4?.text = pressu?.n4.toString()
                etnumero5?.text = pressu?.n5.toString()
                etnumero6?.text = pressu?.n6.toString()
                etnumero7?.text = pressu?.n7.toString()
                ettotaliva?.text = pressu?.preutiva.toString()

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



