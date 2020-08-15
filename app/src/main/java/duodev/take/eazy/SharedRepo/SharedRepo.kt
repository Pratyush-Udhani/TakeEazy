package duodev.take.eazy.SharedRepo

import com.google.firebase.firestore.FirebaseFirestore
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.OrderItems
import duodev.take.eazy.utils.*
import javax.inject.Inject

class SharedRepo @Inject constructor(private val firestore: FirebaseFirestore) {

    fun setData(item: CartItems) {
        firestore.collection(USERS).document("id").collection(CART)
            .document("store").set(hashMapOf("storeId" to item.storeId))

        firestore.collection(USERS).document("id").collection(CART)
            .document("store").collection(ITEMS).document(item.singleItem.itemId).set(item)
    }

    fun subtractData(item: CartItems) {
        if (item.quantity != 0) {
            firestore.collection(USERS).document("id").collection(CART)
                .document(item.storeId).collection(ITEMS).document(item.singleItem.itemId)
                .update("quantity", item.quantity)
        } else {
            firestore.collection(USERS).document("id").collection(CART)
                .document(item.storeId).collection(ITEMS).document(item.singleItem.itemId).delete()
        }
    }

    fun removeItemFromCart(itemId: String, storeId: String) {
        firestore.collection(USERS).document("id").collection(CART)
            .document(storeId).collection(ITEMS).document(itemId).delete()
    }

    fun orderItems() {
        var storeId = ""
        firestore.collection(USERS).document("id").collection(CART).document("store id")
            .get().addOnSuccessListener {
                storeId = it["storeId"].toString()
        }
        firestore.collection(USERS).document("id").collection(CART).document("store")
            .collection(ITEMS).get().addOnSuccessListener {
                for (i in 0 until it.documents.size) {
                    val cartItem = convertToPojo(it.documents[i].data!!, CartItems::class.java)

                    val orderItems = OrderItems(
                        cartItem = cartItem,
                        timestamp = System.currentTimeMillis(),
                        orderId = getRandomString(),
                        status = ""
                    )
                    removeItemFromCart(cartItem.singleItem.itemId, storeId)

                    firestore.collection(ORDERS).document("id").collection(ORDERS)
                        .document(orderItems.orderId).set(orderItems)

                }
            }
    }
}