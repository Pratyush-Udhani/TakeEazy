package duodev.take.eazy.pojo

import com.google.gson.annotations.SerializedName

data class OrderItems (

    @SerializedName("store")
    val store: Store,

    @SerializedName("itemList")
    val itemList: List<CartItems>
)