package duodev.take.eazy.pojo

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Store (

    @SerializedName("storeId")
    val storeId : String,

    @SerializedName("storeName")
    val storeName: String,

    @SerializedName("storeLocation")
    @IgnoredOnParcel
    val storeLocation: @RawValue GeoPoint,

    @SerializedName("storeImageUri")
    val storeImageUri: String,

    @SerializedName("storeCategory")
    val storeCategory: String,

    @SerializedName("storeAddress")
    val storeAddress: String

) : Parcelable