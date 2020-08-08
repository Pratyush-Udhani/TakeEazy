package duodev.take.eazy

import android.app.Application
import duodev.take.eazy.di.ApplicationModule
import duodev.take.eazy.di.Components
import duodev.take.eazy.di.DaggerComponents

class TakeEasyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDagger()
    }

    private fun initializeDagger() {
        components = DaggerComponents.builder().applicationModule(ApplicationModule(this)).build()
        components.inject(this)
    }


    companion object {
        lateinit var instance: TakeEasyApp
            private set
        lateinit var components: Components
            private set
    }
}