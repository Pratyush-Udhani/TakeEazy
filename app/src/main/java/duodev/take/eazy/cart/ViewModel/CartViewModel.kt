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

    private var _items = MutableLiveData<List<OrderItems>>()
    val items: LiveData<List<OrderItems>>
        get() = _items

    fun fetchItems() {
        viewModelScope.launch {
            val response = cartRepo.fetchItems()
            if (response != null) {
                _items = response
            }
        }
    }
}