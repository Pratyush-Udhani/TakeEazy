package duodev.take.eazy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TakeEasyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
     //   initializeDagger()
    }

//    private fun initializeDagger() {
//        components = DaggerComponents.builder().applicationModule(ApplicationModule(this)).build()
//        components.inject(this)
//    }


    companion object {
        lateinit var instance: TakeEasyApp
            private set
    }
}