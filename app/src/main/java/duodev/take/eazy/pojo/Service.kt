package duodev.take.eazy.pojo

import com.google.gson.annotations.SerializedName

data class Service (

    @SerializedName("storeAddress")
    val storeAddress: String,

    @SerializedName("deliveryAddress")
    val deliveryAddress: String,

    @SerializedName("items")
    val items: String,

    @SerializedName("orderId")
    var orderId : String
)