package duodev.take.eazy.stores.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import duodev.take.eazy.base.BaseViewModel
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.SingleItem
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.stores.Repo.StoreRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoreViewModel @Inject constructor(private val storeRepo: StoreRepo): BaseViewModel() {

    private var _items = MutableLiveData<List<Items>>()
    val items: LiveData<List<Items>>
        get() = _items

    fun fetchData() = storeRepo.fetchStores()

    fun fetchItems() {
        viewModelScope.launch {
            val response = storeRepo.fetchItems()
            if (response != null) {
                _items = response
            }
        }
    }

    fun setData(item: CartItems, store: Store) {
        viewModelScope.launch {
            storeRepo.setData(item, store)
        }
    }

    fun subtractData(item:CartItems, store: Store) {
        viewModelScope.launch {
            storeRepo.subtractData(item, store)
        }
    }

    fun removeFromCart(itemId: String) {
        viewModelScope.launch {
            storeRepo.removeItemFromCart(itemId)
        }
    }
}