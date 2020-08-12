package duodev.take.eazy.stores.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import duodev.take.eazy.base.BaseViewModel
import duodev.take.eazy.pojo.Items
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
}