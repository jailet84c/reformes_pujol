package com.reformespujol

import Pressupost
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

var pressupostosList : ArrayList<Pressupost> = ArrayList()
val adapterPressupost = PressupostAdapter(pressupostosList)
class LListaPressupostos : AppCompatActivity() {
    lateinit var recyclerPressupost: RecyclerView
    private var preDatabaseRef: DatabaseReference? = null
    private  var svPressupostos : SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.llista_pressupostos_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        recyclerPressupost = findViewById(R.id.recyclerpre)
        recyclerPressupost.setHasFixedSize(true)
        recyclerPressupost.layoutManager = LinearLayoutManager(this)
        svPressupostos = findViewById(R.id.searchllistapre)
        preDatabaseRef = FirebaseDatabase.getInstance().getReference("pressupostos")
        val floatingfoto = findViewById<View>(R.id.bfnoupre) as FloatingActionButton
        floatingfoto.setOnClickListener { obrirPressupost() }
        svPressupostos!!.setOnQueryTextListener(object: SearchView.OnQueryTextListener{    // SearchView
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapterPressupost.filter.filter(newText)
                return true
            }
        })
        val preListener = object : ValueEventListener {       // Rebre info pressupost
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pressupostosList.clear()
                    for(ds in snapshot.children) {
                        val stdpre = ds.getValue(Pressupost::class.java)
                        pressupostosList.add(stdpre!!)
                    }
                    adapterPressupost.pressupostAdapter(pressupostosList, this@LListaPressupostos)
                    recyclerPressupost.adapter = adapterPressupost
                    adapterPressupost.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        preDatabaseRef!!.addValueEventListener(preListener)        // Rebre info pressupost
    }
    private fun obrirPressupost() {
        val intentPre = Intent(this@LListaPressupostos, PressupostNou::class.java)
        startActivity(intentPre)
    }
}
class PressupostAdapter(private var pressupostosList: ArrayList<Pressupost>) : RecyclerView.Adapter<PressupostAdapter.ViewHolder>() ,Filterable {
    var preFilterList : ArrayList<Pressupost> = pressupostosList
    lateinit var contextpre: Context
    fun pressupostAdapter(pressupostosList: ArrayList<Pressupost>, contextpre: Context) {

         this.pressupostosList = pressupostosList
         this.contextpre = contextpre
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val datapress = view.findViewById(R.id.tvitemdata) as? TextView
        val campingpress = view.findViewById(R.id.tvitemcamping) as? TextView
        val nompress = view.findViewById(R.id.tvitemnombre) as? TextView
        val preutotalpress = view.findViewById(R.id.tvitempreu) as? TextView

        fun bind(detallpre: Pressupost, contextpre: Context) {

            campingpress?.text = detallpre.campingpre
            nompress?.text = detallpre.nompre
            datapress?.text = detallpre.datapre
            preutotalpress?.text = detallpre.preutotal.toString() //ojo
            itemView.setOnClickListener(View.OnClickListener {
                val positionpre = adapterPosition
                if (positionpre != RecyclerView.NO_POSITION) {
                    val intentpressupost = Intent(contextpre, Pressupostos::class.java).apply {
                        putExtra("ID", detallpre.campingpre)
                    }

                    itemView.context.startActivity(intentpressupost)
                }
            })
            itemView.setOnLongClickListener {
                val builderpre = AlertDialog.Builder(contextpre)
                builderpre.setTitle("Vols eliminar el pressupost?")
                builderpre.setIcon(android.R.drawable.ic_dialog_alert)
                builderpre.setPositiveButton("Eliminar pressupost") { _, _ ->
                    borrarPressupost(detallpre.pressid)
                }
                builderpre.setNegativeButton("Anular") { _, _ ->
                    return@setNegativeButton
                }
                val alertDialog: AlertDialog = builderpre.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }
        }
        private fun borrarPressupost(press_id: String) {
            val pressReference = FirebaseDatabase.getInstance().getReference("pressupostos").child(press_id)
          pressReference.removeValue()
          pressupostosList.removeAt(adapterPosition)
          adapterPressupost.notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_pressupost, parent, false))
    }
    override fun getItemCount(): Int = preFilterList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itempre = preFilterList[position]
         holder.bind(itempre, contextpre)
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                preFilterList = when {
                    charSearch.isEmpty() -> pressupostosList
                    else -> {
                        val internalFilteredList: ArrayList<Pressupost> = ArrayList()
                        for (data in pressupostosList) {
                            if (data.campingpre.contains(charSearch, ignoreCase = true)
                                    || data.nompre.contains(charSearch, ignoreCase = true))
                                     {
                                internalFilteredList.add(data)
                            }
                        }
                        internalFilteredList
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = preFilterList
                return filterResults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                preFilterList = results?.values as ArrayList<Pressupost>
                notifyDataSetChanged()
            }
        }
    }
}
