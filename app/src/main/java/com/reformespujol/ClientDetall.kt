package com.reformespujol

import Client
import Imatge
import RecyclerAdapterFP
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage.*
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

val fotosClients: MutableList<Imatge> = ArrayList()
lateinit var RecyclerDetall: RecyclerView
val adapterDetall = ImagesAdapter()

class ClientDetall : AppCompatActivity() {

    val PICK_IMAGE = 0

     var camping : TextView? = null
     var nom: TextView? = null
     var telefon: TextView? = null
     var feina: TextView? = null
     var parcial: TextView? = null
     var preu: TextView? = null

     private var mDatabaseRef: DatabaseReference? = null
     private var mStorageRef : StorageReference? = null

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_client_detall)
         setSupportActionBar(findViewById(R.id.toolbar))

         RecyclerDetall = findViewById(R.id.recyclerDetall)
         RecyclerDetall.setHasFixedSize(true)
         RecyclerDetall.layoutManager = GridLayoutManager(this, 3)

         val floatingfoto = findViewById<View>(R.id.floatingfoto) as FloatingActionButton

         camping = findViewById(R.id.tvdcamping)
         nom = findViewById(R.id.tvdnom)
         telefon = findViewById(R.id.tvtele)
         parcial = findViewById(R.id.tvdparcial)
         feina = findViewById(R.id.tvfeina)
         preu = findViewById(R.id.tvdpreu)

         //Resultat client desde Activity Pressupostos
         val recibir = intent
         var datos = recibir.getStringExtra("clientDades")
         if (datos == null) {
             rebreClient() // Clicat desde RecyclerViewFP
         } else {
             rebreClientPressupost(datos) //Rebut desde l Activity Pressupost
         }

        // rebreClient()

         mDatabaseRef = FirebaseDatabase.getInstance().getReference("clients") //Posar despres de rebreclient() si no es duplica...

         floatingfoto.setOnClickListener { obrirGaleria() }
     }

    private fun rebreClientPressupost(campingRebut: String?) { //prova

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("clients").child(campingRebut!!)
        val clientDetallPressupost = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val clientDetallInfoPressupost = snapshot.getValue(Client::class.java)
                camping!!.text = clientDetallInfoPressupost?.camping
                nom!!.text = clientDetallInfoPressupost?.nom
                telefon!!.text = clientDetallInfoPressupost?.telefon
                parcial!!.text = clientDetallInfoPressupost?.parcial
                feina!!.text = clientDetallInfoPressupost?.tipofeina
                preu!!.text = clientDetallInfoPressupost?.preutotal
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        mDatabaseRef!!.addValueEventListener(clientDetallPressupost)

        val campingupress: String = campingRebut
        val storagepres = getInstance()
        mStorageRef = storagepres.reference.child("Fotos Clients").child(campingupress)
        fotosClients.clear() // Evitar que es repeteixin les fotos

        val listAllTask: Task<ListResult> = mStorageRef!!.listAll()                // Mostrar imatges Firebase Storage

        listAllTask.addOnCompleteListener { result ->

            val items: List<StorageReference> = result.result!!.items

            items.forEachIndexed { _, item ->

                item.downloadUrl.addOnSuccessListener {

                    Log.d("item", "$it")

                    fotosClients.add(Imatge(it.toString()))

                }.addOnCompleteListener {

                    adapterDetall.ImagesAdapter(fotosClients, this@ClientDetall) //nessesari
                    RecyclerDetall.adapter = adapterDetall //nessesari
                    adapterDetall.notifyDataSetChanged()

                }
            }
        }                                                                          // Mostrar imatges Firebase Storage
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
             val inflater = menuInflater
             inflater.inflate(R.menu.menudetallclient, menu)
             return super.onCreateOptionsMenu(menu)
         }
         override fun onOptionsItemSelected(item: MenuItem): Boolean {
             when (item.itemId) {

                 R.id.actualitzarClient -> {
                     updateClient()
                     return true
                 }

                 R.id.veurepressupost -> {
                     veurePressupost()
                     return true
                 }
             }
             return super.onOptionsItemSelected(item)
         }

    private fun veurePressupost() {

        val idPress = camping!!.text.toString()
        val intentVeureFeina = Intent(this, Pressupostos::class.java).apply {
            putExtra("PressupostDades", idPress)
        }

        startActivity(intentVeureFeina)

    }

    private fun rebreClient() {

        val resultDades: Bundle? = this.intent.extras
        val idClientDetall: String? = resultDades?.getString("DADESCLIENT")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("clients").child(idClientDetall!!)
        val rebreClientDetall = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val clientDetallInfo = snapshot.getValue(Client::class.java)

                camping!!.text = clientDetallInfo?.camping
                nom!!.text = clientDetallInfo?.nom
                telefon!!.text = clientDetallInfo?.telefon
                parcial!!.text = clientDetallInfo?.parcial
                feina!!.text = clientDetallInfo?.tipofeina
                preu!!.text = clientDetallInfo?.preutotal
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        mDatabaseRef!!.addValueEventListener(rebreClientDetall)

        // val campingup: String = camping?.text.toString()
         val campingup: String = idClientDetall
         val storage = getInstance()
         mStorageRef = storage.reference.child("Fotos Clients").child(campingup)

         fotosClients.clear() // Evitar que es repeteixin les fotos

         val listAllTask: Task<ListResult> = mStorageRef!!.listAll()                // Mostrar imatges Firebase Storage

         listAllTask.addOnCompleteListener { result ->

             val items: List<StorageReference> = result.result!!.items

             items.forEachIndexed { _, item ->

                 item.downloadUrl.addOnSuccessListener {

                     Log.d("item", "$it")

                     fotosClients.add(Imatge(it.toString()))

                 }.addOnCompleteListener {

                     adapterDetall.ImagesAdapter(fotosClients, this@ClientDetall) //nessesari
                     RecyclerDetall.adapter = adapterDetall //nessesari
                     adapterDetall.notifyDataSetChanged()

                 }
             }
         }                                                                          // Mostrar imatges Firebase Storage
     }

    private fun obrirGaleria() {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)

    }

    private fun updateClient() {
        val clientId = camping!!.text.toString()
        val clientRenovat = Client(
            clientId,
            camping!!.text.toString(),
            "",
            nom!!.text.toString(),
            telefon!!.text.toString(),
            "",
            "",
            "",
            preu!!.text.toString(),
            parcial!!.text.toString(),
            feina!!.text.toString())

        if (camping!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Posa algo a concepte camping!", Toast.LENGTH_SHORT).show()
        } else {

            mDatabaseRef!!.child(clientId).setValue(clientRenovat)
            mAdapter.notifyDataSetChanged()
            finish()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            // if imatges multiples seleccionades
                if (data?.clipData != null) {

                val count = data.clipData!!.itemCount

                for (i in 0 until count) {

                    val imageUri: Uri? = data.clipData!!.getItemAt(i).uri
                    val filePath: StorageReference = mStorageRef!!.child(imageUri?.lastPathSegment!!) //posar nomes un .child si no es duplica per el mStorageRef
                    filePath.putFile(imageUri).addOnSuccessListener {
                        Toast.makeText(this@ClientDetall, "Foto pujada correctament", Toast.LENGTH_SHORT).show()
                    }

                    fotosClients.add(Imatge(imageUri.toString())) // Sempre Clase(Uri).toString()
                    adapterDetall.ImagesAdapter(fotosClients, this@ClientDetall) //nessesari
                    RecyclerDetall.adapter = adapterDetall //nessesari
                    adapterDetall.notifyDataSetChanged()

                }
                } else if (data?.data != null) {
                    // if imatge unica

                    val imageUri: Uri? = data.data
                    val filePath: StorageReference = mStorageRef!!.child(imageUri?.lastPathSegment!!) //posar nomes un .child si no es duplica per el mStorageRef
                    filePath.putFile(imageUri).addOnSuccessListener {
                        Toast.makeText(this@ClientDetall, "Foto pujada correctament", Toast.LENGTH_SHORT).show()
                    }
                    fotosClients.add(Imatge(imageUri.toString())) // Sempre Clase(Uri).toString()
                    adapterDetall.ImagesAdapter(fotosClients, this@ClientDetall) //nessesari
                    RecyclerDetall.adapter = adapterDetall //nessesari
                    adapterDetall.notifyDataSetChanged()
                }
        }
    }
}

    class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

        private var fotosClients: MutableList<Imatge> = ArrayList()
        lateinit var context: Context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val layoutInflater = LayoutInflater.from(parent.context)
            return ViewHolder(layoutInflater.inflate(R.layout.item_clientdetall, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val item = fotosClients[position]
            holder.bind(item, context)
        }

        override fun getItemCount(): Int {

            return fotosClients.size
        }

        fun ImagesAdapter(fotosClients: MutableList<Imatge>, context: Context) {

            this.fotosClients = fotosClients
            this.context = context
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val fotodetall = view.findViewById(R.id.icondetall) as ImageView

            fun bind(detall: Imatge, context: Context) {

                itemView.setOnClickListener {

                    val intentDetall = Intent(itemView.context, ActivityDetall::class.java)
                    intentDetall.putExtra("posicion", adapterPosition) //posicio click
                    itemView.context.startActivity(intentDetall)
                }

                itemView.setOnLongClickListener {

                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Eliminar foto")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("Eliminar foto") { _, _ ->
                        val perItemPosition = detall
                        borrarFoto(perItemPosition.imatgeGaleria)
                    }
                    
                    builder.setNegativeButton("Anular") { _, _ ->
                        return@setNegativeButton
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    true
                }

                fotodetall.loadUrl(detall.imatgeGaleria)
            }

            private fun borrarFoto(foto_id: String) {

                val fotoReferencia = getInstance().getReferenceFromUrl(foto_id)
                fotoReferencia.delete().addOnSuccessListener {

                    fotosClients.removeAt(adapterPosition)
                    adapterDetall.notifyDataSetChanged()

                }.addOnFailureListener {}
            }

            private fun ImageView.loadUrl(url: String) {

                if (url.isEmpty()) {

                    fotodetall.setImageResource(R.drawable.appicono_foreground)

                } else {

                    Picasso.with(context).load(url).into(fotodetall)
                }
            }
        }
    }






