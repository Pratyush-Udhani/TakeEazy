package duodev.take.eazy.cart.Repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.take.eazy.pojo.*
import duodev.take.eazy.utils.CART
import duodev.take.eazy.utils.USERS
import duodev.take.eazy.utils.convertToPojo
import javax.inject.Inject

class CartRepo @Inject constructor(private val firebaseFirestore: FirebaseFirestore) {

    private val itemsList: MutableList<CartItems> = mutableListOf()

    fun fetchItems(): MutableLiveData<List<CartItems>> {
        val data = MutableLiveData<List<CartItems>>()

        firebaseFirestore.collection(USERS).document("id").collection(CART)
            .get().addOnSuccessListener {
                for (i in 0 until it.documents.size) {
                    itemsList.add(convertToPojo(it.documents[i].data!!, CartItems::class.java))
                }
                data.value = itemsList
            }

        return data
    }

}