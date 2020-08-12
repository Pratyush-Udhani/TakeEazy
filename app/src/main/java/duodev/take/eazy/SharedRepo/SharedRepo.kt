package duodev.take.eazy.SharedRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.utils.DefItems
import javax.inject.Inject

class SharedRepo @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun setData(item: CartItems) {

    }

    suspend fun subtractData(item: CartItems) {

    }

    suspend fun removeItemFromCart(itemId: String) {

    }
}