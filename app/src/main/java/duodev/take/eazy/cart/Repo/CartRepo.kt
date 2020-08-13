package duodev.take.eazy.cart.Repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import duodev.take.eazy.pojo.*
import javax.inject.Inject

class CartRepo @Inject constructor(private val firebaseFirestore: FirebaseFirestore) {

    private val itemsList: MutableList<OrderItems> = mutableListOf()

    suspend fun fetchItems(): MutableLiveData<List<OrderItems>> {
        val data = MutableLiveData<List<OrderItems>>()
        itemsList.add(
            OrderItems(
                store = Store(
                    storeId = "",
                    storeName = "Store",
                    storeAddress = "",
                    storeImageUri = "",
                    storeLocation = GeoPoint(21.11, 22.57),
                    storeCategory = "Pet supplies"
                ),
                itemList = listOf(
                    CartItems(
                        cartItem =
                        SingleItem(
                            itemId = "",
                            itemName = "name 1",
                            itemImageUri = "",
                            itemPrice = ""
                        ),
                        quantity = 3
                    ), CartItems(
                        cartItem =
                        SingleItem(
                            itemId = "",
                            itemName = "name 2",
                            itemImageUri = "",
                            itemPrice = ""
                        ),
                        quantity = 3
                    )
                )
            )
        )
        data.value = itemsList
        return data
    }

}