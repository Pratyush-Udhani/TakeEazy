package duodev.take.eazy.SharedRepo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.OrderItems
import duodev.take.eazy.pojo.Service
import duodev.take.eazy.utils.*
import javax.inject.Inject

class SharedRepo @Inject constructor(private val firestore: FirebaseFirestore) {

    fun setData(item: CartItems) {
        firestore.collection(USERS).document(pm.phone).collection(CART)
            .document(item.storeId).set(hashMapOf("storeId" to item.storeId))

        firestore.collection(USERS).document(pm.phone).collection(CART)
            .document(item.storeId).collection(ITEMS).document(item.singleItem.itemId).set(item)
    }

    fun subtractData(item: CartItems) {
        if (item.quantity != 0) {
            firestore.collection(USERS).document(pm.phone).collection(CART)
                .document(item.storeId).collection(ITEMS).document(item.singleItem.itemId)
                .update("quantity", item.quantity)
        } else {
            firestore.collection(USERS).document(pm.phone).collection(CART)
                .document(item.storeId).collection(ITEMS).document(item.singleItem.itemId).delete()
        }
    }

    fun removeItemFromCart(itemId: String, storeId: String): MutableLiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        firestore.collection(USERS).document(pm.phone).collection(CART)
            .document(storeId).collection(ITEMS).document(itemId).delete().addOnSuccessListener {
                firestore.collection(USERS).document(pm.phone).collection(CART)
                    .document(storeId).collection(ITEMS).get().addOnSuccessListener {
                        if (it.isEmpty) {
                            firestore.collection(USERS).document(pm.phone).collection(CART).document(storeId).delete()
                            data.value = false
                        }
                    }
            }
        return data
    }
        fun orderItems(storeId: String) {

            firestore.collection(USERS).document(pm.phone).collection(CART).document(storeId)
                .collection(ITEMS).get().addOnSuccessListener {
                    log("called order, storeId: $storeId, it.size: ${it.documents.size}")
                    for (i in 0 until it.documents.size) {
                        val cartItem = convertToPojo(it.documents[i].data!!, CartItems::class.java)

                        val orderItems = OrderItems(
                            cartItem = cartItem,
                            timestamp = System.currentTimeMillis(),
                            orderId = getRandomString(),
                            status = "",
                            address = pm.address
                        )
                        removeItemFromCart(cartItem.singleItem.itemId, storeId)
                        firestore.collection(ORDERS).document(pm.phone)
                            .set(hashMapOf("userId" to pm.phone))

                        firestore.collection(ORDERS).document(pm.phone).collection(ORDERS)
                            .document(orderItems.orderId).set(orderItems)

                        if (i == it.documents.size - 1) {
                            firestore.collection(USERS).document(pm.phone).collection(CART)
                                .document(storeId).delete()
                        }
                    }
                }
        }

        fun orderService(service: Service) {
            firestore.collection(SERVICES).document(pm.phone).set(hashMapOf("userId" to pm.phone))

            firestore.collection(SERVICES).document(pm.phone).collection(ORDERS)
                .document(service.orderId).set(service)
        }

        fun fetchServices(): MutableLiveData<List<Service>> {
            val serviceList: MutableList<Service> = mutableListOf()
            val data = MutableLiveData<List<Service>>()
            firestore.collection(SERVICES).document(pm.phone).collection(ORDERS).get()
                .addOnSuccessListener {
                    for (i in 0 until it.documents.size) {
                        serviceList.add(convertToPojo(it.documents[i].data!!, Service::class.java))
                    }
                    data.value = serviceList
                }
            return data
        }
    }