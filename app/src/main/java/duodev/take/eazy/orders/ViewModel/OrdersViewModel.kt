package duodev.take.eazy.orders.ViewModel

import duodev.take.eazy.base.BaseViewModel
import duodev.take.eazy.orders.Repo.OrdersRepo
import javax.inject.Inject

class OrdersViewModel @Inject constructor(private val ordersRepo: OrdersRepo) : BaseViewModel() {

    fun fetchOrders() = ordersRepo.fetchOrders()

}