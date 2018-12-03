package br.com.mobapps.dynamicfeature

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat

class App: Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

}