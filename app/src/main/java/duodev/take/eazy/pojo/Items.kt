package duodev.take.eazy.pojo

import com.google.gson.annotations.SerializedName

data class Items (

    @SerializedName("itemGroup")
    val itemGroup: String,

    @SerializedName("itemList")
    var itemList: List<CartItems>
)