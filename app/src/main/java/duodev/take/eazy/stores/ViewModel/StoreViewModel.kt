package duodev.take.eazy.stores.ViewModel

import duodev.take.eazy.base.BaseViewModel
import duodev.take.eazy.stores.Repo.StoreRepo
import javax.inject.Inject

class StoreViewModel @Inject constructor(private val storeRepo: StoreRepo): BaseViewModel() {

    fun fetchData() = storeRepo.fetchStores()
}