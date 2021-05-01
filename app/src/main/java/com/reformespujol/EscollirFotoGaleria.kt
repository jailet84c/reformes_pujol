package com.reformespujol

import Imatge
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

lateinit var RecyclerGaleria: RecyclerView
val fotosClientsGal: MutableList<Imatge> = ArrayList()

class EscollirFotoGaleria : AppCompatActivity() {

    private var mStorageRef : StorageReference? = null

    val adaptadorGaleria = AdaptadorFotoGaleria()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.escollir_foto_galeria)

        setSupportActionBar(findViewById(R.id.toolbar))

        RecyclerGaleria = findViewById(R.id.recyclerFotoGaleria)
        RecyclerGaleria.setHasFixedSize(true)
        RecyclerGaleria.layoutManager = GridLayoutManager(this, 3)

        rebreFotosGaleria()

    }

    private fun rebreFotosGaleria() {

        val result: Bundle? = this.intent.extras
        val campingup: String? = result?.getString("fotoposicio")

        val storage = FirebaseStorage.getInstance()
        mStorageRef = storage.reference.child("Fotos Clients").child(campingup!!)

        fotosClientsGal.clear() // Evitar que es repeteixin les fotos

        val listAllTask: Task<ListResult> = mStorageRef!!.listAll()                // Mostrar imatges Firebase Storage

        listAllTask.addOnCompleteListener { result ->

            val items: List<StorageReference> = result.result!!.items

            items.forEachIndexed { _, item ->

                item.downloadUrl.addOnSuccessListener {

                    Log.d("item", "$it")

                    fotosClientsGal.add(Imatge(it.toString()))

                }.addOnCompleteListener {

                    adaptadorGaleria.AdaptadorFotoGaleria(fotosClientsGal, this@EscollirFotoGaleria)//nessesari
                    RecyclerGaleria.adapter = adaptadorGaleria //nessesari
                    adaptadorGaleria.notifyDataSetChanged()
                }
            }
        }
    }
}

 class AdaptadorFotoGaleria : RecyclerView.Adapter<AdaptadorFotoGaleria.ViewHolder>() {

     var fotosClientsGal: MutableList<Imatge>  = ArrayList()
     lateinit var context:Context

    fun AdaptadorFotoGaleria(fotosClientsGal: MutableList<Imatge>,context: Context){

        this.fotosClientsGal = fotosClientsGal
        this.context = context

    }

     class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {

         var fotodetallnova = view.findViewById(R.id.icondetall) as ImageView

        fun bind(detallnou: Imatge, context: Context) {

            itemView.setOnClickListener(View.OnClickListener {

                val intentTornada = Intent()
                intentTornada.putExtra("fototornada", detallnou.imatgeGaleria)
                (context as EscollirFotoGaleria).setResult(RESULT_OK, intentTornada)
                context.finish()
                
            })

            fotodetallnova.loadUrl(detallnou.imatgeGaleria)
        }

        private fun ImageView.loadUrl(direcciofoto: String) {

            if (direcciofoto.isEmpty()) {
                fotodetallnova.setImageResource(R.drawable.appicono_foreground)
            } else {
                Picasso.with(context).load(direcciofoto).into(fotodetallnova)
            }
        }
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaterGaleria = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflaterGaleria.inflate(R.layout.item_clientdetall, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = fotosClientsGal[position]
        holder.bind(item, context)
    }

    override fun getItemCount(): Int = fotosClientsGal.size
 }


