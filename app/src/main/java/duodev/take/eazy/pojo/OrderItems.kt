package duodev.take.eazy.pojo

import com.google.gson.annotations.SerializedName

data class OrderItems (

    @SerializedName("orderId")
    var orderId: String = "",

    @SerializedName("item")
    val cartItem: CartItems,

    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("status")
    val status: String
)