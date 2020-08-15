package duodev.take.eazy.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItems (
    @SerializedName("cartItem")
    val singleItem: SingleItem,

    @SerializedName("quantity")
    var quantity: Int,

    @SerializedName("storeId")
    var storeId: String

) : Parcelable