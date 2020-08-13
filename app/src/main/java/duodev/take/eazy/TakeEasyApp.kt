package duodev.take.eazy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TakeEasyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: TakeEasyApp
            private set
    }
}