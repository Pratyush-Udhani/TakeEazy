package duodev.take.eazy.orders.Repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import duodev.take.eazy.pojo.OrderItems
import duodev.take.eazy.pojo.pm
import duodev.take.eazy.utils.ORDERS
import duodev.take.eazy.utils.convertToPojo
import javax.inject.Inject

class OrdersRepo @Inject constructor(private val firestore: FirebaseFirestore) {

    private val ordersList: MutableList<OrderItems> = mutableListOf()

    fun fetchOrders(): MutableLiveData<List<OrderItems>> {
        val data = MutableLiveData<List<OrderItems>>()
        firestore.collection(ORDERS).document(pm.phone).collection(ORDERS).get().addOnSuccessListener {
            for (i in 0 until it.documents.size) {
                ordersList.add(convertToPojo(it.documents[i].data!!, OrderItems::class.java))
            }
            data.value = ordersList
        }
        return data
    }

}