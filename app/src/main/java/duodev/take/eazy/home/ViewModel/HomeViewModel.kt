package duodev.take.eazy.home.ViewModel

import duodev.take.eazy.base.BaseViewModel
import duodev.take.eazy.home.Repo.HomeRepo
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeRepo: HomeRepo): BaseViewModel() {

    fun fetchData() = homeRepo.fetchStores()
}