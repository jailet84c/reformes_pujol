package com.reformespujol

import ClientPendent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

lateinit var RecyclerPendent : RecyclerView
var llistaPendent : ArrayList<ClientPendent> = ArrayList()
val adapterFeinaPendent : AdapterFeinaP = AdapterFeinaP(llistaPendent)

class FeinesPendents : AppCompatActivity() {

    private var referenciaPendent: DatabaseReference? = null
    lateinit var context: Context
    private var svClientsPendents: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feines_pendents)
        setSupportActionBar(findViewById(R.id.toolbar))

        RecyclerPendent = findViewById(R.id.recyclerpendent)
        RecyclerPendent.setHasFixedSize(true)
        RecyclerPendent.layoutManager = LinearLayoutManager(this)
        svClientsPendents = findViewById(R.id.svClientsPendents)
        referenciaPendent = FirebaseDatabase.getInstance().getReference("clientsPendents")

        val botoFP = findViewById<View>(R.id.btfeinapendent) as FloatingActionButton
        botoFP.setOnClickListener { startActivity(Intent(this, agregarNouClient::class.java)) }

        svClientsPendents?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {    // SearchView
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterFeinaPendent.filter.filter(newText)
                return true
            }
        })

        //rebre client
        val pendentListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    llistaPendent.clear()
                    for (ds in snapshot.children) {
                        val std = ds.getValue(ClientPendent::class.java)
                        llistaPendent.add(std!!)
                    }
                    adapterFeinaPendent.adapterFeinaP(llistaPendent, this@FeinesPendents)
                    RecyclerPendent.adapter = adapterFeinaPendent
                    adapterFeinaPendent.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        referenciaPendent!!.addValueEventListener(pendentListener)
    }

    }

class AdapterFeinaP(private var llistaFP: ArrayList<ClientPendent>) : RecyclerView.Adapter<AdapterFeinaP.ViewHolder>(),
    Filterable {

    var fpFilter : ArrayList<ClientPendent> = llistaFP
    lateinit var context: Context

    fun adapterFeinaP(llistaFP: ArrayList<ClientPendent>, context: Context) {

        this.llistaFP = llistaFP
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.casella_feinapendent, parent, false))
    }
    override fun getItemCount(): Int {
        return fpFilter.size
    }

    override fun onBindViewHolder(holder: AdapterFeinaP.ViewHolder, position: Int) {
        val item = fpFilter[position]
        holder.bind(item, context)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                fpFilter = when {
                    charSearch.isEmpty() -> llistaFP
                    else -> {
                        val internalFilteredList: ArrayList<ClientPendent> = ArrayList()
                        for (data in llistaFP) {
                            if (data.camping!!.contains(charSearch, ignoreCase = true)
                                || data.nom!!.contains(charSearch, ignoreCase = true)
                                || data.tipofeina!!.contains(charSearch, ignoreCase = true)) {
                                internalFilteredList.add(data)
                            }
                        }
                        internalFilteredList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = fpFilter
                return filterResults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                fpFilter = results?.values as ArrayList<ClientPendent>
                notifyDataSetChanged()
            }
        }
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val camping = view.findViewById(R.id.tvcampinga) as TextView
        val nom = view.findViewById(R.id.tvnoma) as TextView
        private val preu = view.findViewById(R.id.tvpreu)  as TextView
        private val feina = view.findViewById(R.id.tipofeina) as TextView

        fun bind(clientFP: ClientPendent, context: Context) {

            camping.text = clientFP.camping
            nom.text = clientFP.nom
            preu.text = clientFP.preutotal
            feina.text = clientFP.tipofeina

            itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Eliminar client?")
                builder.setMessage("Eliminar?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton("Eliminar client") { _, _ ->

                    borrarClient(clientFP.clientid!!)
                }
                builder.setNegativeButton("Anular") { _, _ ->
                    return@setNegativeButton
                }
                builder.setNeutralButton("Veure pressupost ") { _, _ ->
                    veurePressupost(clientFP.clientid)
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }
        }

        private fun veurePressupost(posicio: String?) {
            val intentPessFP = Intent(itemView.context, Pressupostos::class.java).apply {
                putExtra("dadesFP", posicio)
            }
            itemView.context.startActivity(intentPessFP)
        }

        private fun borrarClient(client_id: String) {
            val clientsRef = FirebaseDatabase.getInstance().getReference("clientsPendents").child(client_id)
            clientsRef.removeValue()
            llistaPendent.removeAt(adapterPosition)
            adapterFeinaPendent.notifyDataSetChanged()
        }
    }
}
