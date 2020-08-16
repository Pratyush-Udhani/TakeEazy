package duodev.take.eazy.cart.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import duodev.take.eazy.base.BaseViewModel
import duodev.take.eazy.cart.Repo.CartRepo
import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.OrderItems
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(private val cartRepo: CartRepo): BaseViewModel(){

    fun fetchItems(storeId: String) = cartRepo.fetchItems(storeId)

    fun getStoreId() = cartRepo.getStoreId()
}