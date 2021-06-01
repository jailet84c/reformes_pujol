package com.reformespujol

import Client
import RecyclerAdapterFP
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


lateinit var mRecyclerView : RecyclerView
var feinespendents : ArrayList<Client> = ArrayList()
val mAdapter : RecyclerAdapterFP = RecyclerAdapterFP(feinespendents)

class Feines : AppCompatActivity() {

    private var mMessageReference: DatabaseReference? = null
    lateinit var context: Context
    private var svClients: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feines)
        setSupportActionBar(findViewById(R.id.toolbar))

        mRecyclerView = findViewById(R.id.recyclerpre)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        svClients = findViewById(R.id.svClients)
        mMessageReference = FirebaseDatabase.getInstance().getReference("clients")

        val bfnouclient = findViewById<View>(R.id.bfnoupre) as FloatingActionButton
        bfnouclient.setOnClickListener { startActivity(Intent(this, agregarNouClient::class.java)) }
        svClients!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {    // SearchView

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filter.filter(newText)
                return true
            }
        })                                                                            // Searchview
        //rebre client
        val messageListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    feinespendents.clear()
                    for (ds in snapshot.children) {
                        val std = ds.getValue(Client::class.java)
                        feinespendents.add(std!!)
                    }
                    mAdapter.RecyclerAdapterFP(feinespendents, this@Feines)
                    mRecyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        mMessageReference!!.addValueEventListener(messageListener)
    }

}