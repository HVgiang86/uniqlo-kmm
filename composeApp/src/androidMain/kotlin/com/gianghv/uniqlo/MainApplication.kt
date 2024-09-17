package com.gianghv.uniqlo

import android.app.Application
import android.util.Log
import com.gianghv.uniqlo.rootview.AppInitializer
import com.gianghv.uniqlo.util.logging.AppLogger
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Log.d("HEHE","App onCreated")
//        NotifierManager.initialize(
//            NotificationPlatformConfiguration.Android(
//                notificationIconResId = R.drawable.ic_notification,
//                notificationIconColorResId = R.color.orange,
//                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
//                    id = "CHANNEL_ID_GENERAL",
//                    name = "General"
//                )
//            )
//        )
        AppInitializer.initialize(isDebug = true) {
            androidContext(this@MainApplication)
        }
    }
}
