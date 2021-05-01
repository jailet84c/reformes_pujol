import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude

data class Client (var clientid : String = "",
                   var camping : String = "",
                   var nom : String = "",
                   var telefon : String? = "Telefon",
                   var preu : String? = "",
                   var parcial : String? = "PiS",
                   var tipofeina : String = "")
//{

  /*  constructor() : this("", "", "", 0, 0.00, "") {

    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "clientid" to clientid,
                "camping" to camping,
                "nom" to nom,
                "preu" to preu,
                "telefon" to telefon,
                "tipofeina" to tipofeina
        )
    }
}
 */