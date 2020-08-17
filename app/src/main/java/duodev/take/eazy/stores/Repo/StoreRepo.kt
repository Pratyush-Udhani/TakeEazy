package duodev.take.eazy.stores.Repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.SingleItem
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.utils.*
import javax.inject.Inject

class StoreRepo @Inject constructor(private val firestore: FirebaseFirestore) {

    private val storeList: MutableList<Store> = mutableListOf()
    private val categoryList: MutableList<String> = mutableListOf()
    private val pm = PreferenceUtils

    fun fetchStores(): LiveData<List<Store>> {
        val data = MutableLiveData<List<Store>>()
        firestore.collection(STORES).get().addOnSuccessListener {
            for (i in 0 until it.documents.size) {
                storeList.add(convertToPojo(it.documents[i].data!!, Store::class.java))
            }
        data.value = storeList
        }
        return data
    }

    fun fetchCategories(storeId: String): MutableLiveData<List<String>> {
        val data = MutableLiveData<List<String>>()

        firestore.collection(STORES).document(storeId).collection(CATEGORIES)
            .get().addOnSuccessListener {
                for (docs in it) {
                    categoryList.addAll(docs.data.values as Collection<String>)
                }
                data.value = categoryList
            }
        return data
    }

    fun fetchSingleItemList(category: String, storeId: String): MutableLiveData<List<CartItems>> {
        val data = MutableLiveData<List<CartItems>>()
        val itemList: MutableList<CartItems> = mutableListOf()
        firestore.collection(STORES).document(storeId).collection(category)
            .get().addOnSuccessListener {
                for (i in 0 until it.documents.size) {
                    if (it.documents[i].exists()) {
                        itemList.add(
                            CartItems(
                                convertToPojo(it.documents[i].data!!, SingleItem::class.java), 0, storeId
                            )
                        )
                    }
                }
                data.value = itemList
            }
        return data
    }

    fun fetchCartList(storeId: String) : MutableLiveData<List<CartItems>> {
        val data = MutableLiveData<List<CartItems>>()
        val cartList: MutableList<CartItems> = mutableListOf()

        firestore.collection(USERS).document(pm.phone).collection(CART)
            .document(storeId).collection(ITEMS).get().addOnSuccessListener {
                for (i in 0 until it.documents.size) {
                    cartList.add(convertToPojo(it.documents[i].data!!, CartItems::class.java))
                }
                data.value = cartList
            }
        return data
    }
}