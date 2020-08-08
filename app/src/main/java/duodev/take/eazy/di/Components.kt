package duodev.take.eazy.di

import dagger.Component
import duodev.take.eazy.TakeEasyApp
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class, ApplicationModule::class,ViewModelModule::class])
interface Components {

    fun inject(takeEasyApp: TakeEasyApp)


}