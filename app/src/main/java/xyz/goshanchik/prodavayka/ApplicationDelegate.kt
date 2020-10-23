package xyz.goshanchik.prodavayka

import android.app.Application
import timber.log.Timber

class ApplicationDelegate: Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())


        //        applicationContext.deleteDatabase("commerce_database")
    }
}