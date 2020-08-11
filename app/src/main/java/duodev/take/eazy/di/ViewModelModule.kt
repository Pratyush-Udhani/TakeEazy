package duodev.take.eazy.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import duodev.take.eazy.stores.ViewModel.StoreViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(StoreViewModel::class)
    abstract fun homeViewModel(storeViewModel: StoreViewModel): ViewModel



}