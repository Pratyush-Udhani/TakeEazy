package duodev.take.eazy.stores.Repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.utils.DefItems
import javax.inject.Inject

class StoreRepo @Inject constructor(private val firestore: FirebaseFirestore) {

    private val storeList: MutableList<Store> = mutableListOf()
    private val itemsList: MutableList<Items> = mutableListOf()

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

    suspend fun fetchItems() : MutableLiveData<List<Items>> {
        val data = MutableLiveData<List<Items>>()
        itemsList.addAll(DefItems.getItems())
        data.value = itemsList
        return data
    }

}