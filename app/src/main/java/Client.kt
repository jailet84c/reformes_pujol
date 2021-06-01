import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude

data class Client (var clientid : String? = "",
                   var camping : String? = "",
                   var data : String? = "",
                   var nom : String? = "",
                   var telefon : String? = "",
                   var concepte : String? = "",
                   var n1 : String? = "",
                   var preutiva : String? = "",
                   var preutotal : String? = "",
                   var parcial : String? = "",
                   var tipofeina : String? = "")