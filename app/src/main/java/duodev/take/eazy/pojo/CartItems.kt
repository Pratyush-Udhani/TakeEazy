package duodev.take.eazy.pojo

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItems (
    @SerializedName("singleItem")
    val singleItem: SingleItem,

    @SerializedName("quantity")
    var quantity: Int = 0,

    @SerializedName("storeId")
    var storeId: String,

    @SerializedName("prescriptionUrl")
    var prescriptionUrl: Uri = Uri.parse(pm.prescription)

) : Parcelable {

}