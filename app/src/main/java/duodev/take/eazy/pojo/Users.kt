package duodev.take.eazy.pojo

import com.google.gson.annotations.SerializedName
import duodev.take.eazy.utils.PreferenceUtils

val pm = PreferenceUtils

data class Users (

    @SerializedName("userPhone")
    val userPhone: String = pm.phone,

    @SerializedName("hash")
    val userPassword: String = pm.hash,

    @SerializedName("userAddress")
    val userAddress: String = ""

)