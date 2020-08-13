package duodev.take.eazy.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import duodev.take.eazy.TakeEasyApp
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule () {

    @Provides
    @Singleton
    fun provideApp(): Application {
        return TakeEasyApp.instance
    }
    @Provides
    @Singleton
    fun getContext(): Context = TakeEasyApp.instance

}