package com.reformespujol

import Imatge
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList


class ActivityDetall : AppCompatActivity() {

    lateinit var viewPager : ViewPager2
    val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detall)

        setSupportActionBar(findViewById(R.id.toolbar))

        viewPager = findViewById(R.id.view_pager)

        val viewPagerAdapter = DetallAdapter()
       
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.guardarImatge -> {
                val dialog = AlertDialog.Builder(this)
                        .setTitle("Descarregar imatge")
                        .setMessage("Vols descarregar l'imatge?")
                        .setNegativeButton(android.R.string.cancel) { view, _ ->
                            Toast.makeText(this, "Boto d'anular pressionat", Toast.LENGTH_SHORT).show()
                            view.dismiss()
                        }
                        .setPositiveButton(android.R.string.yes) { view, _ ->
                            checkExternalStoragePermission()
                            guardarImatge()
                            view.dismiss()
                        }
                        .setCancelable(false)
                        .create()
                dialog.show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
    private fun guardarImatge() {
        //rebre url de foto seleccionada storage
        val resulturl : Bundle? = this.intent.extras
        val resultatcamping: String? = resulturl?.getString("url")

        val storageRef = storage.reference
        var fotosRef = storageRef.child("Fotos Clients").child(resultatcamping!!)
           //Recullir Bitmap per descarregar foto
            Picasso.with(applicationContext)
                    .load(resultatcamping)
                    .into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            val filename = "${System.currentTimeMillis()}.jpg"
                            var fos : OutputStream? = null
                            val ubicacioArchiu = Environment.DIRECTORY_PICTURES + File.separator + "Reformes Pujol/Feines"
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                contentResolver?.also { resolver ->
                                    val contentValues = ContentValues().apply {
                                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                                        put(MediaStore.MediaColumns.RELATIVE_PATH, ubicacioArchiu  )
                                    }
                                    val imageUri : Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                                    fos = imageUri?.let { resolver.openOutputStream(it) }
                                }
                            } else {
                                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                val image = File(imagesDir, filename)
                                fos = FileOutputStream(image)
                            }
                            fos?.use {
                                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                                Toast.makeText(applicationContext, "Imatge guardada", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onBitmapFailed(errorDrawable: Drawable?) {
                            Toast.makeText(applicationContext, "Algo a fallat", Toast.LENGTH_LONG).show()
                        }
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            Toast.makeText(applicationContext, "Descarregant...", Toast.LENGTH_LONG).show()
                        }
                    })
    }
    private fun checkExternalStoragePermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No hi ha permis per llegir")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 225)
        } else {
            Log.i("Mensaje", "Hi ha permis per llegir!")
        }
    }
}

class DetallAdapter : RecyclerView.Adapter<DetallViewHolder>() {

    lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetallViewHolder {
        return DetallViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false))
    }

    override fun onBindViewHolder(holder: DetallViewHolder, position: Int) {

        val itemDetall = fotosClients[position]
        holder.bind(itemDetall)
    }

    override fun getItemCount(): Int = fotosClients.size
}

class DetallViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val fotoDetallclient = view.findViewById(R.id.fotoDetallclient) as TouchImageView

    fun bind(llistaf : Imatge) {

        fotoDetallclient.loadUrlDetall(llistaf.imatgeGaleria)
    }

    private fun ImageView.loadUrlDetall(urldos: String) {

        if (urldos.isEmpty()) {

            fotoDetallclient.setImageResource(R.drawable.appicono_foreground)

        } else {

            Picasso.with(context).load(urldos).into(fotoDetallclient)
        }
    }
}




