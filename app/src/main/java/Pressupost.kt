import com.google.firebase.database.Exclude

data class Pressupost (var pressid : String = "",
                       var datapre : String = "",
                       var campingpre: String ="",
                       var nompre: String = "",
                       var telefon : String = "",
                       var conceptepre : String = "",
                       var n1 : String? = null,
                       var n2 : String? = null,
                       var n3 : String? = null,
                       var n4 : String? = null,
                       var n5 : String? = null,
                       var n6 : String? = null,
                       var n7 : String? = null,
                       var preutiva : String? = null,
                       var preutotal : String? = null )
/* {

    constructor() : this("", "", "", "", "",0,0,0,0,0,0,0,0.00,0.00) {

    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "pressid" to pressid,
                "data" to datapre,
                "camping" to campingpre,
                "nom" to nompre,
                "concepte" to conceptepre,
                "numero1" to n1,
                "numero2" to n2,
                "numero3" to n3,
                "numero4" to n4,
                "numero5" to n5,
                "numero6" to n6,
                "numero7" to n7,
                "preuiva" to preutiva,
                "preutotal" to preutotal )
    }
} */