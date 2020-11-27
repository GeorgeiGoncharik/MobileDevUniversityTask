package xyz.goshanchik.prodavayka

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class ApplicationDelegate: Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(this)

        // applicationContext.deleteDatabase("commerce_database")
    }
}