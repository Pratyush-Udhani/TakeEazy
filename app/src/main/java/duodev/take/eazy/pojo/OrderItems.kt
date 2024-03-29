package duodev.take.eazy.pojo

import com.google.gson.annotations.SerializedName

data class OrderItems (

    @SerializedName("userAddress")
    var address: String,

    @SerializedName("orderId")
    var orderId: String = "",

    @SerializedName("cartItem")
    val cartItem: CartItems,

    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("status")
    val status: String
)