package duodev.take.eazy.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item (

    @SerializedName("itemName")
    val itemName: String,

    @SerializedName("itemImageUri")
    val itemImageUri: String,

    @SerializedName("itemPrice")
    val itemPrice: String

) : Parcelable