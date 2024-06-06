package com.example.yandexmaptest.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("83df0638-4b29-49c9-8424-457b32a08196")
    }
}