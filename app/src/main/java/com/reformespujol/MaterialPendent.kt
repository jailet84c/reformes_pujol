package com.reformespujol

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.*

class MaterialPendent : AppCompatActivity() {

    private val channelID = "channelID"
    private val channelName = "Notificacions"
    private val notificationId = 0

    private var materialRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_pendent)

        setSupportActionBar(findViewById(R.id.toolbar))

        val etmaterial = findViewById<EditText>(R.id.etMaterials)
        val btGuardar = findViewById<Button>(R.id.btGMaterials)

        materialRef = FirebaseDatabase.getInstance().getReference("Material")

        createNotificationChannel()

        btGuardar.setOnClickListener {

            val textMaterial: String = etmaterial.text.toString().trim()

            materialRef!!.setValue(textMaterial)

            val intent = Intent(this, MaterialPendent::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val notification = NotificationCompat.Builder(this, channelID).also {
                it.setContentTitle("Falta per carregar...")
                it.setContentText(textMaterial)
                it.setSmallIcon(R.drawable.ic_notificacio)
                it.priority = NotificationCompat.PRIORITY_HIGH
                it.setContentIntent(pendingIntent)
                it.setAutoCancel(true)
            }.build()

            val notificationManager = NotificationManagerCompat.from(this)

            if (textMaterial.isEmpty())
                Toast.makeText(this@MaterialPendent, "Res per carregar", Toast.LENGTH_SHORT).show()
            else
                 notificationManager.notify(notificationId, notification)

            finish()
        }

        val rebreMaterial = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val textMaterial: String? = snapshot.getValue(String::class.java)
                    etmaterial.setText(textMaterial)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        materialRef!!.addValueEventListener(rebreMaterial)

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelID, channelName, importance).apply {
                lightColor = Color.RED
                enableLights(true)
            }

            val manager : NotificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}