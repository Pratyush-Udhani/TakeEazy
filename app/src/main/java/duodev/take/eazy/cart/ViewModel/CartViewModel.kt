package duodev.take.eazy.cart.ViewModel

import duodev.take.eazy.base.BaseViewModel
import duodev.take.eazy.cart.Repo.CartRepo
import javax.inject.Inject

class CartViewModel @Inject constructor(private val cartRepo: CartRepo): BaseViewModel(){

    fun fetchItems(storeId: String) = cartRepo.fetchItems(storeId)

    fun getStoreId() = cartRepo.getStoreId()

    fun clearCart(storeId: String) = cartRepo.clearCart(storeId)
}