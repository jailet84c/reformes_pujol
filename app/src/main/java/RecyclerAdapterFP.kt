import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.reformespujol.*
import com.reformespujol.R
import java.util.*
import kotlin.collections.ArrayList


class RecyclerAdapterFP(private var feinespendents: ArrayList<Client>) : RecyclerView.Adapter<RecyclerAdapterFP.ViewHolder>(), Filterable {

    var fpFilterList : ArrayList<Client> = feinespendents
    lateinit var context: Context

    fun RecyclerAdapterFP(feinespendents: ArrayList<Client>, context: Context) {
        this.feinespendents = feinespendents
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_feinapendent, parent, false))
    }
    override fun getItemCount(): Int {
        return fpFilterList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = fpFilterList[position]
        holder.bind(item, context)
    }
    override fun getFilter(): Filter {
         return object : Filter() {
             override fun performFiltering(constraint: CharSequence?): FilterResults {
                 val charSearch = constraint.toString()
                 fpFilterList = when {
                     charSearch.isEmpty() -> feinespendents
                     else -> {
                         val internalFilteredList: ArrayList<Client> = ArrayList()
                         for (data in feinespendents) {
                             if (data.camping.contains(charSearch, ignoreCase = true)
                                     || data.nom.contains(charSearch, ignoreCase = true)
                                     || data.tipofeina.contains(charSearch, ignoreCase = true)) {
                                 internalFilteredList.add(data)
                             }
                         }
                         internalFilteredList
                     }
                 }
                 val filterResults = FilterResults()
                filterResults.values = fpFilterList
                return filterResults
             }
             @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                 fpFilterList = results?.values as ArrayList<Client>
                notifyDataSetChanged()
            }
         }
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val REQUEST_CODE = 0

        val camping = view.findViewById(R.id.tvcampinga) as TextView
        val nom = view.findViewById(R.id.tvnoma) as TextView
        val telefon = view.findViewById(R.id.tvtelefon) as TextView
        val preu = view.findViewById(R.id.tvpreu)  as TextView
        val parcial = view.findViewById(R.id.tvpis)  as TextView
        val feina = view.findViewById(R.id.tipofeina) as TextView

        var fotoportada = view.findViewById(R.id.fotoportada) as ImageView

        fun bind(feinespendents: Client, context: Context) {

            camping.text = feinespendents.camping
            nom.text = feinespendents.nom
            telefon.text = feinespendents.telefon
            preu.text = feinespendents.preu
            parcial.text = feinespendents.parcial
            feina.text = feinespendents.tipofeina

            itemView.setOnClickListener {
                val i = Intent(itemView.context, ClientDetall::class.java).apply {
                    putExtra("CAMPING", camping.text.toString())
                    putExtra("NOM", nom.text.toString())
                    putExtra("TELEFON", telefon.text.toString())
                    putExtra("PREU", preu.text.toString())
                    putExtra("PARCIAL", parcial.text.toString())
                    putExtra("FEINA", feina.text.toString())
                }
                itemView.context.startActivity(i)
            }

            itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Eliminar client?")
                builder.setMessage("Eliminar?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton("Eliminar client") { _, _ ->

                    val perItemPosition = feinespendents
                    borrarClient(perItemPosition.clientid)
                }
                builder.setNegativeButton("Anular") { _, _ ->
                    return@setNegativeButton
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }

            fotoportada.setOnLongClickListener{

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Cambiar foto de perfil")
                builder.setMessage("Vols cambiar foto?")
                builder.setIcon(android.R.drawable.ic_menu_gallery)
                builder.setPositiveButton("Si") { _, _ ->

                    val posicioFoto = feinespendents //
                    escollirFotoPerfil(posicioFoto.clientid, context)
                }
                builder.setNegativeButton("Anular") { _, _ ->
                    return@setNegativeButton
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }
        }

        private fun escollirFotoPerfil(position: String,context: Context) {

             // val intentFotoGal = Intent(context, EscollirFotoGaleria::class.java)
                val intentFotoGal = Intent(itemView.context, EscollirFotoGaleria::class.java)
                intentFotoGal.putExtra("fotoposicio", position)
                (context as Feines).startActivityForResult(intentFotoGal, REQUEST_CODE)

        }

        private fun borrarClient(client_id: String) {
            val clientsRef = FirebaseDatabase.getInstance().getReference("clients").child(client_id)
            clientsRef.removeValue()
            feinespendents.removeAt(adapterPosition)
            mAdapter.notifyDataSetChanged()
        }
    }
}


