package duodev.take.eazy.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import duodev.take.eazy.TakeEasyApp
import javax.inject.Singleton

@Module
class ApplicationModule (var takeEasyApp: TakeEasyApp) {

    @Provides
    @Singleton
    fun provideApp(): Application {
        return takeEasyApp
    }
    @Provides
    @Singleton
    fun getContext(): Context = takeEasyApp

}