package duodev.take.eazy.cart.Repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.take.eazy.pojo.*
import duodev.take.eazy.pojo.pm
import duodev.take.eazy.utils.*
import javax.inject.Inject

class CartRepo @Inject constructor(private val firebaseFirestore: FirebaseFirestore) {

    fun fetchItems(storeId: String): MutableLiveData<List<CartItems>> {
        val data = MutableLiveData<List<CartItems>>()
        val itemsList: MutableList<CartItems> = mutableListOf()
        firebaseFirestore.collection(USERS).document(pm.phone).collection(CART).document(storeId)
            .collection(ITEMS).get().addOnSuccessListener {
                for (i in 0 until it.documents.size) {
                    itemsList.add(convertToPojo(it.documents[i].data!!, CartItems::class.java))
                }
                data.value = itemsList
            }
        return data
    }

    fun getStoreId(): MutableLiveData<String> {
        val data = MutableLiveData<String>()
        firebaseFirestore.collection(USERS).document(pm.phone).collection(CART).get()
            .addOnSuccessListener {
                if (it.documents.size != 0) {
                    val id = it.documents[0].get("storeId").toString()
                    data.value = id
                } else {
                    data.value = ""
                }
            }
        return data
    }

    fun clearCart() {
        firebaseFirestore.collection(USERS).document(pm.phone).collection(CART).get().addOnSuccessListener {
            for (element in it.documents)
                element.reference.delete()
        }
    }
}