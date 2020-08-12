package duodev.take.eazy.SharedViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import duodev.take.eazy.base.BaseViewModel
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.SharedRepo.SharedRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class SharedViewModel @Inject constructor(private val sharedRepo: SharedRepo): BaseViewModel() {

    fun setData(item: CartItems) {
        viewModelScope.launch {
            sharedRepo.setData(item)
        }
    }

    fun subtractData(item:CartItems) {
        viewModelScope.launch {
            sharedRepo.subtractData(item)
        }
    }

    fun removeFromCart(itemId: String) {
        viewModelScope.launch {
            sharedRepo.removeItemFromCart(itemId)
        }
    }
}