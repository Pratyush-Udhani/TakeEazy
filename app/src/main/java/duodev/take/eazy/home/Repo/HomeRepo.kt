package duodev.take.eazy.home.Repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.take.eazy.pojo.Item
import duodev.take.eazy.pojo.Store
import javax.inject.Inject

class HomeRepo @Inject constructor(private val firestore: FirebaseFirestore) {

    private val storeList: MutableList<Store> = mutableListOf()

    fun fetchStores(): LiveData<List<Store>> {
        val data = MutableLiveData<List<Store>>()

        for (i in 0..5) {
            storeList.add(
                Store(
                    storeName = "Store $i",
                    storeAddress = "",
                    storeImageUri = "",
                    storeLocation = GeoPoint(21.11 + i, 22.57),
                    category = "",
                    itemsList = listOf(
                        Item(
                            itemName = "Item 1",
                            itemImageUri = "",
                            itemPrice = "89"
                        )
                    )
                )
            )
        }
        data.value = storeList
        return data
    }

}