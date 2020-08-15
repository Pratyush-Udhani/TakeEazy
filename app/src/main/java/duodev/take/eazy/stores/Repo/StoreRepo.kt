package duodev.take.eazy.stores.Repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.SingleItem
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.utils.CATEGORIES
import duodev.take.eazy.utils.DefItems
import duodev.take.eazy.utils.STORES
import duodev.take.eazy.utils.convertToPojo
import javax.inject.Inject

class StoreRepo @Inject constructor(private val firestore: FirebaseFirestore) {

    private val storeList: MutableList<Store> = mutableListOf()
    private val categoryList: MutableList<String> = mutableListOf()

    fun fetchStores(): LiveData<List<Store>> {
        val data = MutableLiveData<List<Store>>()

        for (i in 0..5) {
            storeList.add(
                Store(
                    storeId = "",
                    storeName = "Store $i",
                    storeAddress = "",
                    storeImageUri = "",
                    storeLocation = GeoPoint(21.11 + i, 22.57),
                    storeCategory = "Pet supplies"
                )
            )
        }
        data.value = storeList
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

    fun fetchSingleItemList(category: String, storeId: String): MutableLiveData<List<SingleItem>> {
        val data = MutableLiveData<List<SingleItem>>()
        val singleItemList: MutableList<SingleItem> = mutableListOf()
        firestore.collection(STORES).document(storeId).collection(category)
            .get().addOnSuccessListener {
                for (i in 0 until it.documents.size) {
                    if (it.documents[i].exists()) {
                        singleItemList.add(
                            convertToPojo(it.documents[i].data!!, SingleItem::class.java)
                        )
                    }
                }
                data.value = singleItemList
            }
        return data
    }

}