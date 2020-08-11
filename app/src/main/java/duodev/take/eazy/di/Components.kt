package duodev.take.eazy.di

import dagger.Component
import duodev.take.eazy.TakeEasyApp
import duodev.take.eazy.base.BaseActivity
import duodev.take.eazy.base.BaseFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class, ApplicationModule::class, ViewModelModule::class])
interface Components {

    fun inject(takeEasyApp: TakeEasyApp)
    fun inject(baseActivity: BaseActivity)
    fun inject(baseFragment: BaseFragment)
}