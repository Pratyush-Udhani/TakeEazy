package duodev.take.eazy.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoMap
import duodev.take.eazy.cart.ViewModel.CartViewModel
import duodev.take.eazy.SharedViewModel.SharedViewModel
import duodev.take.eazy.stores.ViewModel.StoreViewModel

@Module
@InstallIn(ApplicationComponent::class)
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    abstract fun sharedViewModel(sharedViewModel: SharedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StoreViewModel::class)
    abstract fun storeViewModel(storeViewModel: StoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    abstract fun cartViewModel(cartViewModel: CartViewModel): ViewModel

}