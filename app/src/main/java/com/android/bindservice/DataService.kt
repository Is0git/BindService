package com.android.bindservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DataService : Service() {

    private val binder = LocalBinder()

    var text:String = "Hi"

    override fun onBind(p0: Intent?): IBinder? = binder

    suspend fun changeText(value:String ) : String {
        Log.d("TAG", "STARTED: $text")
        delay(5000)
        text = value
        return text
    }



   inner class LocalBinder : Binder() {
       fun getService() : DataService = this@DataService   }
}